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

package eu.mcdb.spicord.util;

import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import eu.mcdb.spicord.Spicord;
import eu.mcdb.spicord.SpicordLoader.ServerType;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.PluginDescription;

public class Server {

	/**
	 * @return the amount of online players on the server
	 */
	public static int getOnlineCount() {
		if (Spicord.isLoaded()) {
			if (Spicord.getInstance().getServerType() == ServerType.BUKKIT) {
				return Bukkit.getServer().getOnlinePlayers().size();
			} else if (Spicord.getInstance().getServerType() == ServerType.BUNGEECORD) {
				return ProxyServer.getInstance().getOnlineCount();
			}
		}
		return 0;
	}

	/**
	 * @deprecated As of snapshot 1.0.2, replaced by {@link #getPlayerLimit()}
	 * @return the player limit of the server
	 */
	@Deprecated
	public static int getMaxOnlineCount() {
		return getPlayerLimit();
	}

	/**
	 * @return the player limit of the server
	 */
	public static int getPlayerLimit() {
		if (Spicord.isLoaded()) {
			if (Spicord.getInstance().getServerType() == ServerType.BUKKIT) {
				return Bukkit.getServer().getMaxPlayers();
			} else if (Spicord.getInstance().getServerType() == ServerType.BUNGEECORD) {
				return BungeeCord.getInstance().getConfig().getPlayerLimit();
			}
		}
		return 0;
	}

	/**
	 * @return the names of the online players
	 */
	public static String[] getOnlinePlayers() {
		if (Spicord.isLoaded()) {
			if (Spicord.getInstance().getServerType() == ServerType.BUKKIT) {
				return Bukkit.getServer().getOnlinePlayers().stream()
						.map(Player::getName)
						.toArray(String[]::new);
			} else if (Spicord.getInstance().getServerType() == ServerType.BUNGEECORD) {
				return ProxyServer.getInstance().getPlayers().stream()
						.map(ProxiedPlayer::getName)
						.toArray(String[]::new);
			}
		}
		return new String[0];
	}

	/**
	 * @return the server version
	 */
	public static String getServerVersion() {
		if (Spicord.isLoaded()) {
			if (Spicord.getInstance().getServerType() == ServerType.BUKKIT) {
				return Bukkit.getServer().getVersion();
			} else if (Spicord.getInstance().getServerType() == ServerType.BUNGEECORD) {
				return ProxyServer.getInstance().getVersion();
			}
		}
		return "unknown";
	}

	/**
	 * @return the name of the installed plugins
	 */
	public static String[] getPlugins() {
		if (Spicord.isLoaded()) {
			if (Spicord.getInstance().getServerType() == ServerType.BUKKIT) {
				return Stream.of(Bukkit.getServer().getPluginManager().getPlugins())
					.map(Plugin::getName).toArray(String[]::new);
			} else if (Spicord.getInstance().getServerType() == ServerType.BUNGEECORD) {
				return ProxyServer.getInstance().getPluginManager().getPlugins().stream()
					.map(net.md_5.bungee.api.plugin.Plugin::getDescription)
					.map(PluginDescription::getName).toArray(String[]::new);
			}
		}
		return new String[0];
	}

	/**
	 * @return the Spicord's version
	 */
	public static String getSpicordVersion() {
		return Spicord.getVersion();
	}
}