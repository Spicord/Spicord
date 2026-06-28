package org.spicord.api.bot.command;

import org.spicord.bot.command.DiscordBotCommand;

@FunctionalInterface
public interface BotCommand {

    void onCommand(DiscordBotCommand command, String[] args);
}
