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

import static org.mozilla.javascript.ScriptableObject.DONTENUM;
import static org.mozilla.javascript.ScriptableObject.PERMANENT;
import static org.mozilla.javascript.ScriptableObject.READONLY;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.NativeJavaArray;
import org.mozilla.javascript.NativeJavaClass;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

@SuppressWarnings({ "unchecked", "static-access" })
class RhinoScriptEngine extends ScriptEngine {

    static {
        try {
            method = RhinoScriptEngine.class.getMethod("require", Context.class, Scriptable.class, Object[].class, Function.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final Gson GSON = new Gson();
    private static final Method method;
    private static final int UNMODIFIABLE = READONLY|DONTENUM|PERMANENT;

    private final ScriptableObject scope;
    private static RhinoModuleManager moduleManager; // this souldn't be static!! :( but it need to be accessed from require()

    public RhinoScriptEngine() throws IOException {
        this.scope = context().initStandardObjects();
        moduleManager = new RhinoModuleManager(this);
    }

    @Override
    public <T> T callFunction(final Object ins, final Object... args) {
        if (ins instanceof Function) {
            final Function func = (Function) ins;
            Scriptable scope = func.getParentScope();
            return (T) func.call(context(), scope, scope, javaToJS(args));
        } else {
            throw new IllegalArgumentException("given object is not a function");
        }
    }

    @Override
    public <T> T loadScript(File file) throws IOException {
        return loadScript(file, new ScriptEnvironment());
    }

    @Override
    public <T> T loadScript(File file, ScriptEnvironment env) throws IOException {
        ScriptInfo info = new ScriptInfo(file, env);
        info.getEnvironment().put("__engine", this);
        return toJava(require(context(), info));
    }

    @Override
    public Object eval(String script) {
        return context().evaluateString(scope, script, "<eval>", 1, null);
    }

    @Override
    public <T> T wrap(Object object) {
        if (object == null)
            return null;

        if (object instanceof Class<?>) {
            return (T) new NativeJavaClass(scope, (Class<?>)object);
        }

        return (T) context().javaToJS(object, scope);
    }

    @Override
    public <T> T toJava(Object obj) {
        if (obj instanceof NativeJavaObject)
            return (T) ((NativeJavaObject) obj).unwrap();
        else if (obj instanceof NativeJavaClass)
            return (T) ((NativeJavaClass) obj).unwrap();
        else if (obj instanceof NativeJavaArray)
            return (T) ((NativeJavaArray) obj).unwrap();
        else if (obj instanceof Function)
            return (T) new org.spicord.script.Function(obj, this);

        return (T) obj;
    }

    @Override
    public <T> T toJava(Class<T> clazzOfT, Object object) {
        if (object instanceof NativeObject) {
            final NativeObject nobj = (NativeObject) object;
            final JsonElement json = GSON.toJsonTree((Map<?, ?>) nobj);
            return GSON.fromJson(json, clazzOfT);
        }
        return toJava(object);
    }

    @Override
    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    // custom methods below this line ---

    private Context context() {
        return Context.enter();
    }

    private Object[] javaToJS(Object... args) {
        return Stream.of(args)
                .map(this::javaToJS)
                .toArray();
    }

    public Object javaToJS(Object obj) {
        return context().javaToJS(obj, scope); // thread safe
    }

    private static ScriptableObject createScope(Context ctx) {
        ScriptableObject newScope = ctx.initStandardObjects();
        FunctionObject fun = new FunctionObject("require", method, newScope);
        newScope.defineProperty("require", fun, UNMODIFIABLE);

        NativeObject module = new NativeObject();
        module.defineProperty("exports", new NativeObject(), 0);

        newScope.defineProperty("module", module, 0);
        return newScope;
    }

    public static Object require(Context cx, ScriptInfo info) throws IOException {
        ScriptableObject newScope = createScope(cx);
        newScope.defineProperty("__dirname", info.getDirectory(), UNMODIFIABLE);

        if (info.hasEnvironment()) {
            for (Entry<String, Object> e : info.getEnvironment().entrySet()) {
                Object val = Context.javaToJS(e.getValue(), newScope);
                newScope.defineProperty(e.getKey(), val, UNMODIFIABLE);
            }
        }

        cx.evaluateReader(newScope, info.getReader(), info.getFileName(), 0, null);
        Object m = newScope.get("module", newScope);
        if (m != null && m instanceof NativeObject) {
            NativeObject obj = (NativeObject) m;
            return obj.get("exports", newScope);
        }
        return null;
    }

    public static Object require(Context cx, Scriptable thisObj, Object[] args, Function funObj) throws IOException {
        if (args.length == 1) {
            String module = String.valueOf(args[0]);

            if (moduleManager.isRegistered(module)) {
                return moduleManager.getModule(module);
            }

            String dirname = (String) thisObj.get("__dirname", thisObj);
            File file = resolve(dirname, module);
            if (file == null) return null;
            ScriptInfo info = new ScriptInfo(file);
            return require(cx, info);
        }
        return null;
    }

    private static File resolve(String dirname, String module) {
        File file = new File(dirname, module);

        if (file.exists()) {
            if (file.isDirectory()) {
                file = new File(file, "index.js");
            }
        } else {
            file = new File(dirname, module + ".js");
        }

        return (
                file.exists() &&
                file.isFile() &&
                file.getName().endsWith(".js")
            ) ? file : null;
    }
}
