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

import java.lang.annotation.Annotation;
import org.bukkit.plugin.java.JavaPlugin;
import org.spicord.plugin.SpongePlugin;
import org.spicord.plugin.VelocityPlugin;
import org.spongepowered.api.Sponge;
import eu.mcdb.universal.command.BukkitCommandExecutor;
import eu.mcdb.universal.command.BungeeCommandExecutor;
import eu.mcdb.universal.command.SpongeCommandExecutor;
import eu.mcdb.universal.command.UniversalCommand;
import eu.mcdb.universal.command.VelocityCommandExecutor;
import net.md_5.bungee.api.plugin.Plugin;

@Deprecated
public final class MCDB {

    private MCDB() {}

    /**
     * Register a command to the server.
     * 
     * @deprecated Use {@link UniversalCommand#register(Object)} instead
     * @param plugin  the plugin instance
     * @param command the command to be registered
     */
    public static void registerCommand(Object plugin, UniversalCommand command) {
        if (isInstance(plugin, "net.md_5.bungee.api.plugin.Plugin"))
            new registerBungeeCommand((Plugin) plugin, command);
        else if (isInstance(plugin, "org.bukkit.plugin.java.JavaPlugin"))
            new registerBukkitCommand((JavaPlugin) plugin, command);
        else if (isInstance(plugin, "org.spicord.plugin.VelocityPlugin"))
            new registerVelocityCommand((VelocityPlugin) plugin, command);
        else if (isSpongePlugin(plugin))
            new registerSpongeCommand((SpongePlugin) plugin, command);
        else
            throw new IllegalArgumentException("plugin");
    }

    @SuppressWarnings("unchecked")
    private static boolean isSpongePlugin(Object plugin) {
        try {
            Class<?> pluginClass = plugin.getClass();
            Class<?> pluginAnnotation = Class.forName("org.spongepowered.api.plugin.Plugin");

            return isInstance(plugin, "org.spicord.plugin.SpongePlugin")
                    || pluginClass.isAnnotationPresent((Class<? extends Annotation>) pluginAnnotation);
        } catch (ClassNotFoundException e) {}
        return false;
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
            VelocityPlugin.getCommandManager().register(command.getName(), new VelocityCommandExecutor(command), command.getAliases());
        }
    }

    private static class registerSpongeCommand {
        registerSpongeCommand(Object plugin, UniversalCommand command) {
            Sponge.getGame().getCommandManager().register(plugin, new SpongeCommandExecutor(command).get(), command.getAliases());
        }
    }
}
