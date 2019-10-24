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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import com.moandjiezana.toml.TomlWriter;
import eu.mcdb.spicord.config.SpicordConfiguration.InternalConfig;
import eu.mcdb.util.Server;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

// pending removal
class OldSpicordConfiguration {

    private final File configFile;
    private InternalConfig config;

    public OldSpicordConfiguration(final File dataFolder) {
        dataFolder.mkdir();
        this.configFile = new File(dataFolder, "config.yml");
        this.config = new InternalConfig();

        final File saveTo = new File(dataFolder, "config.toml");

        if (configFile.exists() && !saveTo.exists()) {
            System.out.println("Detected old configuration file, migrating...");

            switch (Server.getServerType()) {
            case BUNGEECORD:
                new loadBungee();
                break;
            case BUKKIT:
                new loadBukkit();
                break;
            }

            if (config == null) {
                System.out.println("[ERROR] An error ocurred while mirating to the new configuration");
                System.out.println("[ERROR] A clean configuration file will be created (config.toml)");
                configFile.delete();
                return;
            }

            final TomlWriter writer = new TomlWriter.Builder()
                    .indentValuesBy(2)
                    .padArrayDelimitersBy(1)
                    .build();

            try (final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    final FileOutputStream fos = new FileOutputStream(saveTo)) {
                writer.write(config, baos);
                String str = SpicordConfiguration.fix(new String(baos.toByteArray()));
                fos.write(str.getBytes(Charset.forName("UTF-8")));
                fos.flush();
                configFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Using classes because the jvm class loader
    // throws java.lang.NoClassDefFoundError
    // when using methods

    private class loadBungee {
        loadBungee() {
            try {
                Configuration cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);

                List<InternalConfig.Bot> bots = new ArrayList<>();

                ((Configuration) cfg.get("bots")).getKeys().forEach(botName -> {
                    Configuration botData = (Configuration) cfg.get("bots." + botName);

                    InternalConfig.Bot bot = new InternalConfig.Bot();
                    bot.setName(botName);
                    bot.setToken(botData.getString("token", ""));
                    bot.setEnabled(botData.getBoolean("enabled", false));
                    bot.setAddons(botData.getStringList("addons").toArray(new String[0]));
                    bot.setCommand_support(botData.getBoolean("command-support", false));
                    bot.setCommand_prefix(botData.getString("command-prefix", "-"));
                    bots.add(bot);
                });

                config.setBots(bots.toArray(new InternalConfig.Bot[0]));
                config.getJda_messages().setDebug(cfg.getBoolean("enable-debug-messages", true));
                config.getJda_messages().setEnabled(cfg.getBoolean("enable-jda-messages", false));
            } catch (Exception e) {
                config = null;
            }
        }
    }

    private class loadBukkit {
        loadBukkit() {
            try {
                FileConfiguration cfg = new org.bukkit.configuration.file.YamlConfiguration();
                cfg.load(configFile);

                List<InternalConfig.Bot> bots = new ArrayList<>();

                ((MemorySection) cfg.get("bots")).getKeys(false).forEach(botName -> {
                    MemorySection botData = (MemorySection) cfg.get("bots." + botName);
                    InternalConfig.Bot bot = new InternalConfig.Bot();
                    bot.setName(botName);
                    bot.setToken(botData.getString("token", ""));
                    bot.setEnabled(botData.getBoolean("enabled", false));
                    bot.setAddons(botData.getStringList("addons").toArray(new String[0]));
                    bot.setCommand_support(botData.getBoolean("command-support", false));
                    bot.setCommand_prefix(botData.getString("command-prefix", "-"));
                    bots.add(bot);
                });

                config.setBots(bots.toArray(new InternalConfig.Bot[0]));
                config.getJda_messages().setDebug(cfg.getBoolean("enable-debug-messages", true));
                config.getJda_messages().setEnabled(cfg.getBoolean("enable-jda-messages", false));
            } catch (Exception e) {
                config = null;
            }
        }
    }
}
