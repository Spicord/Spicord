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

        injectSelf();
    }

    @Override
    public void loadJar(Path path) {
        final URL url;
        try {
            url = path.toUri().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        super.addURL(url);
    }

    private void injectSelf() {
        ClassLoader loader = CustomClassLoader.class.getClassLoader();

        Field allParentLoadersField = getDeclaredField(
            loader.getClass(),
            "allParentLoaders"
        );

        if (allParentLoadersField == null) {
            throw new RuntimeException("Field 'allParentLoaders' not found");
        }

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

    private Field getDeclaredField(Class<?> cls, String name) {
        for (; cls != null; cls = cls.getSuperclass()) {
            try {
                return cls.getDeclaredField(name);
            } catch (Exception e) {}
        }
        return null;
    }
}
