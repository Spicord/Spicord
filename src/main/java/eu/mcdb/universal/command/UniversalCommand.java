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

package eu.mcdb.universal.command;

public abstract class UniversalCommand {

    /**
     * The command name.
     */
    private final String name;
    /**
     * The command permission.
     */
    private final String permission;
    /**
     * The command aliases.
     */
    private final String[] aliases;

    /**
     * UniversalCommand constructor.
     * 
     * @param name the command name.
     */
    public UniversalCommand(String name) {
        this(name, null, new String[0]);
    }

    /**
     * UniversalCommand constructor.
     * 
     * @param name the command name.
     * @param permission the command permission.
     * @param aliases the aliases of the command.
     */
    public UniversalCommand(String name, String permission, String... aliases) {
        this.name = name;
        this.permission = permission;
        this.aliases = aliases;
    }

    public abstract boolean onCommand(UniversalCommandSender sender, String[] args);

    /**
     * Gets the command name.
     * 
     * @return the command name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the needed permission for this command.
     * 
     * @return the command permission.
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Gets the aliases of this command.
     * 
     * @return the aliases.
     */
    public String[] getAliases() {
        return aliases;
    }
}
