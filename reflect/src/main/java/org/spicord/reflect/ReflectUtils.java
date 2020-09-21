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

package org.spicord.reflect;

import static java.lang.invoke.MethodHandles.Lookup.PACKAGE;
import static java.lang.invoke.MethodHandles.Lookup.PRIVATE;
import static java.lang.invoke.MethodHandles.Lookup.PROTECTED;
import static java.lang.invoke.MethodHandles.Lookup.PUBLIC;
import java.io.File;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Optional;

public final class ReflectUtils {

    private static final ReflectedConstructor lookup_c;
    private static final MethodHandles.Lookup fullLookup = MethodHandles.lookup();
    private static final int ALL_MODES = (PUBLIC | PRIVATE | PROTECTED | PACKAGE);

    static {
        lookup_c = new ReflectedObject(Lookup.class)
                .getConstructor(Class.class, int.class)
                .setAccessible();
    }

    private ReflectUtils() {}

    /**
     * 
     * @param clazz
     * @return
     */
    public static Class<?> getArrayClass(Class<?> clazz) {
        return Array.newInstance(clazz, 0).getClass();
    }

    /**
     * 
     * @param name
     * @return
     */
    public static Optional<Class<?>> findClass(String name) {
        try {
            return Optional.of(Class.forName(name));
        } catch (ClassNotFoundException e) {}
        return Optional.empty();
    }

    /**
     * 
     * @param <T>
     * @param action
     * @return
     */
    public static <T> T consumeException(ExceptionConsumer<T> action) {
        return action.runHandled();
    }

    /**
     * 
     * @param m
     * @return
     */
    public static Lookup getLookup(Member m) {
        return MemberUtils.isPublic(m) ? fullLookup : lookup_c.invoke(m.getDeclaringClass(), ALL_MODES);
    }

    /**
     * 
     * @param clazz
     * @return
     */
    public static boolean isFunctionalInterface(Class<?> clazz) {
        return clazz.isInterface() && clazz.isAnnotationPresent(FunctionalInterface.class);
    }

    /**
     * 
     * @param interfaceClass
     * @return
     */
    public static Method getInterfaceMethod(Class<?> interfaceClass) {
        for (Method m : interfaceClass.getMethods()) {
            if (Modifier.isAbstract(m.getModifiers())) {
                return m;
            }
        }
        return null;
    }

    public static <T> T getSingletonFromField(Class<T> clazz) {
        for (Field f : clazz.getDeclaredFields()) {
            if (MemberUtils.isStatic(f) && f.getType() == clazz) {
                return new ReflectedField(null, f).setAccessible().getValue();
            }
        }
        return null;
    }

    public static <T> T getSingletonFromMethod(Class<T> clazz) {
        for (Method m : clazz.getDeclaredMethods()) {
            if (MemberUtils.isStatic(m) && m.getReturnType() == clazz && m.getParameterCount() == 0) {
                return new ReflectedMethod(null, m).setAccessible().invoke();
            }
        }
        return null;
    }

    public static <T> T getSingleton(Class<T> clazz) {
        return getSingletonFromMethod(clazz) == null ? getSingletonFromField(clazz) : null;
    }

    public static String getCaller() {
        // using "2" to skip this method call and the caller of this method
        StackTraceElement st = new Exception().getStackTrace()[2];
        return st.getClassName() + "#" + st.getMethodName();
    }

    public static File getJarFile(Class<?> clazz) {
        URL url = getClassLocation(clazz);

        if (url == null) { // shouldn't happen but who knows...
            return null;
        }

        if ("jar".equals(url.getProtocol())) {
            // jar:file:/[...].jar!/[...].class

            String urlString = url.toString();

            try {
                urlString = urlString.substring(4, urlString.indexOf("!/"));
                return new File(new URL(urlString).toURI());
            } catch (Exception e) {} // MalformedURLException, URISyntaxException
        } else if ("file".equals(url.getProtocol())) {
            // file:/[...].jar    <- jar file
            // file:/[...].class  <- running class from command line
            // file:/[...]/       <- classPath

            String urlString = url.toString();

            if (urlString.endsWith("/") || urlString.endsWith(".jar")) {
                try {
                    return new File(url.toURI());
                } catch (Exception e) {} // URISyntaxException
            } else if (urlString.endsWith(".class")) {
                String path = clazz.getCanonicalName().replace('.', '/') + ".class";
                String newUrl = urlString.substring(0, urlString.length() - path.length());
                try {
                    return new File(new URL(newUrl).toURI());
                } catch (Exception e) {} // MalformedURLException, URISyntaxException
            }
        }

        return null;
    }

    private static URL getClassLocation(Class<?> clazz) {
        try {
            return clazz.getProtectionDomain().getCodeSource().getLocation();
        } catch (Exception e) {} // SecurityException, NPE

        return clazz.getResource(clazz.getName() + ".class");
    }
}
