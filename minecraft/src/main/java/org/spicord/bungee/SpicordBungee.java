package org.spicord.bungee;

import java.util.concurrent.TimeUnit;

import org.spicord.Spicord;
import org.spicord.SpicordLoader;
import org.spicord.SpicordPlugin;
import org.spicord.bungee.server.BungeeServer;
import org.spicord.fix.Fixes;

import eu.mcdb.universal.Server;
import eu.mcdb.universal.ServerType;
import net.md_5.bungee.api.plugin.Plugin;

public class SpicordBungee extends Plugin implements SpicordPlugin {

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

    @Override
    public void onLoad() {
        Fixes.checkForceload(this);

        Server.setInstance(new BungeeServer(getProxy(), this), ServerType.BUNGEECORD);

        this.loader = new SpicordLoader(this);
    }

    @Override
    public void onEnable() {
        final int loadDelay = loader.getConfig().getLoadDelay();

        getLogger().info("Spicord will load in " + loadDelay + " seconds");

        loader.getThreadPool().schedule(() -> {
            BungeeJDADetector.checkOtherJDA(this);
            loader.load();
        }, loadDelay, TimeUnit.SECONDS);

        Fixes.checkLoader(this, false);
    }

    @Override
    public void onDisable() {
        if (this.loader != null) {
            this.loader.shutdown();
        }
        this.loader = null;
    }
}
