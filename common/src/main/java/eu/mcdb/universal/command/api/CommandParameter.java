package eu.mcdb.universal.command.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@RequiredArgsConstructor
public class CommandParameter {

    @Getter
    private final String name;
    @Setter
    private String displayName;
    @Getter
    private final boolean optional;
    @Getter
    private ParameterSuggestionProvider suggestionProvider;

    /**
     * Create a command parameter with the given name
     * and make it a required parameter (non-optional).
     * 
     * @param name the parameter name
     */
    public CommandParameter(String name) {
        this(name, false);
    }

    /**
     * Get the display name for this command, or its name
     * if no display name was set.
     * 
     * @return the name or the display name
     */
    public String getDisplayName() {
        return displayName == null ? name : displayName;
    }
}
