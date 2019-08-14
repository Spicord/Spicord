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

package eu.mcdb.spicord.bot;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import com.google.common.base.Preconditions;
import eu.mcdb.spicord.Spicord;
import eu.mcdb.spicord.api.addon.SimpleAddon;
import eu.mcdb.spicord.api.bot.SimpleBot;
import eu.mcdb.spicord.api.bot.command.BotCommand;
import eu.mcdb.spicord.bot.command.DiscordBotCommand;
import lombok.Getter;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDA.Status;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.DisconnectEvent;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.StatusChangeEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class DiscordBot extends SimpleBot {

    /**
     * If the bot is enabled on the config, this will be true.
     */
    private final boolean enabled;

    /**
     * The JDA instance.
     */
    @Getter
    private JDA jda;

    /**
     * The addons key of the addons which this bot use.
     */
    @Getter
    private final Collection<String> addons;

    /**
     * The 'command-support' flag.
     */
    @Getter
    private boolean commandSupportEnabled;

    /**
     * The bot's command prefix. (ex. "-" or "!").
     */
    @Getter
    private final String commandPrefix;

    /**
     * The commands provided by the addons (to THIS bot).
     */
    @Getter
    private final Map<String, Consumer<DiscordBotCommand>> commands;

    /**
     * If the bot is running, the value will be 'true'.
     */
    @Getter
    protected boolean ready;

    /**
     * The Spicord instance
     */
    private final Spicord spicord;

    public DiscordBot(String name, String token, boolean enabled, List<String> addons, boolean commandSupportEnabled,
            String commandPrefix) {
        super(name, token);

        this.enabled = enabled;
        this.addons = Collections.unmodifiableCollection(addons);
        this.commandSupportEnabled = commandSupportEnabled;
        this.commandPrefix = commandPrefix.trim();
        this.ready = false;
        this.commands = Collections.synchronizedMap(new HashMap<String, Consumer<DiscordBotCommand>>());
        this.spicord = Spicord.getInstance();

        if (commandSupportEnabled) {
            if (commandPrefix.isEmpty()) {
                this.commandSupportEnabled = false;

                spicord.getLogger().severe(
                        "The command prefix cannot be empty. The command-support feature is now disabled on bot '"
                                + name + "'.");
            }
        }
    }

    @Override
    protected boolean startBot() {
        try {
            this.jda = new JDABuilder(AccountType.BOT).setToken(token).build();

            jda.addEventListener(new ListenerAdapter() {

                @Override
                public void onReady(ReadyEvent event) {
                    DiscordBot.this.ready = true;
                }

                @Override
                public void onStatusChange(StatusChangeEvent event) {
                    if (event.getNewStatus() == Status.SHUTDOWN)
                        DiscordBot.this.ready = false;
                }

                @Override
                public void onDisconnect(DisconnectEvent event) {
                    DiscordBot.this.ready = false;
                }
            });

            if (commandSupportEnabled) {
                jda.addEventListener(new ListenerAdapter() {

                    @Override
                    public void onMessageReceived(MessageReceivedEvent event) {
                        String messageContent = event.getMessage().getContentRaw();

                        if (messageContent.startsWith(commandPrefix)) {
                            messageContent = messageContent.substring(commandPrefix.length());

                            if (messageContent.length() != 0) {
                                String command = messageContent.split(" ")[0];
                                String[] args = messageContent.contains(" ")
                                        ? messageContent.substring(command.length() + 1).split(" ")
                                        : new String[0];

                                if (commands.containsKey(command)) {
                                    commands.get(command).accept(new DiscordBotCommand(args, event.getMessage()));
                                }
                            }
                        }
                    }
                });
            }

            spicord.getAddonManager().loadAddons(this);
            return true;
        } catch (Exception e) {
            spicord.getLogger()
                    .severe("An error ocurred while starting the bot '" + getName() + "'. " + e.getMessage());
        }

        return false;
    }

    /**
     * @param name    the command name without a prefix
     * @param command the action that will be performed when the command is executed
     */
    public void onCommand(String name, Consumer<DiscordBotCommand> command) {
        Preconditions.checkNotNull(name, "name");
        Preconditions.checkNotNull(command, "command");
        Preconditions.checkArgument(!name.trim().isEmpty(), "The command name cannot be empty.");
        Preconditions.checkArgument(!name.trim().contains(" "), "The command name cannot contain spaces (' ').");

        if (commandSupportEnabled) {
            if (commands.containsKey(name)) {
                spicord.getLogger()
                        .warning("The command '" + name + "' is already registered on bot '" + getName() + "'.");
            } else {
                commands.put(name, command);
            }
        } else {
            spicord.getLogger().warning("Cannot register command '" + name + "' on bot '" + getName()
                    + "' because the 'command-support' option is disabled.");
        }
    }

    public void onCommand(String name, BotCommand command) {
        this.onCommand(name, (comm) -> command.onCommand(comm, comm.getArguments()));
    }

    public void registerCommand(String name, BotCommand command) {
        this.onCommand(name, command);
    }

    /**
     * Loads an addon to this bot.
     * 
     * @param addon the addon to be loaded
     */
    public void loadAddon(SimpleAddon addon) {
        addon.onLoad(this);
    }

    /**
     * @return true if the bot is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled the value to set
     * @deprecated The value is final and cannot be changed.
     */
    @Deprecated
    protected void setEnabled(boolean enabled) {
        throw new IllegalStateException("The value is final and cannot be changed.");
    }

    /**
     * @param disabled the value to set
     * @deprecated The value is final and cannot be changed.
     */
    @Deprecated
    public void setDisabled(boolean disabled) {
        throw new IllegalStateException("The value is final and cannot be changed.");
    }

    /**
     * @return true if the bot is disabled
     */
    public boolean isDisabled() {
        return !enabled;
    }
}
