package org.spicord.bot.command;

import eu.mcdb.universal.command.UniversalCommandSender;
import eu.mcdb.universal.command.api.CommandHandler;
import eu.mcdb.universal.command.api.CommandParameters;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;

@FunctionalInterface
public interface DiscordCommandHandler extends CommandHandler {

    boolean handle(DiscordCommandSender sender, GuildMessageChannel channel, CommandParameters parameters);

    @Override
    default boolean handle(UniversalCommandSender sender, CommandParameters parameters) {
        final DiscordCommandSender commandSender = (DiscordCommandSender) sender;
        return handle(commandSender, commandSender.channel, parameters);
    }
}
