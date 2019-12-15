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

    /**
     * Create an addon.
     * 
     * @param name   the addon name
     * @param key    the addon key
     * @param author the addon author
     */
    public SimpleAddon(String name, String key, String author) {
        this(name, key, author, new String[0]);
    }

    /**
     * Create an addon and pass the command name that the addon will use.
     * 
     * The method {@link #onCommand(DiscordBotCommand, String[])} will be called
     * when one of the given commands are executed by an user.
     * 
     * You can use this if this addon will only provide one command with some
     * aliases to it, for example:
     * 
     * new SimpleAddon(_,_,_, new String[] {"hello", "hi"})
     * 
     * If the addon will have different commands with different functions, you
     * should use {@link DiscordBotCommand#getName()} to know what command is being
     * executed.
     * 
     * @param name     the addon name
     * @param key      the addon key
     * @param author   the addon author
     * @param commands the command list or aliases
     */
    public SimpleAddon(String name, String key, String author, String[] commands) {
        this.name = name;
        this.key = key;
        this.author = author;
        this.commands = commands;
    }

    /**
     * Method called when a bot loads this addon, the bot may not be started yet.
     * 
     * @param bot the bot that loaded this addon
     */
    public void onLoad(DiscordBot bot) {
    }

    /**
     * Method called when a bot that loaded this addon is ready.
     * 
     * @param bot the bot ready to be used
     */
    public void onReady(DiscordBot bot) {
    }

    /**
     * Method called only if you used the
     * {@link #SimpleAddon(String, String, String, String[])} constructor to build
     * this addon.
     * 
     * @param command the command instance that contains information about the
     *                sender and related things
     * @param args    the command arguments
     */
    public void onCommand(DiscordBotCommand command, String[] args) {
    }

    /**
     * Method called when a bot that loaded this addon receives a message.
     * 
     * @param bot   the bot that received the message
     * @param event the message event data
     */
    public void onMessageReceived(DiscordBot bot, MessageReceivedEvent event) {
    }
}
