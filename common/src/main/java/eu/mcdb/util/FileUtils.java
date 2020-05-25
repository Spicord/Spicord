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

package eu.mcdb.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class FileUtils {

    private final static List<File> files = new ArrayList<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> files.forEach(FileUtils::delete)));
    }

    private static void delete(final File file) {
        if (file.exists()) {
            deleteRecursive(file);
        }
    }

    private static void deleteRecursive(final File dir) {
        if (dir.isDirectory()) {
            for (final File f : dir.listFiles()) {
                deleteRecursive(f);
            }
        }
        dir.delete();
    }

    public static void deleteOnExit(final File file) {
        files.add(file);
    }

    public static File getParent(final File file) {
        return file.toPath().getParent().toFile();
    }
}
