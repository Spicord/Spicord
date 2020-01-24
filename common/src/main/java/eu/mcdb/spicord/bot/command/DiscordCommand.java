package eu.mcdb.spicord.bot.command;

import eu.mcdb.spicord.api.bot.command.BotCommand;
import eu.mcdb.universal.command.api.Command;
import net.dv8tion.jda.core.Permission;

public class DiscordCommand extends Command implements BotCommand {

    public DiscordCommand(String name) {
        super(name);
    }

    public DiscordCommand(String name, Permission permission) {
        super(name, toString(permission));
    }

    public DiscordCommand(String name, Permission permission, String[] aliases) {
        super(name, toString(permission), aliases);
    }

    public void setCommandHandler(DiscordCommandHandler commandHandler) {
        super.setCommandHandler(commandHandler);
    }

    public void setCommandHandler(UnparametrizedDiscordCommandHandler commandHandler) {
        super.setCommandHandler(commandHandler);
    }

    public void addSubCommand(String name, DiscordCommandHandler handler) {
        super.addSubCommand(name, handler);
    }

    public void addSubCommand(String name, String permission, DiscordCommandHandler handler) {
        super.addSubCommand(name, permission, handler);
    }

    @Override
    public final void onCommand(DiscordBotCommand command, String[] args) {
        final DiscordCommandSender sender = new DiscordCommandSender(command);
        super.onCommand(sender, args);
    }

    private static String toString(Permission permission) {
        return permission == null ? null : permission.toString();
    }
}
