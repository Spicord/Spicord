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

package org.spicord.script.module;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import org.spicord.script.ScriptEngine;

public class Native {

    private final ScriptEngine engine;
    private final Loader loader;

    public Native(ScriptEngine engine) {
        this.engine = engine;
        this.loader = new Loader();
    }

    public void load(String path) {
        for (URL url : getURLS(new File(path))) {
            loader.addURL(url);
        }
    }

    public Object get(String name) {
        return getClass(name);
    }

    public Object getClass(String name) {
        return engine.wrap(getJavaClass(name));
    }

    public Class<?> getJavaClass(String name) {
        return loader.getClass(name);
    }

    private static URL[] getURLS(File fileOrDirectory) {
        if (fileOrDirectory.exists()) {
            if (fileOrDirectory.isDirectory()) {
                return getURLS(fileOrDirectory.listFiles());
            } else {
                return getURLS(new File[] { fileOrDirectory });
            }
        }
        return new URL[0];
    }

    private static URL[] getURLS(File[] files) {
        List<URL> urls = new ArrayList<>();

        for (File f : files) {
            if (f.getName().endsWith(".jar")) {
                try {
                    URL url = f.toURI().toURL();
                    urls.add(url);
                } catch (MalformedURLException e) {} // won't happen... maybe???
            }
        }

        return urls.toArray(new URL[urls.size()]);
    }

    class Loader extends URLClassLoader {

        public Loader() {
            super(new URL[0], null);
        }

        @Override
        public void addURL(URL url) {
            super.addURL(url);
        }

        public Class<?> getClass(String name) {
            try {
                return super.loadClass(name, true);
            } catch (ClassNotFoundException e) {
                System.err.println("Unable to find class " + name);
                return null;
            }
        }
    }
}
