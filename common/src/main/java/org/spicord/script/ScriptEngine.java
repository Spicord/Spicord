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

public interface ScriptEngine extends IScriptEngine {

    /**
     * Get a new ScriptEngine instance for the requested engine.
     * 
     * @param name the engine name
     * @return the new ScriptEngine instance
     * @throws IllegalArgumentException if the engine was not found
     */
    public static ScriptEngine getEngine(String name) {
        final ScriptEngine engine;

        switch (name) {
        case "nashorn":
            engine = new NashornScriptEngine(); break;
        case "rhino":
            engine = new RhinoScriptEngine(); break;
        default:
            throw new IllegalArgumentException("engine '" + name + "' not found");
        }

        return engine;
    }
}
