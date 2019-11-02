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

package eu.mcdb.spicord.api.addon;

import eu.mcdb.spicord.Spicord;
import eu.mcdb.spicord.bot.DiscordBot;
import eu.mcdb.spicord.bot.command.DiscordBotCommand;
import eu.mcdb.util.Server;
import lombok.Getter;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@Getter
public abstract class SimpleAddon {

    private final Server server = Server.getInstance();
    private final Spicord spicord = Spicord.getInstance();

    private final String name;
    private final String key;
    private final String author;
    private final String[] commands;

    public SimpleAddon(String name, String key, String author) {
        this(name, key, author, new String[0]);
    }

    public SimpleAddon(String name, String key, String author, String[] commands) {
        this.name = name;
        this.key = key;
        this.author = author;
        this.commands = commands;
    }

    public void onLoad(DiscordBot bot) {
    }

    public void onReady(DiscordBot bot) {
    }

    public void onCommand(DiscordBotCommand command, String[] args) {
    }

    public void onMessageReceived(DiscordBot bot, MessageReceivedEvent event) {
    }
}
