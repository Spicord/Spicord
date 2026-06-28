package org.spicord.script;

import java.io.File;
import java.io.IOException;

public interface IScriptEngine {

    /**
     * Load an script from the given file
     * 
     * @param <T> the return type
     * @param file the file to load
     * @return the value returned by the script
     * @throws IOException if an error occurred while reading the file
     */
    <T> T loadScript(File file) throws IOException;

    /**
     * Load an script from the given file
     * 
     * @param <T> the return type
     * @param file the file to load
     * @param env the environment to expose to the script
     * @return the value returned by the script
     * @throws IOException if an error occurred while reading the file
     */
    <T> T loadScript(File file, ScriptEnvironment env) throws IOException;

    /**
     * Evaluate an script
     * 
     * @param <T> the return type
     * @param script the script to evaluate
     * @return the value returned by the script
     */
    <T> T eval(String script);

    /**
     * 
     * @param <T>
     * @param ins
     * @param args
     * @return
     */
    <T> T callFunction(final Object ins, final Object... args);

    /**
     * 
     * @param <T>
     * @param object
     * @return
     */
    <T> T wrap(Object object);

    /**
     * 
     * @param <T>
     * @param object
     * @return
     */
    <T> T toJava(Object object);

    /**
     * 
     * @param <T>
     * @param clazz
     * @param object
     * @return
     */
    <T> T toJava(Class<T> clazz, Object object);

    /**
     * Get the module manager of this ScriptEngine
     * 
     * @return the module manager
     */
    ModuleManager getModuleManager();

}
