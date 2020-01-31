/*
 * Copyright (C) 2020  OopsieWoopsie
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

package org.spicord.cli;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.spicord.cli.log.FormattedLogger;
import eu.mcdb.spicord.LibraryLoader;
import eu.mcdb.spicord.SpicordLoader;

public class SpicordMain {

    private final Logger logger = FormattedLogger.getLogger("Spicord");
    private final File dataFolder = this.getLocation();
    private final SpicordLoader loader;

    public static void main(String[] args) throws IOException {
        new SpicordMain(args);
    }

    public SpicordMain(String[] args) throws IOException {
        this.preload();
        this.loader = new SpicordLoader(this.logger, this.dataFolder);
        this.loader.load();

        final LineReader lineReader = LineReaderBuilder.builder().build();
        final SpicordConsoleCommand scc = new SpicordConsoleCommand(this.logger);
        final String prompt = "spicord > ";

        do {
            try {
                final String input = lineReader.readLine(prompt);
                scc.execute(input);
            } catch (Exception e) {
                this.loader.disable();
                System.out.println("Bye!");
                System.exit(0);
                break;
            }
        } while (true);
    }

    private void preload() throws IOException {
        LibraryLoader libraryLoader = new LibraryLoader("/preload.libinfo", this.logger, this.dataFolder);
        libraryLoader.downloadLibraries();
        libraryLoader.loadLibraries();
    }

    private File getLocation() {
        String str = this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
        str = str.substring(0, str.lastIndexOf(File.separator));
        return new File(str);
    }
}
