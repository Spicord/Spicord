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

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import eu.mcdb.universal.player.UniversalPlayer;

public class BukkitCommandExecutor implements CommandExecutor {

    private final UniversalCommand command;

    public BukkitCommandExecutor(UniversalCommand command) {
        this.command = command;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        UniversalCommandSender commandSender = new UniversalCommandSender() {

            @Override
            public boolean hasPermission(String permission) {
                return isEmpty(permission) || sender.hasPermission(permission);
            }

            @Override
            public void sendMessage(String message) {
                sender.sendMessage(message);
            }

            private boolean isEmpty(String s) {
                return s == null || "".equals(s);
            }
        };

        if (sender instanceof Player) {
            Player player = (Player) sender;

            commandSender.setPlayer(new UniversalPlayer(player.getName(), player.getUniqueId()) {

                @Override
                public Player getBukkitPlayer() {
                    return player;
                }
            });
        }

        return command.onCommand(commandSender, args);
    }
}
