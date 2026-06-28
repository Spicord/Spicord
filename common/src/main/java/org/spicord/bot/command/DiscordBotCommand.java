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

    private final String name;
    @Getter private final Message message;
    @Getter private final User author;
    private final Member member;
    @Getter private final Guild guild;
    @Getter private final GuildMessageChannel channel;
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

    public String getName() {
        return name;
    }

    public String getAuthorAsMention() {
        return author.getAsMention();
    }

    /**
     * Wrap the given message into an embed and send it.
     * 
     * @param message the message to send
     * @see #reply(String, boolean)
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
