/*
 * Copyright (C) 2019  OopsieWoopsie
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.spicord;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import com.google.common.base.Preconditions;
import eu.mcdb.spicord.config.SpicordConfiguration;
import eu.mcdb.spicord.util.JarClassLoader;

public final class SpicordLoader implements AutoCloseable {

    private static boolean firstRun = true;

    private Spicord spicord;

    private final File dataFolder;
    private final LibraryLoader libraryLoader;
    private final Logger logger;

    /**
     * The {@link SpicordLoader} constructor.
     */
    public SpicordLoader(Logger logger, File dataFolder) {
        this(null, logger, dataFolder);
    }

    public SpicordLoader(JarClassLoader classLoader, Logger logger, File dataFolder) {
        Preconditions.checkNotNull(logger);

        this.logger = logger;
        this.libraryLoader = new LibraryLoader(classLoader, "/libraries.libinfo", logger, dataFolder);
        this.dataFolder = dataFolder;

        this.load0();
    }

    private void load0() {
        try {
            if (firstRun) {
                firstRun = false;
                libraryLoader.downloadLibraries();
                libraryLoader.loadLibraries();
            }

            this.spicord = new Spicord(logger);
        } catch (IOException e) {
            handleException(e);
        }
    }

    public void load() {
        try {
            final SpicordConfiguration config = new SpicordConfiguration(dataFolder);
            spicord.onLoad(config);
        } catch (IOException e) {
            handleException(e);
        }
    }

    @Override
    public void close() {
        if (spicord != null)
            spicord.onDisable();
        spicord = null;
    }

    /**
     * Turns off Spicord.
     */
    public void disable() {
        close();
    }

    private void handleException(Exception e) {
        logger.severe("Spicord could not be loaded, please report this error in \n\t -> https://github.com/OopsieWoopsie/Spicord/issues");
        e.printStackTrace();
        disable();
    }
}
