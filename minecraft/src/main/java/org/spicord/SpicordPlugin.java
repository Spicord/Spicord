package org.spicord;

import java.io.File;
import org.spicord.LibraryLoader;
import org.spicord.fix.FixClassLoaderPosition;
import org.spicord.plugin.PluginInterface;

public interface SpicordPlugin extends PluginInterface {

    default void checkLoader(boolean isBukkit) {
        if (new File(getDataFolder(), "fixloader.txt").exists()) {
            try {
                if (FixClassLoaderPosition.init(isBukkit)) {
                    getLogger().info("Successfully applied the Loader fix");
                } else {
                    getLogger().warning("Cannot apply the Loader fix");
                }
            } catch (Exception e) {
                getLogger().warning("An error ocurred while applying the Loader fix: " + e.getMessage());
            }
        }
    }

    default void checkForceload() {
        if (new File(getDataFolder(), "forceload.txt").exists()) {
            getLogger().info("Libraries will be forced to load");
            LibraryLoader.setForceLoad(true);
        }
    }

    /*====================================================*/

//    public static void setInstance(SpicordPlugin instance) {
//        Vars.instance = instance;
//    }
//    public static SpicordPlugin getInstance() {
//        return Vars.instance;
//    }
//    static final class Vars {
//        private Vars() {}
//        private static SpicordPlugin instance;
//    }
}
