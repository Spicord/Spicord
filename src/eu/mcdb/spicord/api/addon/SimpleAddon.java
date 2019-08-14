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

import eu.mcdb.spicord.bot.DiscordBot;
import eu.mcdb.util.IServer;
import eu.mcdb.util.Server;
import lombok.Data;
import lombok.Getter;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@Data
public abstract class SimpleAddon {

    private String name;
    private String key;
    private String author;
    private String[] commands;

    public SimpleAddon(String name, String key, String author) {
        this(name, key, author, new String[0]);
    }

    public SimpleAddon(String name, String key, String author, String[] commands) {
        this.name = name;
        this.key = key;
        this.author = author;
        this.commands = commands;
    }

    public abstract void onLoad(DiscordBot bot);

    public void onCommand(String cmd, String[] args, MessageReceivedEvent event) {
    }

    @Getter
    public static IServer server = Server.getInstance();
}
