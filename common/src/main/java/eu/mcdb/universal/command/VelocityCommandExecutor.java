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

import org.spicord.player.VelocityPlayer;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

/**
 * Wrapper for the {@link UniversalCommand} class to
 * make it usable by Velocity.
 * 
 * @author sheidy
 */
public final class VelocityCommandExecutor implements SimpleCommand {

    private final UniversalCommand command;

    public VelocityCommandExecutor(UniversalCommand command) {
        this.command = command;
    }

    @Override
    public void execute(Invocation invocation) {
        final CommandSource source = invocation.source();
        final String[] args = invocation.arguments();

        UniversalCommandSender commandSender;

        if (source instanceof Player) {
            commandSender = new VelocityPlayer((Player) source);
        } else {
            commandSender = new UniversalCommandSender() {

                @Override
                public boolean hasPermission(String permission) {
                    return isEmpty(permission) || source.hasPermission(permission);
                }

                @Override
                public void sendMessage(String message) {
                    source.sendMessage(Component.text(message));
                }

                private boolean isEmpty(String string) {
                    return string == null || string.isEmpty();
                }
            };
        }

        command.onCommand(commandSender, args);
    }
}
