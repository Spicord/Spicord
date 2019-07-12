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

package eu.mcdb.spicord.bot.command;

import eu.mcdb.spicord.api.bot.command.SimpleCommand;
import lombok.Getter;
import net.dv8tion.jda.core.entities.Message;

public class DiscordBotCommand extends SimpleCommand {

    /**
     * The message object privided by JDA.
     */
    @Getter
    private final Message message;

    /**
     * The constructor.
     * 
     * @param args the command arguments.
     * @param message the message object.
     */
    public DiscordBotCommand(String[] args, Message message) {
        super(args);
        this.message = message;
    }
}
