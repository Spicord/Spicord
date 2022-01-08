package org.spicord.fix;

import java.io.File;

import org.spicord.LibraryLoader;
import org.spicord.SpicordPlugin;

public class Fixes {
    public static void checkLoader(SpicordPlugin plugin, boolean isBukkit) {
        if (new File(plugin.getDataFolder(), "fixloader.txt").exists()) {
            try {
                if (FixClassLoaderPosition.init(isBukkit)) {
                    plugin.getLogger().info("Successfully applied the Loader fix");
                } else {
                    plugin.getLogger().warning("Cannot apply the Loader fix");
                }
            } catch (Exception e) {
                plugin.getLogger().warning("An error ocurred while applying the Loader fix: " + e.getMessage());
            }
        }
    }

    public static void checkForceload(SpicordPlugin plugin) {
        if (new File(plugin.getDataFolder(), "forceload.txt").exists()) {
            plugin.getLogger().info("Libraries will be forced to load");
            LibraryLoader.setForceLoad(true);
        }
    }
}
