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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.logging.Logger;
import eu.mcdb.spicord.util.SpicordClassLoader;
import lombok.Getter;

public class LibraryLoader {

    private final Logger log;
    private SpicordClassLoader classLoader;
    private File libFolder;
    private Library[] libraries;

    public LibraryLoader(String libinfo, Logger log, File dataFolder) {
        this.log = log;
        this.classLoader = SpicordClassLoader.get();
        this.libFolder = new File(dataFolder, "lib");

        if (!dataFolder.exists())
            dataFolder.mkdir();

        if (!libFolder.exists())
            libFolder.mkdir();

        if (!libFolder.isDirectory())
            throw new IllegalStateException("File 'lib' must be a directory.");

        try (final InputStream in = LibraryLoader.class.getResourceAsStream(libinfo);
                final ObjectInputStream ois = new ObjectInputStream(in)) {

            this.libraries = (Library[]) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void downloadLibraries() throws IOException {
        for (final Library lib : libraries) {
            this.downloadLibrary(lib, false);
        }
        this.sha1Check();
    }

    private File downloadLibrary(Library lib, boolean replace) throws IOException {
        File out = new File(libFolder, lib.getFileName());

        if (!out.exists() || replace) {
            log.info("[Loader] Downloading library " + lib.getName());
            byte[] data = lib.download();
            FileOutputStream fos = new FileOutputStream(out);
            fos.write(data);
            fos.flush();
            fos.close();
        }

        return out;
    }

    private void sha1Check() {
        for (final Library lib : libraries) {
            if (lib.getSha1() != null) {
                try {
                    File file = new File(libFolder, lib.getFileName());
                    byte[] b = Files.readAllBytes(file.toPath());
                    MessageDigest digest = MessageDigest.getInstance("SHA-1");

                    digest.update(b);

                    String sha1 = String.format("%040x", new BigInteger(1, digest.digest()));

                    if (!lib.getSha1().equals(sha1)) {
                        log.info("[Loader] sha1sum of library '" + lib.getName() + "' is wrong");
                        log.info("[Loader] expected sha1sum: " + lib.getSha1());
                        log.info("[Loader] current  sha1sum: " + sha1);
                        downloadLibrary(lib, true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Loads the libraries inside the plugin data folder.
     */
    public void loadLibraries() {
        for (Library lib : libraries) {
            if (lib.getDontloadifclassfound() != null) {
                if (classExists(lib.getDontloadifclassfound())) {
                    log.info("[Loader] The library '" + lib.getName() + "' wasn't loaded by Spicord, errors may occur.");
                    continue;
                }
            }

            File file = new File(libFolder, lib.getFileName());
            if (file.isFile() && file.getName().endsWith(".jar")) {

                if (file.exists()) {
                    try {
                        classLoader.loadJar(file.toPath());
                        log.info("[Loader] Loaded library '" + file.getName() + "'.");
                    } catch (Exception e) {
                        log.severe("[Loader] Cannot load library '" + file.getName() + "'. " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    log.severe("[Loader] Library '" + file.getName() + "' was not found on the library path.");
                }
            }
        }
    }

    @Getter
    public class Library implements Serializable {

        private static final long serialVersionUID = 1L;

        private String name;
        private String sha1;
        private String url;
        private String dontloadifclassfound;

        public String getFileName() {
            return url.substring(url.lastIndexOf('/') + 1, url.length());
        }

        public byte[] download() throws IOException {
            final URL url = new URL(this.url);

            try (final InputStream in = url.openStream();
                    final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

                int n;
                while ((n = in.read()) != -1)
                    baos.write(n);

                return baos.toByteArray();
            } catch (IOException e) {
                throw new IOException(e);
            }
        }
    }
}
