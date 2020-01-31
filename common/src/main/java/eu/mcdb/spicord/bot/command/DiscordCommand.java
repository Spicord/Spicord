/*
 * Copyright (C) 2020  OopsieWoopsie
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

package eu.mcdb.spicord.bot.command;

import eu.mcdb.spicord.api.bot.command.BotCommand;
import eu.mcdb.universal.command.api.Command;
import net.dv8tion.jda.core.Permission;

public class DiscordCommand extends Command implements BotCommand {

    public DiscordCommand(String name) {
        super(name);
    }

    public DiscordCommand(String name, Permission permission) {
        super(name, toString(permission));
    }

    public DiscordCommand(String name, Permission permission, String[] aliases) {
        super(name, toString(permission), aliases);
    }

    public void setCommandHandler(DiscordCommandHandler commandHandler) {
        super.setCommandHandler(commandHandler);
    }

    public void setCommandHandler(UnparametrizedDiscordCommandHandler commandHandler) {
        super.setCommandHandler(commandHandler);
    }

    public void addSubCommand(String name, DiscordCommandHandler handler) {
        super.addSubCommand(name, handler);
    }

    public void addSubCommand(String name, String permission, DiscordCommandHandler handler) {
        super.addSubCommand(name, permission, handler);
    }

    @Override
    public final void onCommand(DiscordBotCommand command, String[] args) {
        final DiscordCommandSender sender = new DiscordCommandSender(command);
        super.onCommand(sender, args);
    }

    private static String toString(Permission permission) {
        return permission == null ? null : permission.toString();
    }
}
