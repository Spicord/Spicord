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

package eu.mcdb.spicord;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;
import org.spicord.api.services.ServiceManager;
import eu.mcdb.spicord.addon.AddonManager;
import eu.mcdb.spicord.addon.InfoAddon;
import eu.mcdb.spicord.addon.PlayersAddon;
import eu.mcdb.spicord.addon.PluginsAddon;
import eu.mcdb.spicord.bot.DiscordBot;
import eu.mcdb.spicord.bot.DiscordBotLoader;
import eu.mcdb.spicord.config.SpicordConfiguration;
import eu.mcdb.universal.Server;
import eu.mcdb.universal.ServerType;
import lombok.Getter;

public final class Spicord {

    private static Spicord instance;

    @Getter private Logger logger;
    @Getter private ServerType serverType;
    @Getter private SpicordConfiguration config;
    @Getter private ServiceManager serviceManager;
    @Getter private AddonManager addonManager;

    private List<Consumer<Spicord>> loadListeners;

    /**
     * The Spicord constructor.
     * 
     * @param logger the logger instance
     */
    public Spicord(Logger logger) {
        instance = this;
        this.logger = logger;
        this.addonManager = new AddonManager();
        this.serviceManager = new SpicordServiceManager();
        this.loadListeners = new ArrayList<>();
    }

    public void onLoad(Consumer<Spicord> action) {
        loadListeners.add(action);

        if (config != null)
            action.accept(this);
    }

    protected void onLoad(SpicordConfiguration config) throws IOException {
        if (!isLoaded())
            return;

        this.config = config;

        File addonsDir = new File(config.getDataFolder(), "addons");
        this.addonManager.loadAddons(addonsDir);
        this.registerIntegratedAddons();

        Server.getInstance().setDebugEnabled(config.isDebugEnabled());

        loadListeners.forEach(l -> l.accept(this));
        loadListeners.clear();

        getLogger().info("Starting the bots...");
        config.getBots().forEach(DiscordBotLoader::startBot);
    }

    private void registerIntegratedAddons() {
        this.getAddonManager().registerAddon(new InfoAddon());
        this.getAddonManager().registerAddon(new PluginsAddon());
        this.getAddonManager().registerAddon(new PlayersAddon());
    }

    protected void onDisable() {
        logger.info("Disabling Spicord...");

        if (config != null) {
            config.getBots().forEach(DiscordBotLoader::shutdownBot);
            config.getBots().clear();
        }

        this.addonManager = null;
        this.serviceManager = null;
        this.serverType = null;
        this.logger = null;
        this.config = null;
        instance = null;
    }

    /**
     * Get a bot instance by its name.
     * 
     * @param name the bot name
     * @return the bot instance, or null if the bot was not found
     */
    public DiscordBot getBotByName(String name) {
        for (DiscordBot bot : config.getBots())
            if (bot.getName().equals(name))
                return bot;

        return null;
    }

    /**
     * Print a message in the console if the debug mode is enabled.
     * 
     * @param message the message to print
     */
    public void debug(String message) {
        if (config.isDebugEnabled())
            logger.info("[DEBUG] " + message);
    }

    /**
     * Get the Spicord instance.
     * 
     * @see {@link #isLoaded()}
     * @return the Spicord instance, may be null
     */
    public static Spicord getInstance() {
        return instance;
    }

    /**
     * Get the Spicord version.
     */
    public static String getVersion() {
        return Spicord.class.getPackage().getImplementationVersion();
    }

    /**
     * Check if Spicord is loaded.
     * 
     * @return true if Spicord is loaded, or false if not
     */
    public static boolean isLoaded() {
        return instance != null;
    }
}
