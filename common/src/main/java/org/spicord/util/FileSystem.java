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
