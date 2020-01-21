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

package eu.mcdb.spicord.bot;

import java.util.function.Supplier;
import eu.mcdb.spicord.api.addon.SimpleAddon;
import eu.mcdb.spicord.bot.command.DiscordBotCommand;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

@RequiredArgsConstructor
public class BotCommandListener extends ListenerAdapter {

    private final DiscordBot bot;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        bot.onMessageReceived(event);

        String commandPrefix = bot.getCommandPrefix();
        String messageContent = event.getMessage().getContentRaw();

        if (messageContent.startsWith(commandPrefix)) {
            messageContent = messageContent.substring(commandPrefix.length());

            if (!messageContent.isEmpty()) {
                String commandName = messageContent.split(" ")[0];
                String[] args = messageContent.contains(" ")
                        ? messageContent.substring(commandName.length() + 1).split(" ")
                        : new String[0];

                // the command instance will only be created if the get() method is called
                Supplier<DiscordBotCommand> commandSupplier = () -> new DiscordBotCommand(commandName, args, event.getMessage());

                if (bot.commands.containsKey(commandName)) {
                    bot.commands.get(commandName).accept(commandSupplier.get());
                } else {
                    for (SimpleAddon addon : bot.loadedAddons) {
                        for (String cmd : addon.getCommands()) {
                            if (cmd.equals(commandName)) {
                                addon.onCommand(commandSupplier.get(), args);
                            }
                        }
                    }
                }
            }
        }
    }
}
