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
import eu.mcdb.spicord.util.ArrayUtils;
import eu.mcdb.universal.command.UniversalCommand;
import eu.mcdb.universal.command.UniversalCommandSender;
import lombok.Getter;

@Getter
public class Command extends UniversalCommand {

    private final String name;
    private final String permission;
    private final Set<Command> subCommands;
    private final Map<Integer, CommandParameter> parameters;
    private CommandHandler commandHandler;
    private Command parent;

    public Command(String name) {
        this(name, null);
    }

    public Command(String name, String permission) {
        this(name, permission, new String[0]);
    }

    public Command(String name, String permission, String... aliases) {
        super(name, permission, aliases);
        this.name = name;
        this.permission = permission;
        this.subCommands = new LinkedHashSet<Command>();
        this.parameters = new LinkedHashMap<Integer, CommandParameter>();
    }

    public void addSubCommand(final String name, final CommandHandler handler) {
        final Command command = new Command(name);
        command.setCommandHandler(handler);
        this.addSubCommand(command);
    }

    public void addSubCommand(final String name, final String permission, final CommandHandler handler) {
        final Command command = new Command(name, permission);
        command.setCommandHandler(handler);
        this.addSubCommand(command);
    }

    public void addSubCommand(final Command subcommand) {
        subcommand.parent = this;
        this.subCommands.add(subcommand);
    }

    public void setParameter(final int index, final CommandParameter parameter) {
        this.parameters.put(index, parameter);
    }

    public void setCommandHandler(final CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public void setCommandHandler(final UnparametrizedCommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public Command getSubCommand(final String name) {
        final Predicate<Command> filter = command -> command.getName().equals(name);
        return subCommands.stream().filter(filter).findFirst().orElse(null);
    }

    @Override
    public boolean onCommand(final UniversalCommandSender sender, final String[] args) {
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

    public void sendAllUsage(final UniversalCommandSender sender) {
        sender.sendFormattedMessage(getUsage());
        subCommands.stream().map(Command::getUsage).forEach(sender::sendFormattedMessage);
    }

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

    public String getUsage() {
        final StringBuilder usage = new StringBuilder("Usage: /")
                .append(parent == null ? "" : parent.getName().concat(" "))
                .append(name);

        for (CommandParameter param : parameters.values()) {
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
}
