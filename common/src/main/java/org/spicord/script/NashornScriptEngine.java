/*
 * Copyright (C) 2020  OopsieWoopsie
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.spicord.script;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import javax.script.Invocable;
import javax.script.ScriptEngineManager;
import org.spicord.script.module.Path;
import org.spicord.util.AbsoluteFile;
import org.spicord.util.FileSystem;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

@SuppressWarnings("all")
class NashornScriptEngine implements ScriptEngine {

    private static final Gson GSON = new Gson();

    protected final javax.script.ScriptEngine nashorn;
    private final NashornModuleManager moduleManager;
    private final String BASE_SCRIPT;

    public NashornScriptEngine() {
        System.setProperty("nashorn.args", "--language=es6");
        this.nashorn = new ScriptEngineManager().getEngineByName("nashorn");

        if (nashorn == null)
            throw new IllegalStateException("the nashorn engine is not present on this JVM");

        this.moduleManager = new NashornModuleManager(this);

        final String setup = "const console = { log: print };"
                + "const __core = { require: {}, __engine: {} };"
                + "__setup = function(core) {"
                + "    __core.require = function(dir, str) {"
                + "        return core.require(dir, str);"
                + "    };"
                + "    __core.__engine = core;"
                + "    delete __setup;"
                + "};"
                + "const J = function(obj) {"
                + "    return __core.__engine.java(obj);"
                + "};";

        this.eval(setup);
        this.callFunction("__setup", this);

        this.BASE_SCRIPT = "(function() {"
                + "    const module = { exports: {} };"
                + "    const __dirname = \"{{{dirname}}}\";"
                + "    const require = function(name) {"
                + "        return __core.require(__dirname, name)"
                + "    };"
                + "    {{{body}}}"
                + ";"
                + "    return module.exports;"
                + "})();";
    }

    @Override
    public <T> T eval(final String script) throws ScriptException {
        try {
            return (T) nashorn.eval(script, nashorn.getContext());
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }

    @Override
    public <T> T callFunction(final String name, final Object... args) throws ScriptException {
        try {
            return (T) ((Invocable) nashorn).invokeFunction(name, args);
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }

    @Override
    public <T> T callFunction(final Object ins, final Object... args) throws ScriptException {
        try {
            if (ins instanceof ScriptObjectMirror) {
                final ScriptObjectMirror func = (ScriptObjectMirror) ins;

                if (func.isFunction()) {
                    return (T) func.call(ins, args);
                } else {
                    throw new IllegalArgumentException("given object is not a function");
                }
            } else {
                throw new IllegalArgumentException("given object is not a javascript object");
            }
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }

    @Override
    public <T> T loadScript(final File file) throws IOException, ScriptException {
        return (T) loadScript(new FileReader(file));
    }

    @Override
    public <T> T loadScript(final Reader reader) throws IOException, ScriptException {
        try {
            return (T) nashorn.eval(reader, nashorn.getContext());
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }

    @Override
    public <T> T require(final String dir, final String name) throws IOException {
        if (moduleManager.isRegistered(name)) {
            return (T) moduleManager.getModule(name);
        }

        File file = AbsoluteFile.of(dir, name);

        if (file.isDirectory())
            file = new File(file, "index.js");

        if (file.exists() && file.isFile()) {
            final String script = buildScript(file);
            return eval(script);
        } else {
            if (name.endsWith(".js")) {
                return null;
            }
            return require(dir, name + ".js");
        }
    }

    @Override
    public <T> T require(final File file) throws IOException {
        if (file.exists() && file.isFile()) {
            final String script = buildScript(file);
            return eval(script);
        }
        return null;
    }

    @Override
    public <T> T java(final Object obj) {
        if (obj instanceof ScriptObjectMirror) {
            final ScriptObjectMirror som = (ScriptObjectMirror) obj;

            if (som.isFunction()) {
                return (T) new Function(obj, this);
            }
        }
        return (T) obj;
    }

    @Override
    public <T> T java(Class<T> clazzOfT, Object object) {
        if (object instanceof ScriptObjectMirror) {
            final ScriptObjectMirror som = (ScriptObjectMirror) object;

            if (som.getClassName().equals("Object")) {
                final JsonElement json = GSON.toJsonTree((Map) som);

                return GSON.fromJson(json, clazzOfT);
            }
        }
        return java(object);
    }

    private String buildScript(final File file) throws IOException {
        final String parent = file.toPath().getParent().toString();
        return BASE_SCRIPT
                .replace("{{{dirname}}}", Path.normalize(parent))
                .replace("{{{body}}}", FileSystem.readFile(file));
    }

    @Override
    public ModuleManager getModuleManager() {
        return moduleManager;
    }
}
