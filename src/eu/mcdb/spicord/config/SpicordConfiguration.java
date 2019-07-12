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

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import eu.mcdb.spicord.Spicord;
import eu.mcdb.spicord.bot.DiscordBot;
import eu.mcdb.spicord.bukkit.SpicordBukkit;
import eu.mcdb.spicord.bungee.SpicordBungee;
import eu.mcdb.util.ServerType;
import lombok.Getter;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class SpicordConfiguration {

    /** The list of the loaded bots. */
    @Getter
    private final Set<DiscordBot> bots;

    /** The server type. */
    @Getter
    private ServerType serverType;

    /** Debug mode flag. */
    @Getter
    private boolean debugEnabled;

    /** JDA messages flag. */
    @Getter
    private boolean jdaMessagesEnabled;

    /** The data folder of the plugin. */
    @Getter
    private File dataFolder;

    /** The plugin jar file. */
    @Getter
    private File file;

    public SpicordConfiguration(ServerType serverType) {
        this.bots = Collections.synchronizedSet(new HashSet<DiscordBot>());
        this.serverType = serverType;

        switch (serverType) {
        case BUKKIT:
            loadBukkit();
            break;
        case BUNGEECORD:
            loadBungee();
            break;
        }

        long disabledCount = bots.stream().filter(DiscordBot::isDisabled).count();
        Spicord.getInstance().getLogger().info("Loaded " + bots.size() + " bots, " + disabledCount + " disabled.");
    }

    private void loadBungee() {
        SpicordBungee plugin = SpicordBungee.getInstance();

        try {
            this.file = plugin.getFile();

            createConfigIfNotExists(plugin.getDataFolder());

            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class)
                    .load(new File(plugin.getDataFolder(), "config.yml"));
            ((Configuration) config.get("bots")).getKeys().forEach(botName -> {
                Configuration botData = (Configuration) config.get("bots." + botName);
                bots.add(new DiscordBot(botName, botData.getString("token"), botData.getBoolean("enabled", false),
                        botData.getStringList("addons"), botData.getBoolean("command-support", false),
                        botData.getString("command-prefix")));
            });

            this.debugEnabled = config.getBoolean("enable-debug-messages", true);
            this.jdaMessagesEnabled = config.getBoolean("enable-jda-messages", true);
        } catch (Exception e) {
            plugin.getLogger().severe(
                    "This is a configuration error, NOT a plugin error, please generate a new config or fix it. "
                            + e.getMessage());
        }
    }

    private void loadBukkit() {
        SpicordBukkit plugin = SpicordBukkit.getInstance();

        try {
            this.file = plugin.getFile();

            createConfigIfNotExists(plugin.getDataFolder());

            FileConfiguration config = plugin.getConfig();

            ((MemorySection) config.get("bots")).getKeys(false).forEach(botName -> {
                MemorySection botData = (MemorySection) config.get("bots." + botName);
                bots.add(new DiscordBot(botName, botData.getString("token"), botData.getBoolean("enabled", false),
                        botData.getStringList("addons"), botData.getBoolean("command-support", false),
                        botData.getString("command-prefix")));
            });

            this.debugEnabled = config.getBoolean("enable-debug-messages", true);
            this.jdaMessagesEnabled = config.getBoolean("enable-jda-messages", false);
        } catch (Exception e) {
            plugin.getLogger().severe(
                    "This is a configuration error, NOT a plugin error, please generate a new config or fix it. "
                            + e.getMessage());
        }
    }

    private void createConfigIfNotExists(File dataFolder) {
        if (!dataFolder.exists())
            dataFolder.mkdir();

        this.dataFolder = dataFolder;
        File configFile = new File(dataFolder, "config.yml");

        if (!configFile.exists()) {
            try (InputStream in = getClass().getResourceAsStream("/config.yml")) {
                Files.copy(in, configFile.toPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
