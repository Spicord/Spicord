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

import org.spicord.player.BungeePlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/**
 * Wrapper for the {@link UniversalCommand} class to
 * make it usable by BungeeCord.
 * 
 * @author sheidy
 */
public final class BungeeCommandExecutor extends Command {

    private final UniversalCommand command;

    public BungeeCommandExecutor(final UniversalCommand command) {
        super(command.getName(), command.getPermission(), command.getAliases());
        this.command = command;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        UniversalCommandSender commandSender;

        if (sender instanceof ProxiedPlayer) {
            commandSender = new BungeePlayer((ProxiedPlayer) sender);
        } else {
            commandSender = new UniversalCommandSender() {

                @Override
                public boolean hasPermission(String permission) {
                    return isEmpty(permission) || sender.hasPermission(permission);
                }

                @Override
                public void sendMessage(String message) {
                    sender.sendMessage(new TextComponent(message));
                }

                private boolean isEmpty(String string) {
                    return string == null || string.isEmpty();
                }
            };
        }

        command.onCommand(commandSender, args);
    }
}
