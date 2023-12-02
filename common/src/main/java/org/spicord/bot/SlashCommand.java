package org.spicord.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;

public class SlashCommand {

    private CommandCreateAction action;
    private SlashCommandExecutor executor;
    private SlashCommandCompleter completer;

    private SlashCommand(CommandCreateAction command) {
        this.action = command;
    }

    public SlashCommand addOption(OptionType type, String name, String description, boolean required, boolean autoComplete) {
        action.addOption(type, name, description, required, autoComplete);
        return this;
    }

    public SlashCommand setExecutor(SlashCommandExecutor executor) {
        this.executor = executor;
        return this;
    }

    public SlashCommand setCompleter(SlashCommandCompleter completer) {
        this.completer = completer;
        return this;
    }

    public SlashCommandExecutor getExecutor() {
        return executor;
    }

    public SlashCommandCompleter getCompleter() {
        return completer;
    }

    CommandCreateAction getCreateAction() {
        return action;
    }

    static SlashCommand builder(JDA jda, String name, String description) {
        return new SlashCommand(jda.upsertCommand(name, description));
    }
}
