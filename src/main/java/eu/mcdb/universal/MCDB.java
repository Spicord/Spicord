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

import net.md_5.bungee.api.plugin.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import eu.mcdb.universal.command.UniversalCommand;
import eu.mcdb.universal.command.UniversalCommandSender;
import eu.mcdb.universal.player.UniversalPlayer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public final class MCDB {

    private MCDB() {
    }

    public static void registerCommand(Plugin plugin, UniversalCommand command) {
        new RegisterBungeeCommand(plugin, command);
    }

    public static void registerCommand(JavaPlugin plugin, UniversalCommand command) {
        new RegisterBukkitCommand(plugin, command);
    }

    private static class RegisterBungeeCommand {

        public RegisterBungeeCommand(Plugin plugin, UniversalCommand command) {
            PluginManager pm = plugin.getProxy().getPluginManager();

            Command cmd = new Command(command.getName(), command.getPermission(), command.getAliases()) {

                @Override
                public void execute(net.md_5.bungee.api.CommandSender sender, String[] args) {
                    UniversalCommandSender commandSender = new UniversalCommandSender() {
                        {
                            if (sender instanceof ProxiedPlayer) {
                                ProxiedPlayer p = (ProxiedPlayer) sender;

                                setPlayer(new UniversalPlayer(p.getName(), p.getUniqueId()) {

                                    @Override
                                    public ProxiedPlayer getProxiedPlayer() {
                                        return p;
                                    };
                                });
                            }
                        }

                        @Override
                        public boolean hasPermission(String permission) {
                            return sender.hasPermission(permission);
                        }

                        @Override
                        public void sendMessage(String message) {
                            sender.sendMessage(new TextComponent(message));
                        }
                    };
                    command.onCommand(commandSender, args);
                }
            };

            pm.registerCommand(plugin, cmd);
        }
    }

    private static class RegisterBukkitCommand {

        public RegisterBukkitCommand(JavaPlugin plugin, UniversalCommand command) {
            plugin.getCommand(command.getName()).setExecutor(new CommandExecutor() {

                @Override
                public boolean onCommand(CommandSender sender, org.bukkit.command.Command arg1, String arg2, String[] args) {
                    UniversalCommandSender commandSender = new UniversalCommandSender() {
                        {
                            if (sender instanceof Player) {
                                Player p = (Player) sender;

                                setPlayer(new UniversalPlayer(p.getName(), p.getUniqueId()) {

                                    public Player getBukkitPlayer() {
                                        return p;
                                    };
                                });
                            }
                        }

                        @Override
                        public boolean hasPermission(String permission) {
                            return sender.hasPermission(permission);
                        }

                        @Override
                        public void sendMessage(String message) {
                            sender.sendMessage(message);
                        }
                    };
                    return command.onCommand(commandSender, args);
                }
            });
        }
    }
}
