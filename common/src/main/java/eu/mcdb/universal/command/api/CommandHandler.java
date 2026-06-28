package eu.mcdb.universal.command.api;

import eu.mcdb.universal.command.UniversalCommandSender;

@FunctionalInterface
public interface CommandHandler {

    boolean handle(UniversalCommandSender sender, CommandParameters parameters);
}
