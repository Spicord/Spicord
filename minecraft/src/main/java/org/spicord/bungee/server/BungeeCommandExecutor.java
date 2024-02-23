package org.spicord.bungee.server;

import eu.mcdb.universal.command.UniversalCommand;
import eu.mcdb.universal.command.UniversalCommandSender;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

public final class BungeeCommandExecutor extends Command {

    private final UniversalCommand command;

    public BungeeCommandExecutor(final UniversalCommand command) {
        super(command.getName(), command.getPermission(), command.getAliases());
        this.command = command;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        UniversalCommandSender commandSender;

        if (sender instanceof ProxiedPlayer) {
            commandSender = new BungeePlayer((ProxiedPlayer) sender);
        } else {
            commandSender = new UniversalCommandSender() {

                @Override
                public boolean hasPermission(String permission) {
                    return isEmpty(permission) || sender.hasPermission(permission);
                }

                @Override
                public void sendMessage(String message) {
                    sender.sendMessage(new TextComponent(message));
                }

                private boolean isEmpty(String string) {
                    return string == null || string.isEmpty();
                }
            };
        }

        command.onCommand(commandSender, args);
    }

    public static void register(Plugin plugin, UniversalCommand command) {
        plugin.getProxy().getPluginManager().registerCommand(plugin, new BungeeCommandExecutor(command));
    }
}
