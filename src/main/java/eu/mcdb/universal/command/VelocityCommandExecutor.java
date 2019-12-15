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

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.text.TextComponent;

/**
 * Wrapper for the {@link UniversalCommand} class to
 * make it usable by Velocity.
 * 
 * @author sheidy
 */
public final class VelocityCommandExecutor implements Command {

    private final UniversalCommand command;

    public VelocityCommandExecutor(UniversalCommand command) {
        this.command = command;
    }

    @Override
    public void execute(CommandSource source, String[] args) {
        UniversalCommandSender commandSender = new UniversalCommandSender() {

            @Override
            public boolean hasPermission(String permission) {
                return isEmpty(permission) || source.hasPermission(permission);
            }

            @Override
            public void sendMessage(String message) {
                source.sendMessage(TextComponent.of(message));
            }

            private boolean isEmpty(String s) {
                return s == null || "".equals(s);
            }
        };

        command.onCommand(commandSender, args);
    }
}
