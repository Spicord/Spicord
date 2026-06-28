package org.spicord.plugin;

import java.io.File;
import java.util.logging.Logger;

import org.spicord.reflect.ReflectUtils;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.plugin.PluginManager;
import com.velocitypowered.api.proxy.ProxyServer;

import eu.mcdb.util.SLF4JWrapper;
import lombok.Getter;
import lombok.NonNull;

public abstract class VelocityPlugin {

    private final static File pluginsDir = new File("plugins");

    @Getter private static ProxyServer proxyServer;
    @Getter private static CommandManager commandManager;
    @Getter private static EventManager eventManager;
    @Getter private static PluginManager pluginManager;

    @Getter private File dataFolder;
    @Getter private Logger logger;

    public VelocityPlugin() {
        check(getPlugin());
    }

    public VelocityPlugin(@NonNull ProxyServer server) {
        proxyServer    = server;
        commandManager = server.getCommandManager();
        eventManager   = server.getEventManager();
        pluginManager  = server.getPluginManager();

        check(getPlugin());
    }

    public VelocityPlugin(@NonNull ProxyServer server, PluginContainer plugin) {
        proxyServer    = server;
        commandManager = server.getCommandManager();
        eventManager   = server.getEventManager();
        pluginManager  = server.getPluginManager();

        check(plugin);
    }

    public void onLoad() {}
    public void onEnable() {}

    private void check(Plugin plugin) {
        this.dataFolder = new File(pluginsDir, plugin.id());
        this.logger = new SLF4JWrapper(plugin.name().isEmpty() ? plugin.id() : plugin.name());

        this.onLoad();
        this.onEnable(); // TODO
    }

    private void check(PluginContainer plugin) {
        PluginDescription desc = plugin.getDescription();

        this.dataFolder = new File(pluginsDir, desc.getId());
        this.logger = new SLF4JWrapper(desc.getName().orElse(desc.getId()));

        this.onLoad();
        this.onEnable(); // TODO
    }

    private Plugin getPlugin() {
        final Class<?> clazz = getClass();

        if (clazz.isAnnotationPresent(Plugin.class)) {
            return clazz.getAnnotation(Plugin.class);
        }

        throw new IllegalStateException(String.format("missing annotation %s for class %s", Plugin.class.getName(), clazz.getName()));
    }

    public final org.slf4j.Logger getSLF4JLogger() {
        return ((SLF4JWrapper) logger).getSLF4JLogger();
    }

    public File getFile() {
        return ReflectUtils.getJarFile(getClass());
    }
}
