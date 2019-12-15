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
import eu.mcdb.spicord.embed.Embed;
import eu.mcdb.spicord.embed.EmbedSender;
import lombok.Getter;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

@Getter
public class DiscordBotCommand extends SimpleCommand {

    private final Message message;
    private final User author;
    private final Member member;
    private final Guild guild;
    private final TextChannel channel;
    private final String name;
    private final String prefix;

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
        this.channel = message.getTextChannel();
        String raw = message.getContentRaw();
        this.prefix = raw.split(" ")[0].substring(0, raw.indexOf(name));
    }

    public String getAuthorAsMention() {
        return author.getAsMention();
    }

    public void reply(String message) {
        reply(Embed.fromString(message));
    }

    public void reply(Embed embed) {
        EmbedSender.prepare(channel, embed).queue();
    }

    public void reply(MessageEmbed embed) {
        channel.sendMessage(embed).queue();
    }

    @Override
    public String toString() {
        return "[DiscordBotCommand command='" + name + "' author='" + getAuthorAsMention() + "' channel=" + channel.getId() + " message='" + message.getContentRaw() + "']";
    }
}
