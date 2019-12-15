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

package eu.mcdb.universal;

import org.bukkit.plugin.java.JavaPlugin;
import eu.mcdb.universal.command.BukkitCommandExecutor;
import eu.mcdb.universal.command.BungeeCommandExecutor;
import eu.mcdb.universal.command.UniversalCommand;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.plugin.Plugin;

@UtilityClass
public class MCDB {

    /**
     * Register a command to the server.
     * 
     * @param plugin the BungeeCord plugin instance
     * @param command the command to be registered
     */
    public void registerCommand(Plugin plugin, UniversalCommand command) {
        new registerBungeeCommand(plugin, command);
    }

    /**
     * Register a command to the server.
     * 
     * @param plugin the Bukkit plugin instance
     * @param command the command to be registered
     */
    public void registerCommand(JavaPlugin plugin, UniversalCommand command) {
        new registerBukkitCommand(plugin, command);
    }

    private class registerBungeeCommand {
        registerBungeeCommand(Plugin plugin, UniversalCommand command) {
            plugin.getProxy().getPluginManager().registerCommand(plugin, new BungeeCommandExecutor(command));
        }
    }

    private class registerBukkitCommand {
        registerBukkitCommand(JavaPlugin plugin, UniversalCommand command) {
            plugin.getCommand(command.getName()).setExecutor(new BukkitCommandExecutor(command));
        }
    }
}
