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

package eu.mcdb.spicord.config;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import eu.mcdb.spicord.Spicord;
import eu.mcdb.spicord.bot.DiscordBot;
import eu.mcdb.spicord.config.SpicordConfiguration.SpicordConfig.Bot;
import eu.mcdb.spicord.util.ArrayUtils;
import lombok.Data;
import lombok.Getter;

public final class SpicordConfiguration {

    @Getter
    private final Set<DiscordBot> bots;

    @Getter
    private boolean debugEnabled;

    @Getter
    private boolean jdaMessagesEnabled;

    private final File dataFolder;
    private final File configFile;
    private final TomlWriter writer;

    private SpicordConfig config;

    @Getter
    private final ConfigurationManager manager;

    public SpicordConfiguration(final File dataFolder) {
        new OldSpicordConfiguration(dataFolder); // migrate from old config

        this.bots = Collections.synchronizedSet(new HashSet<DiscordBot>());

        this.dataFolder = dataFolder;
        this.dataFolder.mkdir();
        this.configFile = new File(dataFolder, "config.toml");

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
            DiscordBot bot = new DiscordBot(botData.name, botData.token, botData.enabled,
                    Arrays.asList(botData.addons), botData.command_support,
                    botData.command_prefix);

            bots.add(bot);
        }

        this.jdaMessagesEnabled = config.jda_messages.enabled;
        this.debugEnabled = config.jda_messages.debug;

        long disabledCount = bots.stream().filter(DiscordBot::isDisabled).count();
        Spicord.getInstance().getLogger().info("Loaded " + bots.size() + " bots, " + disabledCount + " disabled.");
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
                Files.copy(in, configFile.toPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * This adds the comment at the top of the file and
     * fixes the toml indentation bug for some values
     */
    static String fixIndentation(String content) {
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
    private static String fixArrayIndentation(List<String> lines) {
        for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
            String line = lines.get(lineIndex);
            int i = line.indexOf(" = [ ");

            if (i != -1) {
                char[] c = line.toCharArray();
                int spaceCount = -1;
                while (c[spaceCount+=1] == 32);

                String spaces = StringUtils.repeat(" ", spaceCount);
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

    public class ConfigurationManager {

        private final SpicordConfig conf;

        public ConfigurationManager(SpicordConfig conf) {
            this.conf = conf;
        }

        public void addAddonToBot(String addonKey, String botName) {
            for (Bot b : conf.bots) {
                if (b.name.equals(botName)) {
                    b.addons = ArrayUtils.push(b.addons, addonKey);
                    save();
                    return;
                }
            }
        }

        public void removeAddonFromBot(String addonKey, String botName) {
            for (Bot b : conf.bots) {
                if (b.name.equals(botName)) {
                    b.addons = ArrayUtils.remove(b.addons, addonKey);
                    save();
                    return;
                }
            }
        }
    }

    // exposed to "package" because of OldSpicordConfiguration class
    @Data
    static class SpicordConfig {

        private Bot[] bots;
        private Messages jda_messages;
        private int config_version;

        SpicordConfig() {
            this.jda_messages = new Messages();
        }

        @Data
        static class Bot {
            private String name;
            private boolean enabled;
            private String token;
            private boolean command_support;
            private String command_prefix;
            private String[] addons;
        }

        @Data
        static class Messages {
            private boolean enabled;
            private boolean debug;
        }
    }
}
