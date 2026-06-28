package org.spicord.script;

public class Function {

    private final Object func;
    private final ScriptEngine engine;

    Function(Object func, ScriptEngine engine) {
        this.func = func;
        this.engine = engine;
    }

    public Object call(Object... args) {
        return engine.callFunction(func, args);
    }
}
