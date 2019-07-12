package eu.mcdb.spicord.api.bot.command;

import eu.mcdb.spicord.bot.command.DiscordBotCommand;

@FunctionalInterface
public interface BotCommand {

    void onCommand(DiscordBotCommand command, String[] args);
}
