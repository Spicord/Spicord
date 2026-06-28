package eu.mcdb.universal.command.api;

import java.util.concurrent.CompletableFuture;
import eu.mcdb.universal.command.UniversalCommandSender;

@FunctionalInterface
public interface AsyncCommandHandler extends CommandHandler {

    boolean handleAsync(UniversalCommandSender sender, CommandParameters parameters);

    default boolean handle(UniversalCommandSender sender, CommandParameters parameters) {
        CompletableFuture.runAsync(() -> handleAsync(sender, parameters));
        return true;
    }
}
