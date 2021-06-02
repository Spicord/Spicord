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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.spicord.config.SpicordConfiguration;
import org.spicord.event.EventHandler;
import org.spicord.event.SpicordEvent;
import org.spicord.util.JarClassLoader;
import com.google.common.base.Preconditions;

public final class SpicordLoader {

    private static boolean firstRun = true;

    private Spicord spicord;
    private SpicordConfiguration config;

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

        try {
            if (firstRun) {
                firstRun = false;
                libraryLoader.downloadLibraries();
                libraryLoader.loadLibraries();
            }

            this.spicord = new Spicord(logger);
            this.config  = new SpicordConfiguration(logger, dataFolder);

            for (EventHandler<Spicord> listener : startupListeners) {
                this.spicord.addEventListener(SpicordEvent.SPICORD_LOADED, listener);
            }

        } catch (IOException e) {
            handleException(e);
        }
    }

    public void load() {
        try {
            spicord.onLoad(config);
        } catch (IOException e) {
            handleException(e);
        }
    }

    public void shutdown() {
        if (spicord != null)
            spicord.onDisable();
        spicord = null;
        self = null;
    }

    private void handleException(Exception e) {
        logger.severe("Spicord could not be loaded, please report this error in \n\t -> https://github.com/OopsieWoopsie/Spicord/issues");
        e.printStackTrace();
        shutdown();
    }

    public SpicordConfiguration getConfig() {
        return config;
    }

    // -------------------

    private static final List<EventHandler<Spicord>> startupListeners = new ArrayList<>();
    private static SpicordLoader self; { self = this; }

    public static void addStartupListener(EventHandler<Spicord> listener) {
        if (self == null) {
            throw new IllegalStateException("Called too early");
        }

        if (
            self.spicord != null &&
            self.spicord.getConfig() != null
        ) {
            listener.handle(self.spicord);
        }

        startupListeners.add(listener);
    }
}
