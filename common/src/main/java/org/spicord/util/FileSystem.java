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

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystem {

    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private FileSystem() {}

    public static String readFile(String file) throws IOException {
        return new String(readBytes(file), DEFAULT_CHARSET);
    }

    public static String readFile(File file) throws IOException {
        return new String(readBytes(file.toPath()), DEFAULT_CHARSET);
    }

    public static String readFile(String file, String charset) throws IOException {
        return new String(readBytes(file), Charset.forName(charset));
    }

    public static byte[] readBytes(String file) throws IOException {
        return Files.readAllBytes(Paths.get(file));
    }

    public static byte[] readBytes(Path path) throws IOException {
        return Files.readAllBytes(path);
    }
}
