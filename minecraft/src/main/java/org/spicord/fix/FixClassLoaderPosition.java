package org.spicord.fix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.spicord.reflect.ReflectedObject;

@SuppressWarnings("all")
public class FixClassLoaderPosition {

    public static boolean init(boolean isBukkit) {
        if (isBukkit) {
            return bukkit();
        } else {
            return bungee();
        }
    }

    public static boolean bukkit() {
        ClassLoader loader = FixClassLoaderPosition.class.getClassLoader();

        Object loadersObj = new ReflectedObject(loader)
                .getField("loader").setAccessible()
                .getReflectValue()
                .getField("loaders").setAccessible().setModifiable()
                .getValue();

        if (loadersObj instanceof Map) {
            Map loaders = (Map) loadersObj;
            Map copy = new HashMap(loaders);
            loaders.clear();
            loaders.put("Spicord", loader);
            loaders.putAll(copy);
            copy.clear();
            copy = null;

            return loaders.get(0) == loader;
        } else if (loadersObj instanceof List) {
            List loaders = (List) loadersObj;
            List copy = new ArrayList(loaders);
            loaders.clear();
            loaders.add(loader);
            loaders.addAll(copy);
            copy.clear();
            copy = null;

            return loaders.get(0) == loader;
        }

        return false;
    }

    public static boolean bungee() {
        ClassLoader loader = FixClassLoaderPosition.class.getClassLoader();

        Set allLoaders = new ReflectedObject(loader)
                .getField("allLoaders")
                .setAccessible()
                .setModifiable()
                .getValue();
        Set copy = new HashSet(allLoaders);
        allLoaders.clear();
        allLoaders.add(loader);
        allLoaders.addAll(copy);
        copy.clear();
        copy = null;

        return allLoaders.iterator().next() == loader;
    }
}
