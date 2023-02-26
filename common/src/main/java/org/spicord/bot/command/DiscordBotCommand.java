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

package org.spicord.bot.command;

import org.spicord.api.bot.command.SimpleCommand;
import org.spicord.embed.Embed;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;

public class DiscordBotCommand extends SimpleCommand {

    @Getter private final Message message;
    @Getter private final User author;
    private final Member member;
    @Getter private final Guild guild;
    @Getter private final GuildMessageChannel channel;
    @Getter private final String name;
    @Getter private final String prefix;

    /**
     * The constructor.
     * 
     * @param name    the command name
     * @param args    the command arguments
     * @param message the message object
     */
    public DiscordBotCommand(String name, String[] args, Message message) {
        super(args);
        this.name = name;
        this.message = message;
        this.author = message.getAuthor();
        this.member = message.getMember();
        this.guild = message.getGuild();
        this.channel = message.getChannel().asGuildMessageChannel();
        String raw = message.getContentRaw();
        this.prefix = raw.split(" ")[0].substring(0, raw.indexOf(name));
    }

    public String getAuthorAsMention() {
        return author.getAsMention();
    }

    /**
     * Wrap the given message into an embed and send it.
     * 
     * @param message the message to send
     * @see {@link #reply(String, boolean)}
     */
    public void reply(String message) {
        reply(message, true);
    }

    /**
     * Send the given message and choose if it should be wrapped into an embed or not.
     * 
     * @param message the message to send
     * @param wrap true if the message should be wrapped into an embed
     */
    public void reply(String message, boolean wrap) {
        if (wrap) {
            Embed.fromString(message).sendToChannel(channel);
        } else {
            channel.sendMessage(message).queue();
        }
    }

    /**
     * Send the given embed message to the channel this command was called.
     * 
     * @param embed the embed message
     */
    public void reply(Embed embed) {
        embed.sendToChannel(channel);
    }

    /**
     * Send the given JDA embed to the channel this command was called.
     * 
     * @param embed the JDA embed to send
     */
    public void reply(MessageEmbed embed) {
        channel.sendMessageEmbeds(embed).queue();
    }

    /**
     * @deprecated use {@link #getSender()} instead.
     */
    @Deprecated
    public Member getMember() {
        return member;
    }

    public Member getSender() {
        return member;
    }

    @Override
    public String toString() {
        return "[DiscordBotCommand command='" + name + "' author='" + getAuthorAsMention() + "' channel=" + channel.getId() + " message='" + message.getContentRaw() + "']";
    }
}
