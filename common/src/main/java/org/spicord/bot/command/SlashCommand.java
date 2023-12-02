package org.spicord.bot.command;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

@Getter
public class SlashCommand {

    private String name;
    private String description;

    private SlashCommandExecutor executor;
    private SlashCommandCompleter completer;

    private boolean guildOnly = false;
    private boolean nsfw = false;

    private List<SlashCommandOption> options = new LinkedList<>();
    private List<SlashCommand> subcommands = new LinkedList<>();
    private List<SlashCommandGroup> subcommandGroups = new LinkedList<>();

    private DefaultMemberPermissions defaultMemberPermissions = DefaultMemberPermissions.ENABLED;

    public SlashCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public SlashCommand setGuildOnly(boolean guildOnly) {
        this.guildOnly = guildOnly;
        return this;
    }

    public SlashCommand setNSFW(boolean nsfw) {
        this.nsfw = nsfw;
        return this;
    }

    public SlashCommand setDefaultPermissions(Collection<Permission> permissions) {
        this.defaultMemberPermissions = DefaultMemberPermissions.enabledFor(permissions);
        return this;
    }

    public SlashCommand setDefaultPermissions(Permission... permissions) {
        this.defaultMemberPermissions = DefaultMemberPermissions.enabledFor(permissions);
        return this;
    }

    public SlashCommand addOption(OptionType type, String name, String description, boolean required, boolean autoComplete) {
        this.options.add(new SlashCommandOption(type, name, description, required, autoComplete));
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

    public SlashCommand addSubcommandGroup(SlashCommandGroup group) {
        this.subcommandGroups.add(group);
        return this;
    }

    public SlashCommand addSubcommand(SlashCommand subcommand) {
        this.subcommands.add(subcommand);
        return this;
    }

    public boolean isSingle() {
        return subcommandGroups.isEmpty() && subcommands.isEmpty();
    }

    public SubcommandData buildAsSubcommand() {
        SubcommandData data = new SubcommandData(name, description);
        for (SlashCommandOption option : options) {
            data.addOptions(option.buildOption());
        }
        return data;
    }
}
