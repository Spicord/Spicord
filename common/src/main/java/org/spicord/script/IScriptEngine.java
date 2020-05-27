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
import java.io.IOException;

public interface IScriptEngine {

    /**
     * 
     * @param <T>
     * @param file
     * @return
     * @throws IOException
     */
    <T> T loadScript(File file) throws IOException;

    /**
     * 
     * @param <T>
     * @param file
     * @param env
     * @return
     * @throws IOException
     */
    <T> T loadScript(File file, ScriptEnvironment env) throws IOException;

    /**
     * 
     * @param <T>
     * @param script
     * @return
     */
    <T> T eval(String script);

    /**
     * 
     * @param <T>
     * @param ins
     * @param args
     * @return
     */
    <T> T callFunction(final Object ins, final Object... args);

    /**
     * 
     * @param <T>
     * @param object
     * @return
     */
    <T> T wrap(Object object);

    /**
     * 
     * @param <T>
     * @param object
     * @return
     */
    <T> T toJava(Object object);

    /**
     * 
     * @param <T>
     * @param clazz
     * @param object
     * @return
     */
    <T> T toJava(Class<T> clazz, Object object);

    /**
     * Get the module manager of this ScriptEngine
     * 
     * @return the module manager
     */
    ModuleManager getModuleManager();

}
