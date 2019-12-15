/*
 * Copyright (C) 2019  OopsieWoopsie
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package eu.mcdb.universal.command.api;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import eu.mcdb.universal.command.UniversalCommand;
import eu.mcdb.universal.command.UniversalCommandSender;
import eu.mcdb.util.ArrayUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Command extends UniversalCommand {

    private final String name;
    private final String permission;
    private final Set<Command> subCommands;
    private final Map<Integer, CommandParameter> parameters;
    private CommandHandler commandHandler;
    private Command parent;
    private String prefix;
    @Setter
    private boolean usageEnabled;

    /**
     * Create a command with a given name and make it usable for everyone
     * (no permission required).
     * 
     * A command can be also used as a subcommand for another command.
     * 
     * @param name the command name
     */
    public Command(String name) {
        this(name, null);
    }

    /**
     * Create a command with a given name and only allow it to be executed by
     * entities that has the given permission.
     * 
     * @param name       the command name
     * @param permission the required permission
     */
    public Command(String name, String permission) {
        this(name, permission, new String[0]);
    }

    /**
     * Create a command with a given name and only allow it to be executed by
     * entities that has the given permission and create aliases for that command.
     * 
     * @param name       the command name
     * @param permission the required permission
     * @param aliases    the command aliases
     */
    public Command(String name, String permission, String... aliases) {
        super(name, permission, aliases);
        this.name = name;
        this.permission = permission;
        this.subCommands = new LinkedHashSet<Command>();
        this.parameters = new LinkedHashMap<Integer, CommandParameter>();
        this.prefix = "/";
        this.usageEnabled = true;
    }

    /**
     * Add a subcommand for this command.
     * 
     * @param name    the command name
     * @param handler the command handler
     */
    public void addSubCommand(final String name, final CommandHandler handler) {
        final Command command = new Command(name);
        command.setCommandHandler(handler);
        this.addSubCommand(command);
    }

    /**
     * Add a subcommand for this command.
     * 
     * @param name       the command name
     * @param permission the command permission
     * @param handler    the command handler
     */
    public void addSubCommand(final String name, final String permission, final CommandHandler handler) {
        final Command command = new Command(name, permission);
        command.setCommandHandler(handler);
        this.addSubCommand(command);
    }

    /**
     * Add a subcommand for this command.
     * 
     * @param subcommand the subcommand
     */
    public void addSubCommand(final Command subcommand) {
        subcommand.parent = this;
        this.subCommands.add(subcommand);
    }

    public void setParameter(final int index, final CommandParameter parameter) {
        this.parameters.put(index, parameter);
    }

    /**
     * Set the command handler for this command.
     * 
     * @param commandHandler the command handler
     */
    public void setCommandHandler(final CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    /**
     * Set the command handler for this command.
     * Note: The command handler will not receive any parameter.
     * 
     * @param commandHandler the command handler
     */
    public void setCommandHandler(final UnparametrizedCommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    /**
     * Get a subcommand instance by its name.
     * 
     * @param name the subcommand name
     * @return the subcommand, may be null
     */
    public Command getSubCommand(final String name) {
        final Predicate<Command> filter = command -> command.getName().equals(name);
        return subCommands.stream().filter(filter).findFirst().orElse(null);
    }

    @Override
    public final boolean onCommand(final UniversalCommandSender sender, final String[] args) {
        if (args.length == 0) {
            if (commandHandler != null) {
                if (sender.hasPermission(getPermission())) {
                    if (parameters.size() == 0) {
                        return commandHandler.handle(sender, null);
                    } else sendAllUsage(sender);
                } else sendMissingPermissionMessage(sender);
            } else sendAllUsage(sender);
            return false;
        } else {
            final String sub = args[0];

            for (final Command command : subCommands) {
                if (command.getName().equals(sub)) {
                    if (sender.hasPermission(command.getPermission())) {
                        final String[] args1 = ArrayUtils.shift(args);

                        if (command.isApplicable(args1)) {
                            final CommandParameters params = command.buildParameters(args1);
                            return command.getCommandHandler().handle(sender, params);
                        } else command.sendUsage(sender);
                    } else sendMissingPermissionMessage(sender);
                    return false;
                }
            }

            if (commandHandler != null) {
                if (sender.hasPermission(getPermission())) {
                    if (isApplicable(args)) {
                        final CommandParameters params = buildParameters(args);
                        return commandHandler.handle(sender, params);
                    } else sendAllUsage(sender);
                } else sendMissingPermissionMessage(sender);
            }
        }
        return false;
    }

    /**
     * Send the usage of this command and its subcommands to the given sender.
     * 
     * @param sender the sender
     */
    public void sendAllUsage(final UniversalCommandSender sender) {
        sender.sendFormattedMessage(getUsage());
        subCommands.stream().map(Command::getUsage).forEach(sender::sendFormattedMessage);
    }

    /**
     * Send the usage of this command to the given sender.
     * 
     * @param sender the sender
     */
    public void sendUsage(final UniversalCommandSender sender) {
        sender.sendFormattedMessage(getUsage());
    }

    private void sendMissingPermissionMessage(final UniversalCommandSender sender) {
        sender.sendFormattedMessage("&cYou do not have permission to run this command");
    }

    @Override
    public String getPermission() {
        return permission != null ? permission : (parent != null ? parent.getPermission() : null);
    }

    /**
     * Get the auto-generated usage for this command.
     * 
     * @return the usage, may be null if {@link #setUsageEnabled(boolean)} was
     *         called with the 'false' parameter value
     */
    public String getUsage() {
        if (!usageEnabled)
            return null;

        final StringBuilder usage = new StringBuilder("Usage: ")
                .append(prefix)
                .append(parent == null ? "" : parent.getName().concat(" "))
                .append(name);

        for (final CommandParameter param : parameters.values()) {
            if (param.isOptional()) {
                usage.append(" [").append(param.getDisplayName()).append("]");
            } else {
                usage.append(" <").append(param.getDisplayName()).append(">");
            }
        }

        return usage.toString();
    }

    private boolean isApplicable(final String[] args) {
        return args.length >= getRequiredParametersCount() && args.length <= parameters.size();
    }

    private int getRequiredParametersCount() {
        int count = 0;
        for (CommandParameter p : parameters.values()) {
            if (!p.isOptional())
                count++;
        }
        return count;
    }

    private CommandParameters buildParameters(final String[] args) {
        final Map<String, String> values = new HashMap<String, String>(args.length);

        int i = 0;
        for (final CommandParameter param : parameters.values()) {
            values.put(param.getName(), i >= args.length ? null : args[i]);
            i++;
        }

        return new CommandParameters(values);
    }

    public void detachChilds() {
        for (final Command sc : subCommands) {
            sc.parent = null;
        }
    }

    /**
     * Set the command prefix for the usage message.
     * 
     * This class can be use to handle commands for any software, not only Minecraft Servers.
     * So you can change the command prefix that is '/' by default.
     * The usage prefix for all the subcommands (if any) will be also changed.
     * 
     * @param prefix the new command prefix
     */
    public void setUsagePrefix(String prefix) {
        this.prefix = prefix;
        for (final Command sc : subCommands) {
            sc.prefix = prefix;
        }
    }
}
