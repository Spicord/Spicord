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

package org.spicord;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.spicord.addon.AddonManager;
import org.spicord.addon.internal.InfoAddon;
import org.spicord.addon.internal.PlayersAddon;
import org.spicord.addon.internal.PluginsAddon;
import org.spicord.api.services.ServiceManager;
import org.spicord.bot.DiscordBot;
import org.spicord.bot.DiscordBotLoader;
import org.spicord.config.SpicordConfiguration;
import org.spicord.event.EventHandler;
import org.spicord.event.SpicordEvent;

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

    private Map<SpicordEvent<?>, Set<EventHandler<?>>> listeners;

    private ScheduledExecutorService threadPool;

    /**
     * The Spicord constructor.
     * 
     * @param logger the logger instance
     * @param threadPool 
     */
    protected Spicord(Logger logger, ScheduledExecutorService threadPool) {
        instance = this;

        logger.setLevel(Level.INFO);

        this.logger = logger;
        this.threadPool = threadPool;

        this.addonManager = new AddonManager(this, logger);
        this.serviceManager = new SpicordServiceManager();
        this.listeners = new HashMap<>();

        for (SpicordEvent<?> e : SpicordEvent.values()) {
            this.listeners.put(e, new HashSet<>());
        }
    }

    public <T> void addEventListener(SpicordEvent<T> eventType, EventHandler<T> eventHandler) {
        listeners.get(eventType).add(eventHandler);
    }

    @SuppressWarnings("unchecked")
    public <T> void callEvent(SpicordEvent<T> eventType, T object) {
        for (EventHandler<?> listener : listeners.get(eventType)) {
            ((EventHandler<T>) listener).handleSafe(object);
        }
    }

    protected void onLoad(SpicordConfiguration config) throws IOException {
        if (!isLoaded())
            return;

        if (config.isDebugEnabled()) {
            logger.setLevel(Level.FINER);
        }

        if (config.isJdaMessagesEnabled() && false) {
            try {
                Class<?> cls = Class.forName("org.spicord.log.LoggerFactory");
                Method init = cls.getMethod("init", Logger.class);
                init.invoke(null, logger);
            } catch (Exception e) {
                logger.warning("Failed to enable JDA messages: " + e.getMessage());
            }
        }

        this.config = config;

        File addonsDir = new File(config.getDataFolder(), "addons");
        this.addonManager.loadAddons(addonsDir);
        this.registerIntegratedAddons();

        Server.getInstance().setDebugEnabled(config.isDebugEnabled());

        callEvent(SpicordEvent.SPICORD_LOADED, this);

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

        threadPool.shutdownNow();

        this.threadPool = null;
        this.addonManager = null;
        this.serviceManager = null;
        this.serverType = null;
        this.logger = null;
        this.config = null;
        instance = null;
    }

    public ScheduledExecutorService getThreadPool() {
        return threadPool;
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
