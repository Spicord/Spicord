package org.spicord.sponge;

import java.io.File;
import org.slf4j.Logger;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.plugin.Plugin;
import com.google.inject.Inject;
import eu.mcdb.spicord.SpicordCommand;
import eu.mcdb.spicord.SpicordLoader;
import eu.mcdb.universal.MCDB;
import eu.mcdb.universal.plugin.SpongePlugin;
import eu.mcdb.util.SLF4JWrapper;

@Plugin(id = "spicord", name = "Spicord", version = "3.0.0", authors = { "Sheidy" })
public class SpicordSponge extends SpongePlugin {

    private SpicordLoader loader;

    @Inject
    public SpicordSponge(Logger logger, @ConfigDir(sharedRoot = false) File configDir) {
        this.loader = new SpicordLoader(new SLF4JWrapper(logger), configDir);
        this.loader.load();
        MCDB.registerCommand(this, new SpicordCommand(() -> {}));
    }
}
