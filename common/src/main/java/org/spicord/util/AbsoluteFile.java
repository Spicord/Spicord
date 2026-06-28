package org.spicord.util;

import java.io.File;

public final class AbsoluteFile {

    private AbsoluteFile() {}

    public static File of(String pathname) {
        return new File(pathname).getAbsoluteFile();
    }

    public static File of(String parent, String child) {
        return new File(parent, child).getAbsoluteFile();
    }
}
