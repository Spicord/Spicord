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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import eu.mcdb.spicord.config.SpicordConfiguration;
import eu.mcdb.spicord.util.SpicordClassLoader;
import lombok.Getter;
import lombok.Setter;

public class SpicordLoader {

	/**
	 * The server type.
	 */
	@Getter
	@Setter
	private ServerType serverType;

	/**
	 * The {@link Spicord} instance.
	 */
	private Spicord spicord;

	/**
	 * The lib folder inside the plugin data folder.
	 */
	private File libFolder;

	/**
	 * Function that will be executed when the plugin is being disabled.
	 */
	private Consumer<Void> disable;

	/**
	 * The {@link SpicordClassLoader} instance.
	 */
	@Getter
	private SpicordClassLoader classLoader;

    private Libraries libraries;

	/**
	 * The {@link SpicordLoader} constructor.
	 * @param spicord the {@link Spicord} instance
	 * @param classLoader the plugin class loader
	 */
	public SpicordLoader(Spicord spicord, ClassLoader classLoader) {
		Preconditions.checkNotNull(spicord);
		Preconditions.checkNotNull(classLoader);

		try {
			this.classLoader = new SpicordClassLoader((URLClassLoader) classLoader);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		this.spicord = spicord;
	}

	/**
	 * Loads the {@link Spicord} instance triggering the {@link Spicord#onLoad(SpicordLoader)} method.
	 */
	public void load() {
		try {
			spicord.onLoad(this);
		} catch (IOException e) {
			spicord.getLogger().severe("Spicord could not be loaded, please report this error in https://github.com/OopsieWoopsie/Spicord/issues");
			disable();
			e.printStackTrace();
		}
	}

	/**
	 * Turns off {@link Spicord} triggering the {@link Spicord#onDisable()} method and other stuff.
	 */
	public void disable() {
		this.disable.accept(null);
		this.spicord.onDisable();
		this.spicord = null;
		this.serverType = null;
		this.libFolder = null;
	}

	/**
	 * Extract the internal libraries to the plugin data folder.
	 * @param config the {@link SpicordConfiguration} instance.
	 */
	public void extractLibraries(SpicordConfiguration config) throws IOException {
		Preconditions.checkNotNull(config);

        // ------
        InputStream in = getClass().getResourceAsStream("/lib/libraries.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        reader.close();
        this.libraries = new Gson().fromJson(stringBuilder.toString(), Libraries.class);
        // ------

		JarFile jarFile = new JarFile(config.getFile());
		Enumeration<JarEntry> entries = jarFile.entries();

		this.libFolder = new File(config.getDataFolder(), "lib");
		if (!libFolder.exists()) libFolder.mkdir();

		Preconditions.checkArgument(libFolder.isDirectory(), "File 'lib' must be a directory");

		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			if (entry.getName().startsWith("lib/") && entry.getName().endsWith(".jar")) {
				String name = entry.getName();
				name = name.substring(name.lastIndexOf("/") + 1);
				Files.copy(jarFile.getInputStream(entry), new File(libFolder, name).toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
		}

		jarFile.close();
	}

	/**
	 * Loads the libraries inside the plugin data folder.
	 */
	public void loadLibraries() {
		Preconditions.checkNotNull(this.libFolder);
		Preconditions.checkArgument(this.libFolder.isDirectory());

        for (String libName : libraries.getLibraries()) {
            File file = new File(libFolder, libName);
            if (file.isFile() && file.getName().endsWith(".jar")) {
                try {
                    getClassLoader().loadJar(file.toPath());
                    spicord.debug("[Loader] Loaded library '" + file.getName() + "'.");
                } catch (Exception e) {
                    spicord.getLogger().severe("[Loader] Cannot load library '" + file.getName() + "'. " + e.getMessage());
                }
            }
        }
		try {
			Class.forName("net.dv8tion.jda.core.JDA");
		} catch (ClassNotFoundException e) {
			spicord.getLogger().severe("[Loader] JDA library is not loaded, this plugin will not work.");
			this.disable(); // TODO: Don't use this method
		}
	}

	public enum ServerType {
		BUKKIT, BUNGEECORD
	}

	public void setDisableAction(Consumer<Void> object) {
		this.disable = object;
	}
	
	private class Libraries {

	    @Getter
	    private String[] libraries;
	}
}