package org.spicord.bot;

import java.util.Collection;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;

public class SlashCommandBuilder {

    private CommandCreateAction action;
    private SlashCommandExecutor executor;
    private SlashCommandCompleter completer;

    private SlashCommandBuilder(CommandCreateAction command) {
        this.action = command;
    }

    public SlashCommandBuilder setGuildOnly(boolean guildOnly) {
        action.setGuildOnly(guildOnly);
        return this;
    }

    public SlashCommandBuilder setNSFW(boolean nsfw) {
        action.setNSFW(nsfw);
        return this;
    }

    public SlashCommandBuilder setDefaultPermissions(Collection<Permission> permissions) {
        action.setDefaultPermissions(DefaultMemberPermissions.enabledFor(permissions));
        return this;
    }

    public SlashCommandBuilder setDefaultPermissions(Permission... permissions) {
        action.setDefaultPermissions(DefaultMemberPermissions.enabledFor(permissions));
        return this;
    }

    public SlashCommandBuilder addOption(OptionType type, String name, String description, boolean required, boolean autoComplete) {
        action.addOption(type, name, description, required, autoComplete);
        return this;
    }

    public SlashCommandBuilder setExecutor(SlashCommandExecutor executor) {
        this.executor = executor;
        return this;
    }

    public SlashCommandBuilder setCompleter(SlashCommandCompleter completer) {
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

    static SlashCommandBuilder builder(CommandCreateAction action) {
        return new SlashCommandBuilder(action);
    }
}
