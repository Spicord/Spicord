package org.spicord.bot.command;

import eu.mcdb.universal.command.api.CommandParameters;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;

@FunctionalInterface
public interface UnparametrizedDiscordCommandHandler extends DiscordCommandHandler {

    boolean handle(DiscordCommandSender sender, GuildMessageChannel channel);

    default boolean handle(DiscordCommandSender sender, GuildMessageChannel channel, CommandParameters parameters) {
        return handle(sender, channel);
    }
}
