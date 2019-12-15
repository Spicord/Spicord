package eu.mcdb.universal.command;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.text.TextComponent;

public final class VelocityCommandExecutor implements Command {

    private final UniversalCommand command;

    public VelocityCommandExecutor(UniversalCommand command) {
        this.command = command;
    }

    @Override
    public void execute(CommandSource source, String[] args) {
        UniversalCommandSender commandSender = new UniversalCommandSender() {

            @Override
            public boolean hasPermission(String permission) {
                return isEmpty(permission) || source.hasPermission(permission);
            }

            @Override
            public void sendMessage(String message) {
                source.sendMessage(TextComponent.of(message));
            }

            private boolean isEmpty(String s) {
                return s == null || "".equals(s);
            }
        };

        command.onCommand(commandSender, args);
    }
}
