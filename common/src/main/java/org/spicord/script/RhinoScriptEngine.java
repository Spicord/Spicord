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
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeJavaArray;
import org.mozilla.javascript.NativeJavaClass;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.ScriptableObject;
import org.spicord.util.AbsoluteFile;
import org.spicord.util.FileSystem;

@SuppressWarnings("unchecked")
class RhinoScriptEngine implements ScriptEngine {

    protected final Context ctx;
    protected final ScriptableObject scope;
    private final RhinoModuleManager moduleManager;
    private final String BASE_SCRIPT;

    public RhinoScriptEngine() {
        this.ctx = Context.enter();
        this.scope = ctx.initStandardObjects();
        this.moduleManager = new RhinoModuleManager(this);

        final String base = "const print = (print) => {"
                + "    if (print == null)"
                + "        print = 'null';"
                + "    return java.lang.System.out.println(java.lang.String.valueOf(print));"
                + "};"
                + "const console = { log: print };"
                + "const __core = { require: {} };"
                + "__setup = function(core) {"
                + "    __core.require = (dir, str) => core.require(dir, str);"
                + "    delete __setup;"
                + "};";

        this.eval(base);
        this.callFunction("__setup", this);

        this.BASE_SCRIPT = "(function() {"
                + "    const module = { exports: {} };"
                + "    const __dirname = \"{{{dirname}}}\";"
                + "    const require = (name) => __core.require(__dirname, name);"
                + "    {{{body}}}"
                + ""
                + "    return module.exports;"
                + "})();";
    }

    @Override
    public <T> T eval(final String script) {
        return (T) ctx.evaluateString(scope, script, "<eval>", 0, null);
    }

    @Override
    public <T> T callFunction(final String name, final Object... args) {
        final Function func = (Function) scope.get(name, scope);

        return (T) func.call(ctx, scope, scope, convertArgs(args));
    }

    @Override
    public <T> T callFunction(final Object ins, final Object... args) {
        if (ins instanceof Function) {
            final Function func = (Function) ins;

            return (T) func.call(ctx, scope, scope, convertArgs(args));
        } else {
            throw new IllegalArgumentException("given object is not a javascript object");
        }
    }

    private Object[] convertArgs(Object... args) {
        final Object[] _args = new Object[args.length];

        for (int i = 0; i < args.length; i++)
            _args[i] = Context.javaToJS(args[i], scope);

        return _args;
    }

    @Override
    public <T> T loadScript(final File file) throws IOException {
        return (T) ctx.evaluateReader(scope, new FileReader(file), file.getName(), 0, null);
    }

    @Override
    public <T> T loadScript(final Reader reader) throws IOException, ScriptException {
        return (T) ctx.evaluateReader(scope, reader, "<script>", 0, null);
    }

    @Override
    public <T> T require(final String dir, final String name) throws IOException {
        if (moduleManager.isRegistered(name))
            return (T) moduleManager.getModule(name);

        final File file = AbsoluteFile.of(dir, name);

        if (file.exists() && file.isFile()) {
            final String script = buildScript(file);
            return eval(script);
        } else {
            if (name.endsWith(".js")) {
                throw new ScriptException("the module/script '" + name + "' was not found");
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
        if (obj instanceof NativeJavaObject)
            return (T) ((NativeJavaObject) obj).unwrap();
        else if (obj instanceof NativeJavaClass)
            return (T) ((NativeJavaClass) obj).unwrap();
        else if (obj instanceof NativeJavaArray)
            return (T) ((NativeJavaArray) obj).unwrap();

        return (T) obj;
    }

    private String buildScript(final File file) throws IOException {
        final String parent = file.toPath().getParent().toString();
        return BASE_SCRIPT
                .replace("{{{dirname}}}", parent)
                .replace("{{{body}}}", FileSystem.readFile(file));
    }

    @Override
    public ModuleManager getModuleManager() {
        return moduleManager;
    }
}
