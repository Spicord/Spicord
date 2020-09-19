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

import static org.spicord.reflect.ReflectUtils.findClass;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.logging.Logger;
import eu.mcdb.spicord.util.JarClassLoader;
import eu.mcdb.spicord.util.SpicordClassLoader;
import lombok.Setter;

public class LibraryLoader {

    private final Logger logger;
    private File libFolder;
    private Library[] libraries;
    private JarClassLoader loader;

    @Setter private static boolean forceLoad;

    public LibraryLoader(JarClassLoader loader, String libinfo, Logger logger, File dataFolder) {
        this.loader = loader;
        this.logger = logger;
        this.libFolder = new File(dataFolder, "lib");

        if (!libFolder.exists())
            libFolder.mkdirs();

        if (!libFolder.isDirectory())
            throw new IllegalStateException("File 'lib' must be a directory.");

        try (final InputStream in = LibraryLoader.class.getResourceAsStream(libinfo);
                final ObjectInputStream ois = new ObjectInputStream(in)) {

            this.libraries = (Library[]) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (loader == null) {
            this.loader = new SpicordClassLoader();
        }
    }

    public void downloadLibraries() throws IOException {
        for (final Library lib : libraries) {
            this.downloadLibrary(lib, false);
        }
        this.sha1Check(false);
    }

    private File downloadLibrary(Library lib, boolean replace) throws IOException {
        File out = new File(libFolder, lib.getFileName());

        if (!out.exists() || replace) {
            logger.info("[Loader] Downloading library " + lib.getName());
            byte[] data = lib.download();
            FileOutputStream fos = new FileOutputStream(out);
            fos.write(data);
            fos.flush();
            fos.close();
        }

        return out;
    }

    private void sha1Check(boolean failed) {
        boolean recheck = false;
        for (final Library lib : libraries) {
            if (lib.getSha1() != null) {
                try {
                    File file = new File(libFolder, lib.getFileName());
                    byte[] b = Files.readAllBytes(file.toPath());
                    MessageDigest digest = MessageDigest.getInstance("SHA-1");

                    digest.update(b);

                    String sha1 = String.format("%040x", new BigInteger(1, digest.digest()));

                    if (!lib.getSha1().equals(sha1)) {
                        logger.info("[Loader] sha1sum of library '" + lib.getName() + "' is wrong");
                        logger.info("[Loader] expected sha1sum: " + lib.getSha1());
                        logger.info("[Loader] current  sha1sum: " + sha1);
                        downloadLibrary(lib, true);
                        recheck = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (recheck && failed) {
            throw new RuntimeException("Failed second attemp to check library integrity");
        } else if (recheck) sha1Check(true);
    }

    /**
     * Loads the libraries inside the plugin data folder.
     */
    public void loadLibraries() {
        for (Library lib : libraries) {
            boolean loaded = false;
            if (lib.getDontloadifclassfound() != null) {
                if (findClass(lib.getDontloadifclassfound()).isPresent()) {
                    if (!forceLoad) {
                        logger.info("[Loader] The library '" + lib.getName() + "' wasn't loaded by Spicord, errors may occur.");
                        continue;
                    }
                    loaded = true;
                }
            }

            File file = new File(libFolder, lib.getFileName());
            if (file.isFile() && file.getName().endsWith(".jar")) {
                if (file.exists()) {
                    try {
                        loader.loadJar(file.toPath());
                        logger.info("[Loader] Loaded library '" + file.getName() + "'" + (loaded ? " (Forced)" : ""));
                    } catch (Exception e) {
                        logger.severe("[Loader] Cannot load library '" + file.getName() + "'. " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    logger.severe("[Loader] Library '" + file.getName() + "' was not found on the library path.");
                }
            }
        }
    }
}
