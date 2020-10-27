package org.spicord.plugin;

import java.io.File;
import java.util.logging.Logger;

public interface PluginInterface {

    File getFile();
    File getDataFolder();
    Logger getLogger();

}
