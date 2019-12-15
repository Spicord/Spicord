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

    private final String name;
    private final String permission;
    private final String[] aliases;

    /**
     * Create a command with the given name that doesn't requires
     * any permission to be executed.
     * 
     * @param name the command name
     */
    public UniversalCommand(String name) {
        this(name, null, new String[0]);
    }

    /**
     * Create a command with the given name that requires
     * the given permission to be executed and create the
     * given aliases for this command.
     * 
     * @param name the command name
     * @param permission the command permission
     * @param aliases the aliases of the command
     */
    public UniversalCommand(String name, String permission, String... aliases) {
        this.name = name;
        this.permission = permission;
        this.aliases = aliases;
    }

    /**
     * Handle the command execution.
     * 
     * @param sender who executed this command
     * @param args the command arguments
     * @return true if the command execution ended successfully
     */
    public abstract boolean onCommand(UniversalCommandSender sender, String[] args);

    /**
     * Get the command name.
     * 
     * @return the command name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the needed permission to run this command.
     * 
     * @return the permission
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Get the aliases for this command.
     * 
     * @return the aliases
     */
    public String[] getAliases() {
        return aliases;
    }
}
