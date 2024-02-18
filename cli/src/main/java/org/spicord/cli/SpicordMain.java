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
import org.spicord.LibraryLoader;
import org.spicord.Spicord;
import org.spicord.SpicordLoader;
import org.spicord.SpicordPlugin;
import org.spicord.cli.log.FormattedLogger;
import org.spicord.reflect.ReflectUtils;

import eu.mcdb.universal.Server;

public class SpicordMain implements SpicordPlugin {

    public static void main(String[] args) throws IOException { new SpicordMain(args); }

    private final Logger logger = FormattedLogger.getLogger("Spicord");
    private final File dataFolder = this.getLocation();
    private SpicordLoader loader;

    @Override
    public void reloadSpicord() {
        if (this.loader != null) {
            this.loader.shutdown();
        }
        this.loader = new SpicordLoader(this);
        this.loader.load();
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public File getDataFolder() {
        return dataFolder;
    }

    @Override
    public File getFile() {
        return ReflectUtils.getJarFile(SpicordMain.class);
    }

    @Override
    public Spicord getSpicord() {
        return loader.getSpicord();
    }

    public SpicordMain(String[] args) throws IOException {
        Server.setInstance(new DummyServer());

        this.preload();
        this.reloadSpicord();

        final LineReader lineReader = LineReaderBuilder.builder().build();
        final SpicordConsoleCommand scc = new SpicordConsoleCommand(this);
        final String prompt = "spicord > ";

        do {
            try {
                final String input = lineReader.readLine(prompt);
                scc.execute(input);
            } catch (Exception e) {
                this.loader.shutdown();
                System.out.println("Bye!");
                System.exit(0);
                break;
            }
        } while (true);
    }

    private void preload() throws IOException {
        LibraryLoader libraryLoader = new LibraryLoader(null, "/preload.libinfo", this.logger, this.dataFolder);
        libraryLoader.downloadLibraries();
        libraryLoader.loadLibraries();
    }

    private File getLocation() {
        String str = this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
        str = str.substring(0, str.lastIndexOf(File.separator));
        return new File(str);
    }
}
