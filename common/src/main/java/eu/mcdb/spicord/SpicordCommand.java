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

import eu.mcdb.spicord.api.addon.SimpleAddon;
import eu.mcdb.spicord.bot.DiscordBot;
import eu.mcdb.spicord.bot.DiscordBot.BotStatus;
import eu.mcdb.spicord.bot.DiscordBotLoader;
import eu.mcdb.universal.command.UniversalCommandSender;
import eu.mcdb.universal.command.api.Command;
import eu.mcdb.universal.command.api.CommandParameter;
import eu.mcdb.universal.command.api.CommandParameters;

public final class SpicordCommand extends Command {

    private Spicord spicord;
    private final Runnable reloadAction;

    public SpicordCommand(Runnable reload) {
        super("spicord", null, new String[] { "sp" });
        this.spicord = Spicord.getInstance();
        this.reloadAction = reload;

        setCommandHandler(sender -> {
            sender.sendFormattedMessage("&7&l[&a&lSpicord&7&l] &fRunning Spicord %s by OopsieWoopsie", Spicord.getVersion());
            return true;
        });

        // bot command
        Command bot = new Command("bot", "spicord.admin.bot");
        bot.setParameter(0, new CommandParameter("botname"));
        bot.setParameter(1, new CommandParameter("action", "add/remove", false));
        bot.setParameter(2, new CommandParameter("id", "addon-id", false));
        bot.setCommandHandler(this::handleBot);

        // stop command
        Command stop = new Command("stop", "spicord.admin.stop");
        stop.setParameter(0, new CommandParameter("botname", true));
        stop.setCommandHandler(this::handleStop);

        // start command
        Command start = new Command("start", "spicord.admin.start");
        start.setParameter(0, new CommandParameter("botname", true));
        start.setCommandHandler(this::handleStart);

        // restart command
        Command restart = new Command("restart", "spicord.admin.restart");
        restart.setCommandHandler(this::handleRestart);

        // status command
        Command status = new Command("status", "spicord.admin.status");
        status.setCommandHandler(this::handleStatus);

        addSubCommand(bot);
        addSubCommand(stop);
        addSubCommand(start);
        addSubCommand(restart);
        addSubCommand(status);
    }

    private boolean handleBot(UniversalCommandSender sender, CommandParameters params) {
        String botname = params.getValue("botname");
        String action = params.getValue("action");
        String id = params.getValue("id");

        if (action.equals("add") || action.equals("remove")) {
            DiscordBot bot = spicord.getBotByName(botname);

            if (bot != null) {
                SimpleAddon addon = spicord.getAddonManager().getAddonById(id);

                if (addon != null) {
                    if (action.equals("add")) {
                        sender.sendFormattedMessage("&eAdded the addon '%s' to bot '%s'", addon.getName(), bot.getName());
                        spicord.getConfig().getManager().addAddonToBot(addon.getId(), bot.getName());
                    } else {
                        sender.sendFormattedMessage("&eRemoved the addon '%s' from the bot '%s'", addon.getName(), bot.getName());
                        spicord.getConfig().getManager().removeAddonFromBot(addon.getId(), bot.getName());
                    }
                    sender.sendFormattedMessage("&aDo &6/spicord restart &ato apply the changes");
                    return true;
                } else {
                    sender.sendFormattedMessage("&cCannot find the addon '%s'", id);
                    //sender.sendFormattedMessage("&aExecute &6/sp confirm &ato force the removal"); - soon
                }
            } else sender.sendFormattedMessage("&cCannot find the bot '%s'", botname);
        } else sender.sendFormattedMessage("&cInvalid action '%s', use 'add' or 'remove'", action);

        return true;
    }

    private boolean handleStop(UniversalCommandSender sender, CommandParameters params) {
        String botname = params.getOptionalValue("botname").orElse("default");

        DiscordBot bot = spicord.getBotByName(botname);

        if (bot == null) {
            sender.sendFormattedMessage("&cCannot find the bot '%s'", botname);
        } else {
            if (bot.getStatus() == BotStatus.READY) {
                DiscordBotLoader.shutdownBot(bot);
            } else {
                sender.sendFormattedMessage("&7The bot is not online and cannot be stopped");
            }
        }

        return true;
    }

    private boolean handleStart(UniversalCommandSender sender, CommandParameters params) {
        String botname = params.getOptionalValue("botname").orElse("default");
        DiscordBot bot = spicord.getBotByName(botname);

        if (bot == null) {
            sender.sendFormattedMessage("&cCannot find the bot '%s'", botname);
        } else if (bot.isReady()) {
            sender.sendFormattedMessage("&cThe bot has already started");
        } else {
            if (DiscordBotLoader.startBot(bot)) {
                sender.sendFormattedMessage("&aThe bot '%s' is being started", bot.getName());
            } else {
                sender.sendFormattedMessage("&cAn error ocurred while starting the bot '%s'", bot.getName());
            }
        }

        return true;
    }

    private boolean handleRestart(UniversalCommandSender sender) {
        sender.sendFormattedMessage("&cSpicord is being restarted, please wait...");
        reloadAction.run();
        this.spicord = Spicord.getInstance();
        sender.sendFormattedMessage("&aSpicord has been restarted!");
        return true;
    }

    private boolean handleStatus(UniversalCommandSender sender) {
        sender.sendFormattedMessage("&7&l[&a&lSpicord&7&l] &f> Status");
        for (DiscordBot bot : spicord.getConfig().getBots()) {
            sender.sendFormattedMessage(" &7- %s [&e%s&7]", bot.getName(), bot.getStatus().toString());
        }
        sender.sendFormattedMessage("&7&l[&a&lSpicord&7&l] &f--------");
        return true;
    }
}
