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

import lombok.Getter;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.core.utils.JDALogger;
import eu.mcdb.spicord.SpicordLoader.ServerType;
import eu.mcdb.spicord.addon.AddonManager;
import eu.mcdb.spicord.addon.PluginsAddon;
import eu.mcdb.spicord.addon.InfoAddon;
import eu.mcdb.spicord.addon.PlayersAddon;
import eu.mcdb.spicord.api.ISpicord;
import eu.mcdb.spicord.bot.DiscordBot;
import eu.mcdb.spicord.bot.DiscordBotLoader;
import eu.mcdb.spicord.config.SpicordConfiguration;
import eu.mcdb.spicord.util.CustomMap;

public class Spicord implements ISpicord {

	/**
	 * The {@link Spicord}'s instance.
	 */
	@Getter
	private static Spicord instance;

	/**
	 * The {@link Spicord}'s version
	 */
	@Getter
	private static final String version = "1.0.1-SNAPSHOT";

	/**
	 * The {@link Logger}'s instance.
	 */
	@Getter
	private Logger logger;

	/**
	 * The server type.
	 */
	@Getter
	private ServerType serverType;

	/**
	 * The Spicord configuration.
	 */
	@Getter
	private SpicordConfiguration config;

	/**
	 * The addon manager.
	 */
	@Getter
	private AddonManager addonManager;

	/**
	 * The Spicord constructor.
	 * @param logger the Plugin logger.
	 */
	public Spicord(Logger logger) {
		instance = this;
		this.logger = logger;
		this.addonManager = new AddonManager(this);
	}

	protected void onLoad(SpicordLoader loader) {
		this.serverType = loader.getServerType();
		this.config = new SpicordConfiguration(serverType);
		loader.extractLibraries(this.config);
		loader.loadLibraries();
		if (!isLoaded()) return;
		this.registerIntegratedAddons();
		if (!this.config.isJdaMessagesEnabled()) {
			try {
				debug("Disabling JDA's messages...");
				PrintStream err = System.err;
				System.setErr(new PrintStream(err) {

					@Override
					public void println(String x) {
						if (!x.startsWith("SLF4J"))
							super.println(x);
					}
				});
				setAccessible(JDALogger.class.getDeclaredField("SLF4J_ENABLED")).set(null, false);
	            setAccessible(JDALogger.class.getDeclaredField("LOGS")).set(null, new CustomMap<String, Logger>());
				debug("Successfully disabled JDA's messages.");
			} catch (Exception e) {
				getLogger().log(Level.SEVERE, "An error ocurred while disabling JDA's messages. ", e);
			}
		}
		getLogger().info("Starting the bots...");
		config.getBots().forEach(this::startBot);
	}

	private void registerIntegratedAddons() {
		this.getAddonManager().registerAddon(new InfoAddon());
		this.getAddonManager().registerAddon(new PluginsAddon());
		this.getAddonManager().registerAddon(new PlayersAddon());
	}

	private Field setAccessible(Field field) {
		try {
			field.setAccessible(true);
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			return field;
		} catch (Exception e) {
			return field;
		}
	}

	protected void onDisable() {
		getLogger().info("Disabling Spicord...");
		this.config.getBots().forEach(this::shutdownBot);
		this.config.getBots().clear();
		this.addonManager.getAddons().clear();
		this.serverType = null;
		this.logger = null;
		this.config = null;
		this.addonManager = null;
		instance = null;
	}

	public boolean startBot(DiscordBot bot) {
		if (bot.isEnabled()) {
			try {
				getLogger().info("Starting bot '" + bot.getName() + "'.");
				return DiscordBotLoader.startBot(bot);
			} catch (Exception ex) {
				getLogger().log(Level.SEVERE, "An error occurred while starting the bot '" + bot.getName() + "'. ", ex);
			}
		} else {
			getLogger().warning("Bot '" + bot.getName() + "' is disabled. Skipping.");
		}
		return false;
	}

	/**
	 * Shutdown a bot if it is enabled.
	 * @param bot the bot object.
	 */
	public void shutdownBot(DiscordBot bot) {
		if (bot.isEnabled() && bot.getJda() != null)
			bot.getJda().shutdownNow();
	}

	/**
	 * Get a bot by its name.
	 * @param name the bot's name.
	 * @return the {@link DiscordBot} object if the bot exists, or null if not.
	 */
	public DiscordBot getBotByName(String name) {
		for (DiscordBot bot : config.getBots()) {
			if (bot.getName().equals(name)) return bot;
		}
		return null;
	}

	/**
	 * Check if Spicord was loaded.
	 * @return true if Spicord is loaded, or false if not.
	 */
	public static boolean isLoaded() {
		return instance == null ? false : true;
	}

	/**
	 * Displays a message if the debug mode if enabled.
	 * @param msg the message to be displayed.
	 */
	public void debug(String msg) {
		if (config.isDebugEnabled())
			getLogger().info("[DEBUG] " + msg);
	}
}