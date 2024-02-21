package org.spicord.sponge;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.spicord.Spicord;
import org.spicord.SpicordLoader;
import org.spicord.SpicordPlugin;
import org.spicord.reflect.ReflectUtils;
import org.spicord.sponge.server.SpongeServer;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.LoadedGameEvent;
import org.spongepowered.api.event.lifecycle.StoppedGameEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import com.google.inject.Inject;

import eu.mcdb.universal.Server;

@Plugin("spicord")
public class SpicordSponge implements SpicordPlugin {

    private SpicordLoader loader;

    private File dataFolder;

    @Inject
    public SpicordSponge(
        @ConfigDir(sharedRoot = false) Path dataFolder,
        Game game,
        PluginContainer container
    ) {
        Server.setInstance(new SpongeServer(game, container));

        this.dataFolder = dataFolder.toFile();
        this.loader = new SpicordLoader(new MostInefficientClassLoader(), this);
    }

    @Listener
    public void onLoadedGame(LoadedGameEvent event) {
        if (this.loader != null) {
            loadSpicord();
        }
    }

    private void loadSpicord() {
        final int loadDelay = loader.getConfig().getLoadDelay();

        getLogger().info("Spicord will load in " + loadDelay + " seconds");

        loader.getThreadPool().schedule(() -> loader.load(), loadDelay, TimeUnit.SECONDS);
    }

    @Listener
    public void onStoppedGame(StoppedGameEvent event) {
        if (this.loader != null) {
            this.loader.shutdown();
        }
    }

    @Override
    public File getFile() {
        return ReflectUtils.getJarFile(SpicordSponge.class);
    }

    @Override
    public Logger getLogger() {
        return Logger.getLogger("spicord");
    }

    @Override
    public File getDataFolder() {
        return dataFolder;
    }

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
}
