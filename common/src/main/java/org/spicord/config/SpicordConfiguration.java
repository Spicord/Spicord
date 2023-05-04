/*
 * Copyright (C) 2019  OopsieWoopsie
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.spicord.config;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.spicord.Spicord;
import org.spicord.bot.DiscordBot;
import org.spicord.config.SpicordConfiguration.SpicordConfig.Bot;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;

import lombok.Getter;

// this is a mess, i know, will be re-made in the future :)
public final class SpicordConfiguration {

    private final Spicord spicord;

    @Getter private final Set<DiscordBot> bots;
    @Getter private final File dataFolder;
    @Getter private boolean debugEnabled;
    @Getter private boolean jdaMessagesEnabled;
    @Getter private String integratedAddonFooter;
    @Getter private int loadDelay;

    private final File configFile;
    private final TomlWriter writer;
    private final Logger logger;
    private SpicordConfig config;

    @Getter private final ConfigurationManager manager;

    public SpicordConfiguration(Spicord spicord, final File dataFolder) {
        this.spicord = spicord;

        this.bots = Collections.synchronizedSet(new HashSet<DiscordBot>());

        this.dataFolder = dataFolder;
        this.dataFolder.mkdirs();
        this.configFile = new File(dataFolder, "config.toml");
        this.logger = spicord.getLogger();

        this.writer = new TomlWriter.Builder()
                .indentValuesBy(2)
                .padArrayDelimitersBy(1)
                .build();

        this.load();

        this.manager = new ConfigurationManager(config);
    }

    public void load() {
        this.saveDefault();

        final Toml toml = new Toml().read(configFile);
        this.config = toml.to(SpicordConfig.class);

        for (final SpicordConfig.Bot botData : config.bots) {
            final DiscordBot bot = new DiscordBot(
                    spicord,
                    botData.name,
                    botData.token,
                    botData.enabled,
                    botData.addons,
                    botData.command_support,
                    botData.command_prefix
                );

            bots.add(bot);
        }

        this.loadDelay = config.loadDelay >= 10 ? config.loadDelay : 10;
        this.jdaMessagesEnabled = config.jda_messages.enabled;
        this.debugEnabled = config.jda_messages.debug;
        this.integratedAddonFooter = config.integrated_addon_footer;

        long disabledCount = bots.stream().filter(DiscordBot::isDisabled).count();
        logger.info("Loaded " + bots.size() + " bots, " + disabledCount + " disabled.");
    }

    public void save() {
        this.saveDefault();

        try (final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                final FileOutputStream fos = new FileOutputStream(configFile)) {
            writer.write(config, baos);
            String str = fixIndentation(new String(baos.toByteArray()));
            fos.write(str.getBytes(Charset.forName("UTF-8")));
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveDefault() {
        if (!configFile.exists()) {
            try (final InputStream in = getClass().getResourceAsStream("/config.toml")) {
                Files.copy(in, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * This adds the comment at the top of the file and
     * fixes the toml indentation bug for some values
     */
    private String fixIndentation(String content) {
        String[] lines = content.split("\n");
        List<String> res = new ArrayList<String>();
        res.add("# +--------------------------------------------------+");
        res.add("# | Project: Spicord                                 |");
        res.add("# | GitHub: https://github.com/OopsieWoopsie/Spicord |");
        res.add("# +--------------------------------------------------+");
        res.add("");
        boolean in = false;
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                in = false;
            }
            if (in) {
                if (!line.startsWith("  ")) {
                    line = "  ".concat(line);
                }
            }
            if (line.startsWith("[")) {
                in = true;
            }
            res.add(line);
        }
        return fixArrayIndentation(res);
    }

    /*
     * Splits the array in multiline to make a good indentation :)
     */
    private String fixArrayIndentation(List<String> lines) {
        for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
            String line = lines.get(lineIndex);
            int i = line.indexOf(" = [ ");

            if (i != -1) {
                char[] c = line.toCharArray();
                int spaceCount = -1;
                while (c[spaceCount+=1] == 32);

                String spaces = repeat(" ", spaceCount);
                String indentation = spaces.concat("  ");

                // format the array
                String values = line.substring(i + 5, line.length() - 2).replace("\", \"", "\",\n" + indentation + "\"");
                line = line.substring(0, i + 4) + "\n" + indentation + values + "\n" + spaces + "]";

                // save it :)
                lines.set(lineIndex, line);
            }
        }
        return String.join("\n", lines);
    }

    private static String repeat(String string, int times) {
        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < times; i++)
            builder.append(string);

        return builder.toString();
    }

    public class ConfigurationManager {

        private final SpicordConfig conf;

        public ConfigurationManager(SpicordConfig conf) {
            this.conf = conf;
        }

        public void addAddonToBot(String addonKey, String botName) {
            for (Bot b : conf.bots) {
                if (b.name.equals(botName)) {
                    b.addons.add(addonKey);
                    save();
                    return;
                }
            }
        }

        public void removeAddonFromBot(String addonKey, String botName) {
            for (Bot b : conf.bots) {
                if (b.name.equals(botName)) {
                    b.addons.remove(addonKey);
                    save();
                    return;
                }
            }
        }
    }

    public static class SpicordConfig {

        private int loadDelay;
        //private int config_version; // not used yet
        private String integrated_addon_footer;

        private Bot[] bots;
        private Messages jda_messages;

        public SpicordConfig() {
            this.jda_messages = new Messages();
        }

        public class Bot {

            private String name;
            private boolean enabled;
            private String token;
            private boolean command_support;
            private String command_prefix;
            private List<String> addons;
        }

        public class Messages {

            private boolean enabled;
            private boolean debug;
        }
    }
}
