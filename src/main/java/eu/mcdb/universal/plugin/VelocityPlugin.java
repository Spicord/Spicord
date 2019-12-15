package eu.mcdb.universal.plugin;

import java.io.File;
import java.util.logging.Logger;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.PluginManager;
import com.velocitypowered.api.proxy.ProxyServer;
import eu.mcdb.spicord.velocity.SLF4JWrapper;
import eu.mcdb.util.Server;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

@Getter
public abstract class VelocityPlugin {

    private final static File pluginsDir = new File("plugins");

    private final ProxyServer proxyServer;
    private final CommandManager commandManager;
    private final EventManager eventManager;
    private final PluginManager pluginManager;

    private PluginContainer container;
    private File dataFolder;
    private Logger logger;

    @Getter(value = AccessLevel.NONE)
    private Plugin plugin;

    public VelocityPlugin(@NonNull ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
        this.commandManager = proxyServer.getCommandManager();
        this.eventManager = proxyServer.getEventManager();
        this.pluginManager = proxyServer.getPluginManager();

        Server.setVelocityHandle(proxyServer);

        this.check();
        this.onLoad();
        this.onEnable(); // TODO
    }

    public void onLoad() {}
    public void onEnable() {}

    private void check() {
        final Class<?> clazz = getExtendingClass();

        if (clazz.isAnnotationPresent(Plugin.class)) {
            this.plugin = clazz.getAnnotation(Plugin.class);

            final String name = plugin.name().isEmpty() ? plugin.id() : plugin.name();

            this.dataFolder = new File(pluginsDir, name);
            this.logger = new SLF4JWrapper(name);

            return;
        }

        throw new IllegalStateException(); // TODO: message
    }

    private Class<?> getExtendingClass() {
        try {
            throw new Exception();
        } catch (Exception e) {
            try {
                // index 3 (+1) because is the amount of calls since this class was extended
                final String name = e.getStackTrace()[3].getClassName();
                return Class.forName(name);
            } catch (Exception e1) {
                return null;
            }
        }
    }

    public final org.slf4j.Logger getSLF4JLogger() {
        return ((SLF4JWrapper) logger).getSLF4JLogger();
    }
}
