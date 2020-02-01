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

import java.util.Arrays;
import java.util.List;
import org.bukkit.plugin.java.JavaPlugin;
import eu.mcdb.universal.command.BukkitCommandExecutor;
import eu.mcdb.universal.command.BungeeCommandExecutor;
import eu.mcdb.universal.command.SpongeCommandExecutor;
import eu.mcdb.universal.command.UniversalCommand;
import eu.mcdb.universal.command.VelocityCommandExecutor;
import eu.mcdb.universal.plugin.SpongePlugin;
import eu.mcdb.universal.plugin.VelocityPlugin;
import net.md_5.bungee.api.plugin.Plugin;

public final class MCDB {

    private MCDB() {}

    /**
     * Register a command to the server.
     * 
     * @param plugin  the plugin instance
     * @param command the command to be registered
     */
    public static void registerCommand(Object plugin, UniversalCommand command) {
        if (isInstance(plugin, "net.md_5.bungee.api.plugin.Plugin"))
            new registerBungeeCommand((Plugin) plugin, command);
        else if (isInstance(plugin, "org.bukkit.plugin.java.JavaPlugin"))
            new registerBukkitCommand((JavaPlugin) plugin, command);
        else if (isInstance(plugin, "eu.mcdb.universal.plugin.VelocityPlugin"))
            new registerVelocityCommand((VelocityPlugin) plugin, command);
        else if (isInstance(plugin, "eu.mcdb.universal.plugin.SpongePlugin"))
            new registerSpongeCommand((SpongePlugin) plugin, command);
        else
            throw new IllegalArgumentException();
    }

    private static boolean isInstance(Object plugin, String className) {
        try {
            final Class<?> clazz = Class.forName(className);
            return clazz.isInstance(plugin);
        } catch (ClassNotFoundException e) {}
        return false;
    }

    private static class registerBungeeCommand {
        registerBungeeCommand(Plugin plugin, UniversalCommand command) {
            plugin.getProxy().getPluginManager().registerCommand(plugin, new BungeeCommandExecutor(command));
        }
    }

    private static class registerBukkitCommand {
        registerBukkitCommand(JavaPlugin plugin, UniversalCommand command) {
            plugin.getCommand(command.getName()).setExecutor(new BukkitCommandExecutor(command));
        }
    }

    private static class registerVelocityCommand {
        registerVelocityCommand(VelocityPlugin plugin, UniversalCommand command) {
            final List<String> aliases = Arrays.asList(command.getAliases());
            aliases.add(command.getName());

            plugin.getCommandManager().register(new VelocityCommandExecutor(command), aliases.toArray(new String[aliases.size()]));
        }
    }

    private static class registerSpongeCommand {
        registerSpongeCommand(SpongePlugin plugin, UniversalCommand command) {
            plugin.getCommandManager().register(plugin, new SpongeCommandExecutor(command).get(), command.getAliases());
        }
    }
}
