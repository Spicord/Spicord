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

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import org.spicord.reflect.ReflectedMethod;
import org.spicord.reflect.ReflectedObject;

public class SpicordClassLoader implements JarClassLoader {

    private final ReflectedMethod addURL;

    public SpicordClassLoader() {
        this(SpicordClassLoader.class.getClassLoader());
    }

    public SpicordClassLoader(ClassLoader loader) {
        addURL = new ReflectedObject(URLClassLoader.class, loader)
                .getMethod("addURL", URL.class).setAccessible();
    }

    /**
     * Load the classes of a Jar file.
     * 
     * @param file the {@link Path} of the Jar file
     */
    public void loadJar(Path file) {
        if (addURL == null)
            throw new IllegalStateException("SpicordClassLoader not initialized");
        try {
            addURL.invoke(file.toUri().toURL());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
