/*
 * Copyright (C) 2019  OopsieWoopsie
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

package eu.mcdb.universal.command.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@RequiredArgsConstructor
public class CommandParameter {

    @Getter
    private final String name;
    @Setter
    private String displayName;
    @Getter
    private final boolean optional;

    /**
     * Create a command parameter with the given name
     * and make it a required parameter (non-optional).
     * 
     * @param name the parameter name
     */
    public CommandParameter(String name) {
        this(name, false);
    }

    /**
     * Get the display name for this command, or its name
     * if no display name was set.
     * 
     * @return the name or the display name
     */
    public String getDisplayName() {
        return displayName == null ? name : displayName;
    }
}
