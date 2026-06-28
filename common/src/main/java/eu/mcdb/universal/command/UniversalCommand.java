package eu.mcdb.universal.command;

import eu.mcdb.universal.Server;

import java.util.List;

public abstract class UniversalCommand {

    private final String name;
    private final String permission;
    private final String[] aliases;

    /**
     * Create a command with the given name that doesn't requires
     * any permission to be executed.
     * 
     * @param name the command name
     */
    public UniversalCommand(String name) {
        this(name, null, new String[0]);
    }

    /**
     * Create a command with the given name that requires
     * the given permission to be executed and create the
     * given aliases for this command.
     * 
     * @param name the command name
     * @param permission the command permission
     * @param aliases the aliases of the command
     */
    public UniversalCommand(String name, String permission, String... aliases) {
        this.name = name;
        this.permission = permission;
        this.aliases = aliases;
    }

    /**
     * Handle the command execution.
     * 
     * @param sender who executed this command
     * @param args the command arguments
     * @return true if the command execution ended successfully
     */
    public abstract boolean onCommand(UniversalCommandSender sender, String[] args);

    /**
     * Get a list of command suggestions
     *
     * @param sender the sender attempting to get suggestions
     * @param args the entered arguments
     * @return a list of command suggestions
     */
    public abstract List<String> getSuggestions(UniversalCommandSender sender, String[] args);

    /**
     * Get the command name.
     * 
     * @return the command name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the needed permission to run this command.
     * 
     * @return the permission
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Get the aliases for this command.
     * 
     * @return the aliases
     */
    public String[] getAliases() {
        return aliases;
    }

    public void register(Object plugin) {
        Server.getInstance().registerCommand(plugin, this);
    }
}
