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

package eu.mcdb.spicord.util;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.lang.reflect.Method;

public class SpicordClassLoader {

	/**
	 * The plugin class loader.
	 */
	private final URLClassLoader classLoader;

	/**
	 * The method used to load the classes.
	 */
	private final Method addURL;

	/**
	 * The SpicordClassLoader constructor.
	 * @param classLoader the plugin class loader
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public SpicordClassLoader(URLClassLoader classLoader) throws NoSuchMethodException, SecurityException {
		this.addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
		addURL.setAccessible(true);
		this.classLoader = classLoader;
	}

	/**
	 * Loads the classes of a Jar file
	 * @param file the {@link Path} of the Jar file
	 */
	public void loadJar(Path file) {
		try {
			addURL.invoke(this.classLoader, file.toUri().toURL());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}