package org.spicord.bot;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface SlashCommandExecutor {

    void handle(SlashCommandInteractionEvent event);

}
