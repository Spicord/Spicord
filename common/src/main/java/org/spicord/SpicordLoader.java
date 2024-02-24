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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

import org.spicord.config.SpicordConfiguration;
import org.spicord.event.EventHandler;
import org.spicord.event.SpicordEvent;
import org.spicord.util.JarClassLoader;
import org.spicord.util.sched.SpicordSchedulerV1;

import com.google.common.base.Preconditions;

public final class SpicordLoader {

    private static boolean firstRun = true;

    private Spicord spicord;
    private SpicordConfiguration config;

    private final LibraryLoader libraryLoader;
    private final Logger logger;

    private SpicordPlugin plugin;

    private ScheduledExecutorService threadPool;

    /**
     * The {@link SpicordLoader} constructor.
     */
    public SpicordLoader(SpicordPlugin plugin) {
        this(null, plugin);
    }

    public SpicordLoader(JarClassLoader classLoader, SpicordPlugin plugin) {
        Preconditions.checkNotNull(plugin);

        final int availableProcessors = Runtime.getRuntime().availableProcessors();

        final boolean isJava17 = System.getProperty("java.version").startsWith("17.");

        final boolean useTestScheduler = isJava17 && availableProcessors < 2;

        if (useTestScheduler) {

            plugin.getLogger().info("Using test scheduler");

            this.threadPool = new SpicordSchedulerV1();
        } else

        if (availableProcessors > 1) {
            int poolSize = availableProcessors * 2;

            this.threadPool = Executors.newScheduledThreadPool(poolSize);
        } else {
            this.threadPool = Executors.newScheduledThreadPool(2);
        }

        this.plugin = plugin;

        final Logger logger = plugin.getLogger();
        final File dataFolder = plugin.getDataFolder();

        this.logger = logger;
        this.libraryLoader = new LibraryLoader(classLoader, "/libraries.libinfo", logger, dataFolder);

        try {
            if (firstRun) {
                firstRun = false;
                libraryLoader.downloadLibraries();
                libraryLoader.loadLibraries();
            }

            this.spicord = new Spicord(logger, threadPool, plugin);
            this.config  = new SpicordConfiguration(spicord, dataFolder);
        } catch (IOException e) {
            handleException(e);
        }
    }

    public void load() {
        new SpicordCommand(plugin).register(plugin);

        try {
            for (EventHandler<Spicord> listener : startupListeners) {
                this.spicord.addEventListener(SpicordEvent.SPICORD_LOADED, listener);
            }
            spicord.onLoad(config);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public Spicord getSpicord() {
        return spicord;
    }

    public ScheduledExecutorService getThreadPool() {
        return threadPool;
    }

    public void shutdown() {
        if (spicord != null)
            spicord.onDisable();
        spicord = null;
        self = null;
    }

    private void handleException(Exception e) {
        logger.severe("Spicord could not be loaded, please report this error in \n\t -> https://github.com/Spicord/Spicord/issues");
        e.printStackTrace();
        shutdown();
    }

    public SpicordConfiguration getConfig() {
        return config;
    }

    // -------------------

    private static final List<EventHandler<Spicord>> startupListeners = new ArrayList<>();
    private static SpicordLoader self; { self = this; }

    /**
     * Add a new startup listener.
     * 
     * @param listener the listener instance
     */
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
