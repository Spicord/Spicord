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

package org.spicord.api.addon;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Logger;
import org.spicord.Spicord;
import org.spicord.bot.DiscordBot;
import org.spicord.bot.command.DiscordBotCommand;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;

public abstract class SimpleAddon {

    private final String name;
    private final String id;
    private final String author;
    private final String version;
    private final String[] commands;

    //--------------
    private Spicord spicord;
    //--------------

    private File file;
    private File dataFolder;
    private Logger logger;

    public void initFields(Spicord spicord, File file, File dataFolder, Logger logger) {
        this.spicord = spicord;
        this.file = file;
        this.dataFolder = dataFolder;
        this.logger = logger;
    }

    /**
     * Constructor.
     * 
     * @param name   the addon name
     * @param id     the addon id
     * @param author the addon author
     */
    public SimpleAddon(String name, String id, String author) {
        this(name, id, author, new String[0]);
    }

    /**
     * Constructor.
     * 
     * @param name    the addon name
     * @param id      the addon id
     * @param author  the addon author
     * @param version the addon version
     */
    public SimpleAddon(String name, String id, String author, String version) {
        this(name, id, author, version, new String[0]);
    }

    /**
     * Constructor.
     * 
     * @param name     the addon name
     * @param id       the addon id
     * @param author   the addon author
     * @param commands the command list
     */
    public SimpleAddon(String name, String id, String author, String[] commands) {
        this(name, id, author, "unknown", commands);
    }

    /**
     * Constructor.
     * 
     * @param name     the addon name
     * @param id       the addon id
     * @param author   the addon author
     * @param version  the addon version
     * @param commands the command list
     */
    public SimpleAddon(String name, String id, String author, String version, String[] commands) {
        this.name = name;
        this.id = id;
        this.author = author;
        this.version = version;
        this.commands = commands;
    }

    public void onRegister(Spicord spicord) {
    }

    public void onUnregister(Spicord spicord) {
    }

    /**
     * This method will be called when a bot loads this addon.
     * 
     * @param bot the bot, may not be started yet
     */
    public void onLoad(DiscordBot bot) {
    }

    /**
     * This method will be called when a bot that loaded this addon is ready.
     * 
     * @param bot the bot, ready to be used
     */
    public void onReady(DiscordBot bot) {
    }

    /**
     * This method will only be called if you passed a command list to the
     * constructor.
     * <p>
     * You can use {@link DiscordBotCommand#getName()} to know what alias was
     * called.
     * 
     * @param command the command instance that contains information about the
     *                sender and related to the message
     * @param args    the command arguments
     */
    public void onCommand(DiscordBotCommand command, String[] args) {
    }

    /**
     * This method will be called when a bot that loaded this addon receives a
     * message.
     * 
     * @param bot   the bot that received the message
     * @param event the message event data
     */
    public void onMessageReceived(DiscordBot bot, MessageReceivedEvent event) {
    }

    /**
     * This method will be called when a bot that loaded this addon is shutting
     * down. You should use this method to stop interacting with JDA and this bot.
     * 
     * @param bot the bot
     */
    public void onShutdown(DiscordBot bot) {
        if (!id.startsWith("spicord::")) {
            getLogger().warning(String.format("The addon %s (%s) does not implement the onShutdown() method", name, id));
        }
    }

    /**
     * This method is called when Spicord is being disabled (for example when the
     * server shutdowns). You can use this method to release resources.
     */
    public void onDisable() {
    }

    /**
     * Check if this addon is a JavaScript addon.
     * 
     * @return true if this is a JavaScript addon
     */
    public final boolean isJavaScriptAddon() {
        return this instanceof JavaScriptAddon;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getVersion() {
        return version;
    }

    @Deprecated
    public String[] getCommands() {
        return commands;
    }

    public Spicord getSpicord() {
        return spicord;
    }

    public File getFile() {
        return file;
    }

    public File getDataFolder() {
        return dataFolder;
    }

    public Logger getLogger() {
        return logger;
    }

    public Collection<GatewayIntent> getRequiredIntents() {
        return Collections.emptySet();
    }
}
