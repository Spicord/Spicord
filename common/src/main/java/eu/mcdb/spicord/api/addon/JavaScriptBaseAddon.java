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

package eu.mcdb.spicord.api.addon;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class JavaScriptBaseAddon {

    private final Map<String, Object> actions;

    public JavaScriptBaseAddon() {
        this.actions = new HashMap<String, Object>();
    }

    public void on(String action, Object function) {
        actions.put(action, function);
    }

    public Map<String[], Object> buildCommands() {
        final Map<String[], Object> commands = new HashMap<String[], Object>();

        for (final Entry<String, Object> entry : actions.entrySet()) {
            final String k = entry.getKey();

            if (k.startsWith("command:")) {
                final String[] aliases = k.substring("command:".length())
                        .trim().split(", ");

                commands.put(aliases, entry.getValue());
            }
        }

        return commands;
    }

    public Object get(String action) {
        return actions.get(action);
    }
}
