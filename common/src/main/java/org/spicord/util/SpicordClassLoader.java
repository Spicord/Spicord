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

package org.spicord.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

public class SpicordClassLoader implements JarClassLoader {

    private final MethodHandle addURL;

    public SpicordClassLoader() {
        this(SpicordClassLoader.class.getClassLoader());
    }

    public SpicordClassLoader(ClassLoader loader) {
        try {
            addURL = getAddUrlMethod(loader);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Load the classes of a Jar file.
     * 
     * @param file the {@link Path} of the Jar file
     */
    public void loadJar(Path file) {
        try {
            addURL.invoke(file.toUri().toURL());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public MethodHandle getAddUrlMethod(ClassLoader loader) throws ReflectiveOperationException {
        boolean methodNotFound = false;
        Lookup lookup = MethodHandles.lookup();

        try {
            Method m = MethodHandles.class.getMethod("privateLookupIn", Class.class, Lookup.class);
            lookup = (Lookup) m.invoke(null, loader.getClass(), lookup);
        } catch (ReflectiveOperationException e) {
            methodNotFound = true;
        }

        Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);

        if (methodNotFound) {
            addURL.setAccessible(true);
        }

        return lookup.unreflect(addURL).bindTo(loader);
    }
}
