package org.spicord.bot.command;

import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@AllArgsConstructor
public class SlashCommandHandler {
    private SlashCommandExecutor executor;
    private SlashCommandCompleter completer;

    public void execute(SlashCommandInteractionEvent event) {
        if (executor == null) {
            return;
        }
        executor.handle(event);
    }

    public void complete(CommandAutoCompleteInteractionEvent event) {
        if (completer == null) {
            return;
        }
        completer.handle(event);
    }
}
