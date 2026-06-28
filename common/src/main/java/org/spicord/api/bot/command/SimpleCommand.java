package org.spicord.api.bot.command;

public abstract class SimpleCommand {

    private final String[] arguments;

    /**
     * Constructor.
     * 
     * @param args the command arguments
     */
    public SimpleCommand(String[] args) {
        this.arguments = args;
    }

    /**
     * Get the command arguments.
     * 
     * @return the command arguments
     */
    public String[] getArguments() {
        return arguments;
    }
}
