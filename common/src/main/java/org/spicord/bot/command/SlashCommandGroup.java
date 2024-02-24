package org.spicord.bot.command;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

@Getter
public class SlashCommandGroup {

    private String name;
    private String description;

    private List<SlashCommand> subcommands = new LinkedList<>();

    public SlashCommandGroup(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public SlashCommandGroup addSubcommand(SlashCommand subcommand) {
        this.subcommands.add(subcommand);
        return this;
    }

    public SubcommandGroupData buildGroup() {
        SubcommandGroupData data = new SubcommandGroupData(name, description);
        for (SlashCommand subcommand : subcommands) {
            data.addSubcommands(subcommand.toJdaSubcommand());
        }
        return data;
    }
}
