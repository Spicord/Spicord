package org.spicord.bot.command;

import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.OptionType;

@Getter
public class SlashCommandOption {

    private OptionType type;
    private String name;
    private String description;
    private boolean required;
    private boolean autoComplete;

    public SlashCommandOption(OptionType type, String name, String description, boolean required, boolean autoComplete) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.required = required;
        this.autoComplete = autoComplete;
    }
}
