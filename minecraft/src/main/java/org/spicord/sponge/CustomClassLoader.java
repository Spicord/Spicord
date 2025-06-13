package org.spicord.sponge;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import org.spicord.util.JarClassLoader;

import com.google.gson.Gson;

public class CustomClassLoader extends URLClassLoader implements JarClassLoader {

    public CustomClassLoader() {
        super(new URL[0], Gson.class.getClassLoader());

        injectTo(CustomClassLoader.class.getClassLoader());
    }

    @Override
    public void loadJar(Path path) {
        try {
            super.addURL(path.toUri().toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private void injectTo(ClassLoader loader) {
        Class<?> currentClass = loader.getClass();
        Field allParentLoadersField = null;

        do {

            if (!ClassLoader.class.isAssignableFrom(currentClass)) {
                throw new RuntimeException("Unable to find 'allParentLoaders' field");
            }

            try {
                allParentLoadersField = currentClass.getDeclaredField("allParentLoaders");
            } catch (Exception e) {
                currentClass = currentClass.getSuperclass();
            }

        } while (allParentLoadersField == null);

        try {
            allParentLoadersField.setAccessible(true);

            @SuppressWarnings({ "rawtypes", "unchecked" })
            List<ClassLoader> originalList = (List) allParentLoadersField.get(loader);

            LinkedList<ClassLoader> newList = new LinkedList<>(originalList);

            newList.addFirst(this);

            allParentLoadersField.set(loader, newList);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject field", e);
        }
    }
}
