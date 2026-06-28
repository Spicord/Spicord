package org.spicord.script.module;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

// https://nodejs.org/api/fs.html
public class FileSystem {

    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private FileSystem() {}

    // 'encoding' is ignored for now, that will be done in the future :)
    public static String readFileSync(String path, String encoding) throws IOException {
        return new String(readBytes(path), DEFAULT_CHARSET);
    }

    private static byte[] readBytes(String file) throws IOException {
        return Files.readAllBytes(Paths.get(file));
    }
}
