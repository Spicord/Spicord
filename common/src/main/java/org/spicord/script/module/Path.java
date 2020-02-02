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
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.spicord.script.ScriptEngine;

/*
 * https://nodejs.org/api/path.html
 */
public class Path {

    public static final String sep = File.separator;
    public static final String delimiter = File.pathSeparator;
    private static final String EMPTY_STRING = "";

    private final ScriptEngine engine;

    public Path(ScriptEngine engine) {
        this.engine = engine;
    }

    public static String basename(final String path) {
        if (path == null) return null;

        final int i;
        if ((i = path.lastIndexOf(sep)) != -1)
            return path.substring(i + 1);

        return path;
    }

    public static String basename(String path, final String ext) {
        if (path == null) return null;

        path = basename(path);
        if (path.endsWith(ext))
            return path.substring(0, path.length() - ext.length());

        return path;
    }

    public static String dirname(final String path) {
        if (path == null) return null;

        final int i = path.lastIndexOf(sep);
        if (i != -1)
            return path.substring(0, i);

        return path;
    }

    public static String extname(final String path) {
        if (path == null) return null;

        final int i = path.lastIndexOf('.');
        if (i < 1) return EMPTY_STRING;
        return path.substring(i);
    }

    public String format(Object obj) {
        final PathObject path;
        if (obj instanceof PathObject)
            path = (PathObject) obj;
        else
            path = engine.java(PathObject.class, obj);

        final String dir = path.dir == null ? path.root : path.dir;
        final String base = path.base == null ? (path.name + path.ext) : path.base;

        if (dir == null || base == null) {
            return EMPTY_STRING;
        }

        return join(dir, base);
    }

    public static boolean isAbsolute(String path) {
        if (path == null) return false;

        if (path.length() > 0) {
            if (path.startsWith(sep))
                return true;
            else if (path.length() > 1 && path.charAt(1) == ':')
                return true;
        }

        return false;
    }

    public static String join(String... paths) {
        if (paths == null) return null;
        if (paths.length == 0) return EMPTY_STRING;

        for (int i = 0; i < paths.length; i++)
            if (paths[i].length() == 0) paths[i] = ".";

        return normalize(String.join(sep, paths));
    }

    public static String normalize(String path) {
        return Paths.get(path).normalize().toString();
    }

    public static PathObject parse(String path) {
        return null; // TODO
    }

//    private String removeTrailingSeparators(String path) {
//        return path.replaceAll("(\\/+)$", EMPTY_STRING);
//    }

    public static String resolve(String... paths) {
        if (paths == null) return null;
        if (paths.length == 0) return EMPTY_STRING;

        final List<String> list = new LinkedList<>();

        for (int i = paths.length - 1; i >= 0; i--) {
            final String path = paths[i];
            list.add(path);
            if (path.startsWith("/")) break;
        }

        final String last = list.get(list.size() - 1);

        if (!last.startsWith("/")) {
            list.add(".");
        }

        Collections.reverse(list);

        return join(list.toArray(new String[list.size()]));
    }

    public class PathObject {
        public String root;
        public String dir;
        public String base;
        public String ext;
        public String name;
    }
}
