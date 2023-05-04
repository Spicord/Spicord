package org.spicord.sponge;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.spicord.util.JarClassLoader;

public class MostInefficientClassLoader implements JarClassLoader {

    @Override
    public void loadJar(Path path) {
        try (ZipFile zipFile = new ZipFile(path.toFile())) {

            ClassLoader classLoader = MostInefficientClassLoader.class.getClassLoader();

            MethodHandle defineClassMethod = getDefineClassMethod(classLoader);
//            Method defineClassMethod = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
//
//            defineClassMethod.setAccessible(true);

            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            Map<String, byte[]> pendingClasses = new HashMap<>();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();

                if (!entry.getName().endsWith(".class")) {
                    continue;
                }

                byte[] bytes = new byte[1024*8];

                try (
                    InputStream in = zipFile.getInputStream(entry);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream()
                ) {

                    int len;
                    while ((len = in.read(bytes)) != -1) {
                        baos.write(bytes, 0, len);
                    }
                    baos.flush();

                    bytes = baos.toByteArray();
                }

                String className = entry.getName().replace("/", ".");
                className = className.substring(0, className.length() - ".class".length());

                pendingClasses.put(className, bytes);
            }

            int needsToLoad = pendingClasses.size();

            while (needsToLoad > 0) {
                Iterator<Entry<String, byte[]>> iterator = pendingClasses.entrySet().iterator();

                while (iterator.hasNext()) {
                    Entry<String, byte[]> next = iterator.next();

                    String className = next.getKey();
                    byte[] bytes = next.getValue();

                    try {
                        defineClassMethod.invoke(className, bytes, 0, bytes.length);

                        iterator.remove(); // success!
                    } catch (Throwable e) {
                        // this class depends on another class that has not been loaded yet,
                        // will try to load it in the next round, maybe the dependency has
                        // already been loaded by then.
                    }
                }

                int remaining = pendingClasses.size();

                if (remaining < needsToLoad) {
                    needsToLoad = remaining;
                } else {
                    return; // give up here
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private MethodHandle getDefineClassMethod(ClassLoader loader) throws ReflectiveOperationException {
        boolean methodNotFound = false;
        Lookup lookup = MethodHandles.lookup();

        try {
            Method m = MethodHandles.class.getMethod("privateLookupIn", Class.class, Lookup.class);
            lookup = (Lookup) m.invoke(null, loader.getClass(), lookup);
        } catch (ReflectiveOperationException e) {
            methodNotFound = true;
        }

        Method defineClassMethod = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);

        if (methodNotFound) {
            defineClassMethod.setAccessible(true);
        }

        return lookup.unreflect(defineClassMethod).bindTo(loader);
    }
}
