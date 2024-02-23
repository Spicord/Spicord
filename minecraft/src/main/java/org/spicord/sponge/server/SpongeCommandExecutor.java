package org.spicord.sponge.server;

import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.registrar.CommandRegistrar;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.plugin.PluginContainer;

import eu.mcdb.universal.command.UniversalCommand;
import eu.mcdb.universal.command.UniversalCommandSender;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;

public class SpongeCommandExecutor implements CommandExecutor {

    private final UniversalCommand command;

    private Command.Parameterized spongeCommand;

    private Parameter.Value<String> argsParameter;

    public SpongeCommandExecutor(final UniversalCommand command) {
        this.command = command;

        final Command.Builder builder = Command.builder()
                .addParameter(
                        argsParameter = Parameter.remainingJoinedStrings()
                            .key("args")
                            .consumeAllRemaining()
                            .build()
                )
                .executor(this);

        if (command.getPermission() != null) {
            builder.permission(command.getPermission());
        }

        spongeCommand = builder.build();
    }

    @Override
    public CommandResult execute(CommandContext context) throws CommandException {
        final UniversalCommandSender commandSender;

        if (context.subject() instanceof ServerPlayer) {
            commandSender = new SpongePlayer((ServerPlayer) context.subject());
        } else {
            commandSender = new UniversalCommandSender() {

                @Override
                public boolean hasPermission(String permission) {
                    return isEmpty(permission) || context.hasPermission(permission);
                }

                @Override
                public void sendMessage(String message) {
                    context.sendMessage(Identity.nil(), Component.text(message));
                }

                private boolean isEmpty(String string) {
                    return string == null || string.isEmpty();
                }
            };
        }

        final String[] args = context.requireOne(argsParameter).split(" ");

        command.onCommand(commandSender, args);

        return CommandResult.success();
    }

    public Command.Parameterized getSpongeCommand() {
        return spongeCommand;
    }

    public static void register(Object pluginInstance, UniversalCommand command) {
        final Optional<PluginContainer> container = Sponge.game().pluginManager().fromInstance(pluginInstance);

        if (container.isPresent()) {
            final Optional<CommandRegistrar<Parameterized>> registrar = Sponge.server().commandManager().registrar(Command.Parameterized.class);

            if (registrar.isPresent()) {
                final SpongeCommandExecutor executor = new SpongeCommandExecutor(command);

                registrar.get().register(container.get(), executor.getSpongeCommand(), command.getName(), command.getAliases());
            }
        }
    }
}
