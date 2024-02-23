package org.spicord.velocity.server;

import org.spicord.plugin.VelocityPlugin;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;

import eu.mcdb.universal.command.UniversalCommand;
import eu.mcdb.universal.command.UniversalCommandSender;
import net.kyori.adventure.text.Component;

public final class VelocityCommandExecutor implements SimpleCommand {

    private final UniversalCommand command;

    public VelocityCommandExecutor(UniversalCommand command) {
        this.command = command;
    }

    @Override
    public void execute(Invocation invocation) {
        final CommandSource source = invocation.source();
        final String[] args = invocation.arguments();

        UniversalCommandSender commandSender;

        if (source instanceof Player) {
            commandSender = new VelocityPlayer((Player) source);
        } else {
            commandSender = new UniversalCommandSender() {

                @Override
                public boolean hasPermission(String permission) {
                    return isEmpty(permission) || source.hasPermission(permission);
                }

                @Override
                public void sendMessage(String message) {
                    source.sendMessage(Component.text(message));
                }

                private boolean isEmpty(String string) {
                    return string == null || string.isEmpty();
                }
            };
        }

        command.onCommand(commandSender, args);
    }

    public static void register(Object plugin, UniversalCommand command) {
        VelocityPlugin.getCommandManager().register(command.getName(), new VelocityCommandExecutor(command), command.getAliases());
    }
}
