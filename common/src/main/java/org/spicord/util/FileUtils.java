package org.spicord.util;

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

        if (!dir.delete()) System.err.println("Unable to delete " + dir.toString());
    }

    public static void deleteOnExit(final File file) {
        files.add(file);
    }

    public static File getParent(final File file) {
        return (file.isAbsolute() ? file : file.getAbsoluteFile()).getParentFile();
    }
}
