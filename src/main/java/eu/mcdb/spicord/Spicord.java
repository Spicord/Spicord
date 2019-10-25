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

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.logging.Logger;
import eu.mcdb.spicord.addon.AddonManager;
import eu.mcdb.spicord.addon.InfoAddon;
import eu.mcdb.spicord.addon.PlayersAddon;
import eu.mcdb.spicord.addon.PluginsAddon;
import eu.mcdb.spicord.bot.DiscordBot;
import eu.mcdb.spicord.bot.DiscordBotLoader;
import eu.mcdb.spicord.config.SpicordConfiguration;
import eu.mcdb.spicord.util.ReflectionUtils;
import eu.mcdb.util.Server;
import eu.mcdb.util.ServerType;
import lombok.Getter;
import net.dv8tion.jda.core.utils.JDALogger;

public class Spicord {

    /**
     * The {@link Spicord} instance.
     */
    private static Spicord instance;

    /**
     * The {@link Logger} instance.
     */
    @Getter
    private Logger logger;

    /**
     * The server type.
     */
    @Getter
    private ServerType serverType;

    /**
     * The Spicord configuration.
     */
    @Getter
    private SpicordConfiguration config;

    /**
     * The addon manager.
     */
    @Getter
    private AddonManager addonManager;

    /**
     * The Spicord constructor.
     * 
     * @param logger the logger instance
     */
    public Spicord(Logger logger) {
        instance = this;
        this.logger = logger;
        this.addonManager = new AddonManager(this);
    }

    protected void onLoad(SpicordConfiguration config) throws IOException {
        if (!isLoaded())
            return;

        this.config = config;

        this.registerIntegratedAddons();

        Server.getInstance().setDebugEnabled(config.isDebugEnabled());

        setupLogger();

        getLogger().info("Starting the bots...");
        config.getBots().forEach(DiscordBotLoader::startBot);
    }

    private void setupLogger() {
        if (ReflectionUtils.classExists("eu.mcdb.logger.ProvisionalLogger")) {
            try {
                Class<?> loggerClass = Class.forName("eu.mcdb.logger.ProvisionalLogger");
                Constructor<?> constructor = loggerClass.getConstructor(boolean.class, boolean.class);
                Object loggerInst = constructor.newInstance(config.isDebugEnabled(), config.isJdaMessagesEnabled());
                Method setLogMethod = JDALogger.class.getDeclaredMethod("setLog", loggerClass.getInterfaces()[0]);
                setLogMethod.invoke(null, loggerInst);

                if (config.isJdaMessagesEnabled()) {
                    debug("Successfully enabled JDA messages.");
                } else {
                    debug("Successfully disabled JDA messages.");
                }
            } catch (Exception e) {
                getLogger().warning("An error ocurred while setting the logger: " + e.getCause());
                e.printStackTrace();
            }
        }
    }

    private void registerIntegratedAddons() {
        this.getAddonManager().registerAddon(new InfoAddon());
        this.getAddonManager().registerAddon(new PluginsAddon());
        this.getAddonManager().registerAddon(new PlayersAddon());
    }

    protected void onDisable() {
        getLogger().info("Disabling Spicord...");
        config.getBots().forEach(DiscordBotLoader::shutdownBot);
        config.getBots().clear();
        addonManager.getAddons().clear();
        this.addonManager = null;
        this.serverType = null;
        this.logger = null;
        this.config = null;
        instance = null;
    }

    /**
     * Get a bot by its name.
     * 
     * @param name the bot name.
     * @return the {@link DiscordBot} object if the bot exists, or null if not.
     */
    public DiscordBot getBotByName(String name) {
        for (DiscordBot bot : config.getBots())
            if (bot.getName().equals(name))
                return bot;

        return null;
    }

    /**
     * Gets the Spicord instance.
     * 
     * @throws IllegalStateException if Spicord has not loaded.
     * @return the Spicord instance.
     * @see {@link #isLoaded()}
     */
    public static Spicord getInstance() {
        if (!isLoaded())
            throw new IllegalStateException("Spicord has not loaded yet.");

        return instance;
    }

    public static String getVersion() {
        return Spicord.class.getPackage().getImplementationVersion();
    }

    /**
     * Check if Spicord was loaded.
     * 
     * @return true if Spicord is loaded, or false if not.
     */
    public static boolean isLoaded() {
        return instance != null;
    }

    /**
     * Displays a message if the debug mode if enabled.
     * 
     * @param msg the message to be displayed.
     */
    public void debug(String msg) {
        if (config.isDebugEnabled())
            getLogger().info("[DEBUG] " + msg);
    }
}
