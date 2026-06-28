package org.spicord.api.addon;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class JavaScriptBaseAddon {

    private final Map<String, Object> actions;
    private String[] requiredIntents;

    public JavaScriptBaseAddon() {
        this.actions = new HashMap<String, Object>();
    }

    public void on(String action, Object function) {
        actions.put(action, function);
    }

    public void setRequiredIntents(String... requiredIntents) {
        this.requiredIntents = requiredIntents;
    }

    public String[] getRequiredIntents() {
        return requiredIntents;
    }

    public Map<String[], Object> buildCommands() {
        final Map<String[], Object> commands = new HashMap<String[], Object>();

        for (final Entry<String, Object> entry : actions.entrySet()) {
            final String k = entry.getKey();

            if (k.startsWith("command:")) {
                final String[] aliases = k.substring("command:".length())
                        .trim().split(", ");

                commands.put(aliases, entry.getValue());
            }
        }

        return commands;
    }

    public Object get(String action) {
        return actions.get(action);
    }
}
