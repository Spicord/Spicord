package org.spicord.bot.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.Getter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;

@Getter
public class SlashCommandBuilder {

    private String name;
    private String description;

    private SlashCommandExecutor executor;
    private SlashCommandCompleter completer;

    private boolean guildOnly = false;
    private boolean nsfw = false;

    private List<SlashCommandOption> options = new ArrayList<>();

    private DefaultMemberPermissions defaultMemberPermissions = DefaultMemberPermissions.ENABLED;

    public SlashCommandBuilder(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public SlashCommandBuilder setGuildOnly(boolean guildOnly) {
        this.guildOnly = guildOnly;
        return this;
    }

    public SlashCommandBuilder setNSFW(boolean nsfw) {
        this.nsfw = nsfw;
        return this;
    }

    public SlashCommandBuilder setDefaultPermissions(Collection<Permission> permissions) {
        this.defaultMemberPermissions = DefaultMemberPermissions.enabledFor(permissions);
        return this;
    }

    public SlashCommandBuilder setDefaultPermissions(Permission... permissions) {
        this.defaultMemberPermissions = DefaultMemberPermissions.enabledFor(permissions);
        return this;
    }

    public SlashCommandBuilder addOption(OptionType type, String name, String description, boolean required, boolean autoComplete) {
        this.options.add(new SlashCommandOption(type, name, description, required, autoComplete));
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
}
