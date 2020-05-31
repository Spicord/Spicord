package eu.mcdb.spicord.util;

import java.nio.file.Path;

public interface JarClassLoader {

    void loadJar(Path path);
}
