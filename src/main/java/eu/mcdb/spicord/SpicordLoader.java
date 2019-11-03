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

import static eu.mcdb.spicord.util.ReflectionUtils.classExists;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.logging.Logger;
import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
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
     * The lib folder inside the plugin data folder.
     */
    private File libFolder;

    /**
     * The {@link SpicordClassLoader} instance.
     */
    @Getter
    private SpicordClassLoader classLoader;

    private Libraries.Library[] libraries;

    private File dataFolder;

    /**
     * The {@link SpicordLoader} constructor.
     * 
     * @param logger      the {@link Spicord} instance
     * @param classLoader the plugin class loader
     * @param dataFolder 
     * @param serverType  the server type
     */
    public SpicordLoader(Logger logger, ClassLoader classLoader, File dataFolder) {
        Preconditions.checkNotNull(logger);
        Preconditions.checkNotNull(classLoader);

        this.classLoader = new SpicordClassLoader((URLClassLoader) classLoader);
        this.spicord = new Spicord(logger);
        this.dataFolder = dataFolder;
    }

    /**
     * Loads Spicord
     */
    public void load() {
        try {
            SpicordConfiguration config = new SpicordConfiguration(dataFolder);

            if (firstRun) {
                firstRun = false;
                this.downloadLibraries(config);
                this.sha1Check();
                this.loadLibraries();
            }

            spicord.onLoad(config);
        } catch (IOException e) {
            spicord.getLogger().severe(
                    "Spicord could not be loaded, please report this error in \n\t -> https://github.com/OopsieWoopsie/Spicord/issues");
            spicord.getLogger().severe("Error: " + e.getMessage());
            e.printStackTrace();
            disable();
        }
    }

    private void sha1Check() {
        for (Libraries.Library lib : libraries) {
            if (lib.getSha1() != null) {
                try {
                    File file = new File(libFolder, lib.getFileName());
                    byte[] b = Files.readAllBytes(file.toPath());
                    MessageDigest digest = MessageDigest.getInstance("SHA-1");

                    digest.update(b);

                    String sha1 = String.format("%040x", new BigInteger(1, digest.digest()));

                    if (!lib.getSha1().equals(sha1)) {
                        spicord.getLogger().info("[Loader] sha1sum of library '" + lib.getName() + "' is wrong");
                        spicord.getLogger().info("[Loader] expected sha1sum: " + lib.getSha1());
                        spicord.getLogger().info("[Loader] current  sha1sum: " + sha1);
                        downloadLibrary(lib, true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Turns off Spicord.
     */
    public void disable() {
        this.spicord.onDisable();
        this.spicord = null;
        this.libFolder = null;
    }

    /**
     * Extract the internal libraries to the plugin data folder.
     * 
     * @param config the {@link SpicordConfiguration} instance.
     */
    public void downloadLibraries(SpicordConfiguration config) throws IOException {
        Preconditions.checkNotNull(config);

        InputStream in = getClass().getResourceAsStream("/libraries.json");
        String json = new String(ByteStreams.toByteArray(in), Charset.defaultCharset());
        this.libraries = new Gson().fromJson(json, Libraries.class).getLibraries();

        this.libFolder = new File(dataFolder, "lib");

        if (!libFolder.exists())
            libFolder.mkdir();

        Preconditions.checkArgument(libFolder.isDirectory(), "File 'lib' must be a directory.");

        for (Libraries.Library lib : libraries) {
            downloadLibrary(lib, false);
        }
    }

    private File downloadLibrary(Libraries.Library lib, boolean replace) throws IOException {
        File file = new File(libFolder, lib.getFileName());

        if (!file.exists() || replace) {
            spicord.getLogger().info("[Loader] Downloading library " + lib.getName());
            byte[] data = lib.download();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
            fos.close();
        }

        return file;
    }

    /**
     * Loads the libraries inside the plugin data folder.
     */
    public void loadLibraries() {
        Preconditions.checkNotNull(this.libFolder, "libFolder");
        Preconditions.checkArgument(this.libFolder.isDirectory(), "libFolder not directory");

        if (classExists("net.dv8tion.jda.core.JDA")) {
            spicord.getLogger().warning("JDA was previously loaded, some options may not work.");
            return;
        }

        for (Libraries.Library lib : libraries) {
            if (lib.getDontloadifclassfound() != null) {
                if (classExists(lib.getDontloadifclassfound())) {
                    spicord.getLogger().info("[Loader] Another plugin already loaded '" + lib.getName() + "', skipping...");
                    continue;
                }
            }

            File file = new File(libFolder, lib.getFileName());
            if (file.isFile() && file.getName().endsWith(".jar")) {

                if (file.exists()) {
                    try {
                        getClassLoader().loadJar(file.toPath());
                        spicord.getLogger().info("[Loader] Loaded library '" + file.getName() + "'.");
                    } catch (Exception e) {
                        spicord.getLogger().severe("[Loader] Cannot load library '" + file.getName() + "'. " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    spicord.getLogger().severe("[Loader] Library '" + file.getName() + "' was not found on the library path.");
                }
            }
        }

        if (!classExists("net.dv8tion.jda.core.JDA")) {
            spicord.getLogger().severe("[Loader] JDA library is not loaded, this plugin will not work.");
            this.disable();
        }
    }

    private class Libraries {

        @Getter
        private Library[] libraries;

        @Getter
        private class Library {

            private String name;
            private String sha1;
            private String url;
            private String dontloadifclassfound;

            public String getFileName() {
                return url.substring(url.lastIndexOf('/') + 1, url.length());
            }

            public byte[] download() throws IOException {
                URL url = new URL(this.url);

                try (InputStream in = url.openStream()) {
                    return ByteStreams.toByteArray(in);
                } catch (IOException e) {
                    throw new IOException(e);
                }
            }
        }
    }
}
