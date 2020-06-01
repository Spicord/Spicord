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
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.NativeJSON;
import org.mozilla.javascript.NativeJavaArray;
import org.mozilla.javascript.NativeJavaClass;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

@SuppressWarnings({ "unchecked", "static-access" })
class RhinoScriptEngine extends ScriptEngine {

    private static final Gson GSON = new Gson();
    private static final List<Method> nativeMethods = new ArrayList<Method>();
    private static final int UNMODIFIABLE = READONLY|DONTENUM|PERMANENT;
    private static final Object UNDEFINED = Undefined.instance;

    private final ScriptableObject scope;
    private static RhinoModuleManager moduleManager; // this souldn't be static!! :( but it need to be accessed from require()

    static {
        nativeMethods.addAll(findMethods(RhinoScriptEngine.class));
    }

    public RhinoScriptEngine() {
        this.scope = getContext().initStandardObjects();
        moduleManager = new RhinoModuleManager(this);
    }

    @Override
    public <T> T callFunction(final Object ins, final Object... args) {
        if (ins instanceof Function) {
            final Function func = (Function) ins;
            Scriptable scope = func.getParentScope();
            return (T) func.call(getContext(), scope, scope, javaToJS(args));
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
        return toJava(require(getContext(), info));
    }

    @Override
    public Object eval(String script) {
        return getContext().evaluateString(scope, script, "<eval>", 1, null);
    }

    @Override
    public <T> T wrap(Object object) {
        if (object == null)
            return null;

        if (object instanceof Class<?>) {
            return (T) new NativeJavaClass(scope, (Class<?>)object);
        }

        return (T) getContext().javaToJS(object, scope);
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

    @Override
    public void close() {

    }

    // custom methods below this line ---

    private Context getContext() {
        Context ctx = Context.getCurrentContext();
        if (ctx == null) {
            ctx = Context.enter();
        }
        return ctx;
    }

    private Object[] javaToJS(Object... args) {
        return Stream.of(args)
                .map(this::javaToJS)
                .toArray();
    }

    protected Object javaToJS(Object obj) {
        return getContext().javaToJS(obj, scope);
    }

    private static ScriptableObject createScope(Context cx) {
        ScriptableObject newScope = cx.initStandardObjects();

        for (Method m : nativeMethods) {
            FunctionObject fun = new FunctionObject(m.getName(), m, newScope);
            newScope.defineProperty(m.getName(), fun, UNMODIFIABLE);
        }

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

        return UNDEFINED;
    }

    @NativeMethod
    public static Object require(Context cx, Scriptable thisObj, Object[] args, Function funObj) throws IOException {
        if (args.length == 1) {
            String module = String.valueOf(args[0]);

            if (moduleManager.isRegistered(module)) {
                return moduleManager.getModule(module);
            }

            String dirname = (String) thisObj.get("__dirname", thisObj);
            File file = resolve(dirname, module);

            if (file == null) {
                return UNDEFINED;
            }

            if (file.getName().endsWith(".json")) {
                return parseJSON(cx, thisObj, file);
            } else {
                ScriptInfo info = new ScriptInfo(file);
                return require(cx, info);
            }
        }
        return UNDEFINED;
    }

    private static Object parseJSON(Context cx, Scriptable scope, File file) throws IOException {
        byte[] b = Files.readAllBytes(file.toPath());
        return parseJSON(cx, scope, new String(b));
    }

    private static Object parseJSON(Context cx, Scriptable scope, String json) {
        return NativeJSON.parse(cx, scope, json, (p1, p2, p3, args) -> args[1]);
    }

    @NativeMethod
    public static Object JS(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        if (args.length == 1) {
            Object obj = args[0];

            if (obj == null || Undefined.isUndefined(obj)) {
                return UNDEFINED;
            }

            if (obj instanceof NativeObject) {
                return obj;
            }

            if (obj instanceof Map || obj instanceof Collection || obj.getClass().isArray()) {
                JsonElement tree = GSON.toJsonTree(obj);
                String str = GSON.toJson(tree);
                return parseJSON(cx, thisObj, str);
            }

            return Context.javaToJS(obj, thisObj);
        }
        return UNDEFINED;
    }

    @NativeMethod
    public static Object print(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        String[] strings = new String[args.length];

        for (int i = 0; i < strings.length; i++) {
            strings[i] = Context.toString(args[i]);
        }

        System.out.println(String.join(" ", strings));

        return UNDEFINED;
    }

    private static List<Method> findMethods(Class<?> clazz) {
        List<Method> methods = new ArrayList<Method>();
        for (Method m : clazz.getMethods()) {
            if (m.isAnnotationPresent(NativeMethod.class)) {
                methods.add(m);
            }
        }
        return methods;
    }

    private static File resolve(String dir, String name) {
        File file = new File(dir, name);

        if (file.exists()) {
            if (file.isDirectory()) {
                file = new File(file, "index.js");
            }
        } else {
            file = new File(dir, name + ".js");
            if (!file.exists()) {
                file = new File(dir, name + ".json");
            }
        }

        return (
                file.exists() &&
                file.isFile() &&
                (file.getName().endsWith(".js") || file.getName().endsWith(".json"))
            ) ? file : null;
    }
}
