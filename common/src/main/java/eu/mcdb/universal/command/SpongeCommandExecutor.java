/*
 * Copyright (C) 2020  OopsieWoopsie
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

package eu.mcdb.universal.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.command.spec.CommandSpec.Builder;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import eu.mcdb.universal.player.UniversalPlayer;

/**
 * Wrapper for the {@link UniversalCommand} class to
 * make it usable by Sponge.
 * 
 * @author sheidy
 */
public class SpongeCommandExecutor implements CommandExecutor {

    private final UniversalCommand command;
    private final CommandSpec spec;

    public SpongeCommandExecutor(final UniversalCommand command) {
        this.command = command;

        final Builder builder = CommandSpec.builder()
                .arguments(GenericArguments.remainingJoinedStrings(Text.of("args")))
                .executor(this);

        if (command.getPermission() != null)
            builder.permission(command.getPermission());

        this.spec = builder.build();
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        UniversalCommandSender commandSender = new UniversalCommandSender() {

            @Override
            public boolean hasPermission(String permission) {
                return isEmpty(permission) || src.hasPermission(permission);
            }

            @Override
            public void sendMessage(String message) {
                src.sendMessage(Text.of(message));
            }

            private boolean isEmpty(String s) {
                return s == null || "".equals(s);
            }
        };

        if (src instanceof Player) {
            final Player player = (Player) src;

            commandSender.setPlayer(new UniversalPlayer(player.getName(), player.getUniqueId()) {

                @Override
                public Player getSpongePlayer() {
                    return player;
                }
            });
        }

        final String[] _args = args.<String>requireOne("args").split(" ");
        command.onCommand(commandSender, _args);
        return CommandResult.success();
    }

    public CommandSpec get() {
        return spec;
    }
}
