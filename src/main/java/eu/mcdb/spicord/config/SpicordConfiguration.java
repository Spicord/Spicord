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
import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import eu.mcdb.spicord.Spicord;
import eu.mcdb.spicord.bot.DiscordBot;
import lombok.Data;
import lombok.Getter;

public class SpicordConfiguration {

    @Getter
    private final Set<DiscordBot> bots;

    @Getter
    private boolean debugEnabled;

    @Getter
    private boolean jdaMessagesEnabled;

    private final File dataFolder;
    private final File configFile;
    private final TomlWriter writer;

    private InternalConfig config;

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
    }

    public void load() {
        this.saveDefault();

        final Toml toml = new Toml().read(configFile);
        this.config = toml.to(InternalConfig.class);

        for (final InternalConfig.Bot botData : config.bots) {
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
            String str = fix(new String(baos.toByteArray()));
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
    static String fix(String str) {
        String[] lines = str.split("\n");
        List<String> res = new ArrayList<>();
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
                    line = "  " + line;
                }
            }
            if (line.startsWith("[")) {
                in = true;
            }
            res.add(line);
        }
        return String.join("\n", res);
    }

    @Data
    static class InternalConfig {

        private Bot[] bots;
        private Messages jda_messages;
        private int config_version;

        InternalConfig() {
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
