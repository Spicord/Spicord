package eu.mcdb.universal.command.api;

import eu.mcdb.universal.command.UniversalCommandSender;

@FunctionalInterface
public interface UnparametrizedCommandHandler extends CommandHandler {

    boolean handle(UniversalCommandSender sender);

    @Override
    default boolean handle(UniversalCommandSender sender, CommandParameters parameters) {
        return handle(sender);
    }
}
