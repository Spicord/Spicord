package eu.mcdb.spicord.bot.command;

import eu.mcdb.universal.command.api.CommandParameters;
import net.dv8tion.jda.core.entities.MessageChannel;

@FunctionalInterface
public interface UnparametrizedDiscordCommandHandler extends DiscordCommandHandler {

    boolean handle(DiscordCommandSender sender, MessageChannel channel);

    default boolean handle(DiscordCommandSender sender, MessageChannel channel, CommandParameters parameters) {
        return handle(sender, channel);
    }
}
