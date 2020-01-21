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

package eu.mcdb.spicord.util;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

public class SpicordClassLoader {

    private final static URLClassLoader classLoader;
    private final static Method addURL;

    static {
        try {
            (addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class)).setAccessible(true);
             classLoader = (URLClassLoader) SpicordClassLoader.class.getClassLoader();
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the classes of a Jar file
     * 
     * @param file the {@link Path} of the Jar file
     */
    public static void loadJar(Path file) throws Exception {
        addURL.invoke(classLoader, file.toUri().toURL());
    }
}
