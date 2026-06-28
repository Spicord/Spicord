package org.spicord.script;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Path;
import java.util.Map;
import org.spicord.reflect.ReflectUtils;

public class ScriptInfo {

    private final String directory;
    private final String fileName;
    private final boolean exists;
    private final boolean isFile;
    private Reader reader;
    private ScriptEnvironment env;

    public ScriptInfo(File f) {
        this(f, new ScriptEnvironment());
    }

    public ScriptInfo(File f, ScriptEnvironment env) {
        this.env = env;
        Path p = f.toPath().normalize();
        this.directory = p.getParent().toString();
        this.fileName = p.getFileName().toString();
        this.exists = f.exists();
        this.isFile = f.isFile();

        if (exists) {
            this.reader = ReflectUtils.consumeException(() -> new FileReader(f));
        }
    }

    public String getDirectory() {
        return directory;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean exists() {
        return exists;
    }

    public boolean isFile() {
        return isFile;
    }

    public Reader getReader() {
        return reader;
    }

    public boolean hasEnvironment() {
        return getEnvironment().size() > 0;
    }

    public Map<String, Object> getEnvironment() {
        return env.getEnv();
    }
}
