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

package org.spicord.fix;

import java.util.ArrayList;
import java.util.Collection;
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
