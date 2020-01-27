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

import java.util.HashMap;
import java.util.Map;

public final class NashornModuleManager implements ModuleManager {

    private final Map<String, Object> modules;
    private final NashornScriptEngine engine;

    public NashornModuleManager(NashornScriptEngine scriptEngine) {
        this.modules = new HashMap<String, Object>();
        this.engine = scriptEngine;
    }

    @Override
    public void register(String name, Class<?> clazz) {
        modules.put(name, getClass(clazz));
    }

    @Override
    public void register(String name, Object obj) {
        modules.put(name, obj);
    }

    @Override
    public boolean isRegistered(String name) {
        boolean b = modules.containsKey(name);

        if (name.startsWith("class:") && !b) {
            final String className = name.substring("class:".length());
            try {
                final Class<?> clazz = Class.forName(className);
                register(name, clazz);
                return true;
            } catch (Exception e) {}
        }

        return b;
    }

    @Override
    public Object getModule(String name) {
        return modules.get(name);
    }

    private Object getClass(Class<?> clazz) {
        final String line = String.format("Java.type('%s');", clazz.getName());
        return engine.eval(line);
    }
}
