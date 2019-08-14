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
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class DiscordBotCommand extends SimpleCommand {

    /**
     * The message object privided by JDA.
     */
    @Getter
    private final Message message;
    @Getter
    private User author;
    @Getter
    private Guild guild;
    @Getter
    private TextChannel channel;

    /**
     * The constructor.
     * 
     * @param args    the command arguments.
     * @param message the message object.
     */
    public DiscordBotCommand(String[] args, Message message) {
        super(args);
        this.message = message;
        this.author = message.getAuthor();
        this.guild = message.getGuild();
        this.channel = message.getTextChannel();
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
}
