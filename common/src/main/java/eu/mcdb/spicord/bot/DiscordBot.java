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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.security.auth.login.LoginException;
import com.google.common.base.Preconditions;
import eu.mcdb.spicord.Spicord;
import eu.mcdb.spicord.api.Node;
import eu.mcdb.spicord.api.addon.SimpleAddon;
import eu.mcdb.spicord.api.bot.SimpleBot;
import eu.mcdb.spicord.api.bot.command.BotCommand;
import eu.mcdb.spicord.bot.command.DiscordBotCommand;
import lombok.Getter;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class DiscordBot extends SimpleBot implements Node {

    /**
     * If the bot is enabled on the config, this will be true.
     */
    private final boolean enabled;

    /**
     * The JDA instance.
     */
    @Getter
    protected JDA jda;

    /**
     * The addons key of the addons which this bot use.
     */
    @Getter
    private final Collection<String> addons;

    protected final Collection<SimpleAddon> loadedAddons;

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
    protected final Map<String, Consumer<DiscordBotCommand>> commands;

    @Getter
    protected BotStatus status;

    private final Spicord spicord;

    public DiscordBot(String name, String token, boolean enabled, List<String> addons, boolean commandSupportEnabled,
            String prefix) {
        super(name, token);

        this.enabled = enabled;
        this.addons = Collections.unmodifiableCollection(addons);
        this.loadedAddons = new ArrayList<SimpleAddon>();
        this.commandSupportEnabled = commandSupportEnabled;
        this.commandPrefix = prefix.trim();
        this.commands = new HashMap<String, Consumer<DiscordBotCommand>>();
        this.spicord = getSpicord();
        this.status = BotStatus.OFFLINE;

        if (commandSupportEnabled) {
            if (prefix.isEmpty()) {
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
            this.jda = new JDABuilder(AccountType.BOT).setToken(token).setAutoReconnect(true).build();

            jda.addEventListener(new BotStatusListener(this));

            if (commandSupportEnabled)
                jda.addEventListener(new BotCommandListener(this));

            spicord.getAddonManager().loadAddons(this);
            return true;
        } catch (LoginException e) {
            this.status = BotStatus.OFFLINE;
            spicord.getLogger()
                    .severe("An error ocurred while starting the bot '" + getName() + "'. " + e.getMessage());
        }

        return false;
    }

    public void onReady(ReadyEvent event) {
        loadedAddons.forEach(addon -> addon.onReady(this));
    }

    public void onMessageReceived(MessageReceivedEvent event) {
        loadedAddons.forEach(addon -> addon.onMessageReceived(this, event));
    }

    /**
     * Add an event listener to this bot.
     * 
     * @param listener the event listener
     */
    public void addEventListener(ListenerAdapter listener) {
        jda.addEventListener(listener);
    }

    /**
     * Register a command for this bot.
     * 
     * @param name    the command name (without prefix)
     * @param command the action to be performed when the command is executed
     * 
     * @throws NullPointerException if one of the arguments is null
     * @throws IllegalArgumentException if the {@code name} is empty or contains spaces
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

    /**
     * Register a command for this bot.
     * 
     * @param name    the command name (without prefix)
     * @param command the action to be performed when the command is executed
     * 
     * @throws NullPointerException if one of the arguments is null
     * @throws IllegalArgumentException if the {@code name} is empty or contains spaces
     */
    public void onCommand(String name, BotCommand command) {
        this.onCommand(name, comm -> command.onCommand(comm, comm.getArguments()));
    }

    /**
     * Register a command for this bot.
     * Alias of {@link #onCommand(String, BotCommand)}.
     * 
     * @param name    the command name (without prefix)
     * @param command the action to be performed when the command is executed
     * 
     * @throws NullPointerException if one of the arguments is null
     * @throws IllegalArgumentException if the {@code name} is empty or contains spaces
     */
    public void registerCommand(String name, BotCommand command) {
        this.onCommand(name, command);
    }

    /**
     * Load an addon to this bot.
     * 
     * @param addon the addon to be loaded
     */
    public void loadAddon(SimpleAddon addon) {
        loadedAddons.add(addon);
        addon.onLoad(this);
    }

    /**
     * Check if the bot is enabled..
     * 
     * @see #isDisabled()
     * @return true if the bot is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Check if the bot is disabled.
     * 
     * @see #isEnabled()
     * @return true if the bot is disabled
     */
    public boolean isDisabled() {
        return !enabled;
    }

    /**
     * Check if the bot has started and is running.
     * 
     * @return true if the bot is running
     */
    public boolean isReady() {
        return status == BotStatus.READY;
    }

    public enum BotStatus {
        READY, OFFLINE, STARTING, STOPPING, UNKNOWN
    }
}
