package org.spicord.script;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class ScriptEngine implements IScriptEngine, AutoCloseable {

    private final static Map<String, Supplier<ScriptEngine>> initEngines;
    private final static Map<String, ScriptEngine> engines;

    static {
        initEngines = new HashMap<>();
        initEngines.put("rhino", () -> new RhinoScriptEngine());

        engines = new HashMap<>();
    }

    /**
     * Get a new ScriptEngine instance for the requested engine.
     * 
     * @param name the engine name
     * @return the new ScriptEngine instance
     * @throws IllegalArgumentException if the engine was not found
     * @throws RuntimeException if the engine throws an error while creating a new instance
     */
    public static ScriptEngine getEngine(String name) {
        if (engines.containsKey(name)) {
            return engines.get(name);
        } else if (initEngines.containsKey(name)) {
            return engines.computeIfAbsent(name, n -> initEngines.get(n).get());
        }
        throw new IllegalArgumentException("engine '" + name + "' not found");
    }

    public static void registerEngine(String name, ScriptEngine instance) {
        engines.put(name, instance);
    }

    public static void registerEngine(String name, Supplier<ScriptEngine> initializer) {
        initEngines.put(name, initializer);
    }

    public static ScriptEngine getDefaultEngine() {
        return getEngine("rhino");
    }
}
