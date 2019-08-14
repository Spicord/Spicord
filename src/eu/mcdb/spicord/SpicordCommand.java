/*
 * Copyright (C) 2019  OopsieWoopsie
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

package eu.mcdb.spicord;

import eu.mcdb.spicord.bot.DiscordBot;
import eu.mcdb.spicord.bot.DiscordBotLoader;
import eu.mcdb.universal.command.UniversalCommand;
import eu.mcdb.universal.command.UniversalCommandSender;

public class SpicordCommand extends UniversalCommand {

    private final Spicord spicord;

    public SpicordCommand() {
        super("spicord", null, new String[] { "sp" });
        this.spicord = Spicord.getInstance();
    }

    @Override
    public boolean onCommand(UniversalCommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendFormattedMessage("&7&l[&a&lSpicord&7&l] &fRunning Spicord " + Spicord.getVersion() + " by OopsieWoopsie");
        } else if (args.length == 1) {
            switch (args[0]) {
            case "status":
                if (sender.hasPermission("spicord.status")) {
                    sender.sendFormattedMessage("&7&l[&a&lSpicord&7&l] &f> Status");
                    for (DiscordBot bot : spicord.getConfig().getBots()) {
                        sender.sendFormattedMessage(" &7- " + bot.getName() + " [" + (bot.isReady() ? "&aReady" : (bot.isEnabled() ? "&cOffline" : "&cDisabled")) + "&7]");
                    }
                    sender.sendFormattedMessage("&7&l[&a&lSpicord&7&l] &f--------");
                } else {
                    sender.sendFormattedMessage("&4You do not have permission to run this command.");
                }
            }
        } else if (args.length == 2) {
            switch (args[0]) {
            case "start":
                if (sender.hasPermission("spicord.admin.start")) {
                    String name = args[1];
                    DiscordBot bot = spicord.getBotByName(name);
                    if (bot == null) {
                        sender.sendFormattedMessage("&cCannot find the bot '" + name + "'.");
                    } else if (bot.isReady()) {
                        sender.sendFormattedMessage("&cThe bot '" + name + "' has already started.");
                    } else {
                        sender.sendFormattedMessage("&eStarting the bot '" + name + "'...");
                        if (DiscordBotLoader.startBot(bot)) {
                            sender.sendFormattedMessage("&aThe bot '" + name + "' is ready.");
                        } else {
                            sender.sendFormattedMessage("&cAn error ocurred while starting the bot '" + name + "'.");
                        }
                    }
                } else {
                    sender.sendFormattedMessage("&4You do not have permission to run this command.");
                }
                break;
            case "stop":
                if (sender.hasPermission("spicord.admin.stop")) {
                    String name = args[1];
                    DiscordBot bot = Spicord.getInstance().getBotByName(name);
                    if (bot == null) {
                        sender.sendFormattedMessage("&cCannot find the bot '" + name + "'.");
                        break;
                    }
                    if (bot.isEnabled()) {
                        DiscordBotLoader.shutdownBot(bot);
                    } else {
                        sender.sendFormattedMessage("&7The bot '" + name + "' is disabled.");
                    }
                } else {
                    sender.sendFormattedMessage("&4You do not have permission to run this command.");
                }
            }
        }
        return true;
    }
}
