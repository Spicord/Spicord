package org.spicord.bukkit;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.bukkit.plugin.java.JavaPlugin;
import org.spicord.Spicord;
import org.spicord.SpicordLoader;
import org.spicord.SpicordPlugin;
import org.spicord.bukkit.server.BukkitServer;
import org.spicord.fix.Fixes;

import eu.mcdb.universal.Server;
import eu.mcdb.universal.ServerType;

public class SpicordBukkit extends JavaPlugin implements SpicordPlugin {

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

        Server.setInstance(new BukkitServer(getServer(), this), ServerType.BUKKIT);

        this.loader = new SpicordLoader(this);
    }

    @Override
    public void onEnable() {
        final int loadDelay = loader.getConfig().getLoadDelay();

        getLogger().info("Spicord will load in " + loadDelay + " seconds");

        loader.getThreadPool().schedule(() -> {
            BukkitJDADetector.checkOtherJDA(this);
            loader.load();
        }, loadDelay, TimeUnit.SECONDS);

        Fixes.checkLoader(this, true);
    }

    @Override
    public File getFile() {
        return super.getFile();
    }

    @Override
    public void onDisable() {
        if (this.loader != null) {
            this.loader.shutdown();
        }
        this.loader = null;
    }
}
