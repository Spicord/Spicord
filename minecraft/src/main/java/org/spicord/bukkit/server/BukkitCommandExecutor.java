package org.spicord.bukkit.server;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.spicord.player.BukkitPlayer;

import eu.mcdb.universal.command.UniversalCommand;
import eu.mcdb.universal.command.UniversalCommandSender;

public final class BukkitCommandExecutor implements CommandExecutor {

    private final UniversalCommand command;

    public BukkitCommandExecutor(UniversalCommand command) {
        this.command = command;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        UniversalCommandSender commandSender;

        if (sender instanceof Player) {
            commandSender = new BukkitPlayer((Player) sender);
        } else {
            commandSender = new UniversalCommandSender() {

                @Override
                public boolean hasPermission(String permission) {
                    return isEmpty(permission) || sender.hasPermission(permission);
                }

                @Override
                public void sendMessage(String message) {
                    sender.sendMessage(message);
                }

                private boolean isEmpty(String string) {
                    return string == null || string.isEmpty();
                }
            };
        }

        return command.onCommand(commandSender, args);
    }

    public static void register(JavaPlugin plugin, UniversalCommand command) {
        plugin.getCommand(command.getName()).setExecutor(new BukkitCommandExecutor(command));
    }
}
