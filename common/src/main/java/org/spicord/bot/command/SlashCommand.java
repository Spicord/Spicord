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

    /**
     * Construct a new SlashCommand instance.
     * 
     * @param name the command name
     * @param description the command description
     */
    public SlashCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Set whether this command should only be present on guilds.
     * 
     * @param guildOnly
     * @return this SlashCommand instance
     */
    public SlashCommand setGuildOnly(boolean guildOnly) {
        this.guildOnly = guildOnly;
        return this;
    }

    /**
     * Set whether this command has NSFW purposes. 
     * 
     * @param nsfw
     * @return this SlashCommand instance
     */
    public SlashCommand setNSFW(boolean nsfw) {
        this.nsfw = nsfw;
        return this;
    }

    /**
     * Set the permissions a member must have in order to see and use this command.
     * 
     * @param permissions the list of permissions
     * @return this SlashCommand instance
     */
    public SlashCommand setDefaultPermissions(Collection<Permission> permissions) {
        this.defaultMemberPermissions = DefaultMemberPermissions.enabledFor(permissions);
        return this;
    }

    /**
     * Set the permissions a member must have in order to see and use this command.
     * 
     * @param permissions the list of permissions
     * @return this SlashCommand instance
     */
    public SlashCommand setDefaultPermissions(Permission... permissions) {
        this.defaultMemberPermissions = DefaultMemberPermissions.enabledFor(permissions);
        return this;
    }

    /**
     * Add an option to this command.
     * 
     * @param type the option type
     * @param name the option name
     * @param description the option description
     * @return this SlashCommand instance
     */
    public SlashCommand addOption(OptionType type, String name, String description) {
        return addOption(type, name, description, false, false);
    }

    /**
     * Add an option to this command.
     * 
     * @param type the option type
     * @param name the option name
     * @param description the option description
     * @param required true if the option must be set by the user
     * @return this SlashCommand instance
     */
    public SlashCommand addOption(OptionType type, String name, String description, boolean required) {
        return addOption(type, name, description, required, false);
    }

    /**
     * Add an option to this command.
     * 
     * @param type the option type
     * @param name the option name
     * @param description the option description
     * @param required true if the option must be set by the user
     * @param autoComplete true if auto-completion is provided by this option
     * @return this SlashCommand instance
     */
    public SlashCommand addOption(OptionType type, String name, String description, boolean required, boolean autoComplete) {
        this.options.add(new SlashCommandOption(type, name, description, required, autoComplete));
        return this;
    }

    /**
     * Set this command executor.
     * 
     * @param executor the executor
     * @return this SlashCommand instance
     */
    public SlashCommand setExecutor(SlashCommandExecutor executor) {
        this.executor = executor;
        return this;
    }

    /**
     * Set this command option completer.
     * 
     * @param completer the completer
     * @return this SlashCommand instance
     */
    public SlashCommand setCompleter(SlashCommandCompleter completer) {
        this.completer = completer;
        return this;
    }

    /**
     * Add a subcommand group to this command.
     * 
     * @param group the subcommand group to add
     * @return this SlashCommand instance
     */
    public SlashCommand addSubcommandGroup(SlashCommandGroup group) {
        this.subcommandGroups.add(group);
        return this;
    }

    /**
     * Add a subcommand to this command.
     * 
     * @param subcommand the subcommand to add
     * @return this SlashCommand instance
     */
    public SlashCommand addSubcommand(SlashCommand subcommand) {
        this.subcommands.add(subcommand);
        return this;
    }

    /**
     * Check if this command has subcommands.
     * @return true if this command has subcommands
     */
    public boolean isSingle() {
        return subcommandGroups.isEmpty() && subcommands.isEmpty();
    }

    public SubcommandData toJdaSubcommand() {
        SubcommandData data = new SubcommandData(name, description);
        for (SlashCommandOption option : options) {
            data.addOptions(option.toJdaOption());
        }
        return data;
    }
}
