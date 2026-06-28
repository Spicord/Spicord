package org.spicord.script;

import java.util.HashMap;
import java.util.Map;

public class ScriptEnvironment {

    private final Map<String, Object> env = new HashMap<>();

    public ScriptEnvironment addEnv(String name, Object val) {
        env.put(name, val);
        return this;
    }

    public Map<String, Object> getEnv() {
        return env;
    }
}
