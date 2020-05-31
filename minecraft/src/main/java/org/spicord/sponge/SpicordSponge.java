package org.spicord.sponge;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.spicord.reflect.ReflectedObject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import com.google.inject.Inject;
import eu.mcdb.spicord.SpicordLoader;
import eu.mcdb.spicord.util.SpicordClassLoader;
import eu.mcdb.universal.plugin.SpongePlugin;
import eu.mcdb.util.SLF4JWrapper;

@Plugin(id = "spicord", name = "Spicord", version = "3.0.0", authors = { "Sheidy" })
public class SpicordSponge extends SpongePlugin {

    private static final List<String> EXCEPTIONS = Arrays.asList(
            "org.mozilla.javascript.",
            "org.yaml.snakeyaml.",
            "com.moandjiezana.toml.",
            "net.dv8tion.jda."
        );

    @Inject
    public SpicordSponge(Logger logger, @ConfigDir(sharedRoot = false) File configDir) {
        ClassLoader cl = prepareClassLoader(Sponge.class.getClassLoader());
        SpicordClassLoader classLoader = new SpicordClassLoader(cl);
        SpicordLoader loader = new SpicordLoader(classLoader, new SLF4JWrapper(logger), configDir);
        loader.load();
    }

    @Listener
    public void init(GameInitializationEvent event) {
        //MCDB.registerCommand(this, new SpicordCommand(() -> {}));
    }

    private ClassLoader prepareClassLoader(ClassLoader classLoader) {
        ClassLoader parent = classLoader.getClass().getClassLoader();

        Set<String> classLoaderExceptions = new ReflectedObject(classLoader)
                .getField("classLoaderExceptions").setAccessible().getValue();

        classLoaderExceptions.addAll(EXCEPTIONS);
        return parent;
    }
}
