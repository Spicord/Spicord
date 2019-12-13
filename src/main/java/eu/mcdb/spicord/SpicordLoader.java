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

package eu.mcdb.spicord;

import static eu.mcdb.util.ReflectionUtils.classExists;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import com.google.common.base.Preconditions;
import eu.mcdb.spicord.config.SpicordConfiguration;
import eu.mcdb.spicord.util.SpicordClassLoader;
import lombok.Getter;

public final class SpicordLoader {

    private static boolean firstRun = true;

    /**
     * The {@link Spicord} instance.
     */
    private Spicord spicord;

    /**
     * The {@link SpicordClassLoader} instance.
     */
    @Getter
    private SpicordClassLoader classLoader;

    private File dataFolder;

    private LibraryLoader libraryLoader;

    private Logger logger;

    /**
     * The {@link SpicordLoader} constructor.
     * 
     * @param logger      the {@link Spicord} instance
     * @param classLoader the plugin class loader
     * @param dataFolder 
     * @param serverType  the server type
     */
    public SpicordLoader(Logger logger, File dataFolder) {
        Preconditions.checkNotNull(logger);

        this.logger = logger;
        this.classLoader = SpicordClassLoader.get();
        this.libraryLoader = new LibraryLoader("/libraries.libinfo", logger, dataFolder);
        this.spicord = new Spicord(logger);
        this.dataFolder = dataFolder;
    }

    /**
     * Loads Spicord
     */
    public void load() {
        try {
            if (firstRun) {
                firstRun = false;
                libraryLoader.downloadLibraries();
                libraryLoader.loadLibraries();

                if (!classExists("net.dv8tion.jda.core.JDA")) {
                    logger.severe("[Loader] JDA library is not loaded, this plugin will not work.");
                    this.disable();
                    return;
                }
            }

            final SpicordConfiguration config = new SpicordConfiguration(dataFolder);

            spicord.onLoad(config);
        } catch (IOException e) {
            logger.severe(
                    "Spicord could not be loaded, please report this error in \n\t -> https://github.com/OopsieWoopsie/Spicord/issues");
            logger.severe("Error: " + e.getMessage());
            e.printStackTrace();
            disable();
        }
    }

    /**
     * Turns off Spicord.
     */
    public void disable() {
        this.spicord.onDisable();
        this.spicord = null;
    }
}
