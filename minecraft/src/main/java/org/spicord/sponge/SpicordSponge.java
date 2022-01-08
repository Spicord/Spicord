package org.spicord.sponge;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.spicord.Spicord;
import org.spicord.SpicordLoader;
import org.spicord.SpicordPlugin;
import org.spicord.Version;
import org.spicord.plugin.SpongePlugin;
import org.spicord.reflect.ReflectUtils;
import org.spicord.reflect.ReflectedObject;
import org.spicord.util.SpicordClassLoader;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import com.google.inject.Inject;

@Plugin(id = "spicord", name = "Spicord", version = Version.VERSION, authors = { "Sheidy" })
public class SpicordSponge extends SpongePlugin implements SpicordPlugin {

    private static final List<String> EXCEPTIONS = Arrays.asList(
            "org.mozilla.javascript.",
            "org.yaml.snakeyaml.",
            "com.moandjiezana.toml.",
            "net.dv8tion.jda."
        );

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
    public SpicordSponge(Logger logger, @ConfigDir(sharedRoot = false) File configDir) {
        ClassLoader cl = prepareClassLoader(Sponge.class.getClassLoader());
        SpicordClassLoader classLoader = new SpicordClassLoader(cl);

        this.loader = new SpicordLoader(classLoader, this);
    }

    @Listener
    public void init(GameInitializationEvent event) {
        if (this.loader != null) {
            this.loader.load();
        }
    }

    @Listener
    public void stop(GameStoppingEvent event) {
        if (this.loader != null) {
            this.loader.shutdown();
        }
    }

    private ClassLoader prepareClassLoader(ClassLoader classLoader) {
        ClassLoader parent = classLoader.getClass().getClassLoader();

        Set<String> classLoaderExceptions = new ReflectedObject(classLoader)
                .getField("classLoaderExceptions").setAccessible().getValue();

        classLoaderExceptions.addAll(EXCEPTIONS);
        return parent;
    }

    @Override
    public File getFile() {
        return ReflectUtils.getJarFile(SpicordSponge.class);
    }
}
