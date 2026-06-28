package org.spicord;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
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
import org.spicord.util.JDALoggerFactory;

import eu.mcdb.universal.Server;
import eu.mcdb.universal.ServerType;
import lombok.Getter;

public final class Spicord {

    private static Spicord instance;

    @Getter private SpicordPlugin plugin;

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
     * @param threadPool the thread pool
     * @param plugin the plugin instance
     */
    protected Spicord(Logger logger, ScheduledExecutorService threadPool, SpicordPlugin plugin) {
        instance = this;

        logger.setLevel(Level.INFO);

        this.logger = logger;
        this.threadPool = threadPool;
        this.plugin = plugin;

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

        if (config.isJdaMessagesEnabled()) {
            try {
                Class<?> cls = Class.forName(
                    "net.dv8tion.jda.internal.utils.JDALogger",
                    true,
                    Spicord.class.getClassLoader()
                );

                // Enable Fallback Logger
                cls.getMethod("setFallbackLoggerEnabled", boolean.class).invoke(null, true);

                // Replace LOGS Map
                Field logsField = cls.getDeclaredField("LOGS");
                setStaticFinal(logsField, new JDALoggerFactory(this));

            } catch (Exception e) {
                logger.warning("Failed to enable JDA messages: " + e.getMessage());
                if (config.isDebugEnabled()) {
                    e.printStackTrace();
                }
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

    @SuppressWarnings("all")
    private void setStaticFinal(Field field, Object value) throws Exception {
        final Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);

        Object fieldBase = unsafe.staticFieldBase(field);
        long fieldOffset = unsafe.staticFieldOffset(field);

        unsafe.putObject(fieldBase, fieldOffset, value);
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
     * @param args the message arguments
     */
    public void debug(String message, Object... args) {
        if (config.isDebugEnabled())
            logger.info(String.format("[DEBUG] " + message, args));
    }

    /**
     * Get the Spicord instance.
     * 
     * @see #isLoaded()
     * @return the Spicord instance, may be null
     */
    public static Spicord getInstance() {
        return instance;
    }

    /**
     * Get the Spicord version.
     * 
     * @return the Spicord version
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
