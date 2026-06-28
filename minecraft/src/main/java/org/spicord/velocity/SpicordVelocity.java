package org.spicord.velocity;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.spicord.Spicord;
import org.spicord.SpicordLoader;
import org.spicord.SpicordPlugin;
import org.spicord.plugin.VelocityPlugin;
import org.spicord.reflect.ReflectUtils;
import org.spicord.velocity.server.VelocityServer;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;

import eu.mcdb.universal.Server;
import eu.mcdb.universal.ServerType;

public class SpicordVelocity extends VelocityPlugin implements SpicordPlugin {

    private SpicordLoader loader;

    @Override
    public void reloadSpicord() {
        if (this.loader != null) {
            this.loader.shutdown();
        }
        this.loader = new SpicordLoader(this);
        this.loader.load();
    }

    @Override
    public Spicord getSpicord() {
        return this.loader.getSpicord();
    }

    @Inject
    public SpicordVelocity(ProxyServer server, PluginContainer plugin) {
        super(server, plugin);

        Server.setInstance(new VelocityServer(server, this), ServerType.VELOCITY);

        if (this.loader != null) {
            this.loader.shutdown();
        }
        this.loader = new SpicordLoader(this);
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        final int loadDelay = loader.getConfig().getLoadDelay();

        getLogger().info("Spicord will load in " + loadDelay + " seconds");

        loader.getThreadPool().schedule(() -> loader.load(), loadDelay, TimeUnit.SECONDS);
    }

    @Override
    public File getFile() {
        return ReflectUtils.getJarFile(SpicordVelocity.class);
    }
}
