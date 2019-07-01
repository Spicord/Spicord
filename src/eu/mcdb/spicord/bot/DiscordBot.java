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
	private boolean enabled;

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
	private boolean ready;

	public DiscordBot(String name, String token, boolean enabled, List<String> addons, boolean commandSupportEnabled, String commandPrefix) {
		super(name, token);
		this.enabled = enabled;
		this.addons = Collections.unmodifiableCollection(addons);
		this.commandSupportEnabled = commandSupportEnabled;
		this.commandPrefix = commandPrefix;
		this.ready = false;
		this.commands = Collections.synchronizedMap(new HashMap<>());
		if (commandSupportEnabled) {
			if (commandPrefix.length() == 0) {
				Spicord.getInstance().getLogger().severe("The command prefix cannot be empty. The command-support feature is now disabled on bot '" + name + "'.");
				this.commandSupportEnabled = false;
			}
		}
	}

	@Override
	protected DiscordBot startBot() {
		this.enabled = true;
		try {
			this.jda = new JDABuilder(AccountType.BOT).setToken(getToken()).build();
			jda.addEventListener(new ListenerAdapter() {

				@Override
				public void onReady(ReadyEvent event) {
					DiscordBot.this.ready = true;
					System.out.println("The bot '" + DiscordBot.this.getName() + "' has started without errors.");
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
			if (isCommandSupportEnabled()) {
				jda.addEventListener(new ListenerAdapter() {

					@Override
					public void onMessageReceived(MessageReceivedEvent event) {
						String messageContent = event.getMessage().getContentRaw();
						if (messageContent.startsWith(getCommandPrefix())) {
							messageContent = messageContent.substring(getCommandPrefix().length());
							if (messageContent.length() != 0) {
								String command = messageContent.split(" ")[0];
								messageContent = messageContent.contains(" ") ? messageContent.substring(command.length() + 1) : "";
								if (commands.containsKey(command)) {
									commands.get(command).accept(new DiscordBotCommand(messageContent.split(" "), event));
								}
							}
						}
					}
				});
			}
			Spicord.getInstance().getAddonManager().loadAddons(this);
		} catch (Exception e) {
			Spicord.getInstance().getLogger().severe("An error ocurred while enabling the bot '" + getName() + "'. " + e.getMessage());
		}
		return this;
	}

	/**
	 * @param name the command name without a prefix
	 * @param command the action that will be performed when the command is executed
	 */
	public void onCommand(String name, Consumer<DiscordBotCommand> command) {
		Preconditions.checkNotNull(name);
		Preconditions.checkNotNull(command);
		Preconditions.checkArgument(!name.trim().isEmpty(), "The command name cannot be empty.");
		Preconditions.checkArgument(!name.trim().contains(" "), "The command name cannot contain spaces (' ').");

		if (isCommandSupportEnabled()) {
			if (commands.containsKey(name)) {
				Spicord.getInstance().getLogger().warning("The command '" + name + "' is already registered on bot '" + getName() + "'.");
			} else {
				commands.put(name, command);
			}
		} else {
			Spicord.getInstance().getLogger().warning("Cannot register command '" + name + "' on bot '" + getName() + "' because the 'command-support' option is disabled.");
		}
	}

	/**
	 * Loads an addon to this bot.
	 * @param addon the addon to be loaded
	 */
	public void loadAddon(SimpleAddon addon) {
		addon.onLoad(this);
	}

	/**
	 * @param enabled the value to set
	 */
	protected void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return true if the bot is enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}
}