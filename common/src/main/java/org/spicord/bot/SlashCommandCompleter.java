package org.spicord.bot;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;

public interface SlashCommandCompleter {

    void handle(CommandAutoCompleteInteractionEvent event);

}
