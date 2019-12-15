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

package eu.mcdb.util;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public interface IServer {

    /**
     * Get the amount of online players.
     * 
     * @return the amount of online players on the server
     */
    int getOnlineCount();

    /**
     * Get the max amount of players that can join the server.
     * 
     * @return the player limit of the server
     */
    int getPlayerLimit();

    /**
     * Get the names of the connected players.
     * 
     * @return the names of the online players
     */
    String[] getOnlinePlayers();

    /**
     * Get the players connected to each server in case of
     * using BungeeCord.
     * When using bukkit the only map key will be "default"
     * and it will contain all the online players, the
     * result is the same as {@link #getOnlinePlayers()}.
     * 
     * @return the list of online players on each server
     */
    Map<String, List<String>> getServersAndPlayers();

    /**
     * Get the server version.
     * 
     * @return the server version
     */
    String getVersion();

    /**
     * Get the names of the installed plugins.
     * 
     * @return the names of the installed plugins
     */
    String[] getPlugins();

    /**
     * Dispatch a server command from the console.
     * 
     * @param command the command to be executed
     * @return true if the execution result was successfully
     */
    boolean dispatchCommand(String command);

    /**
     * Bukkit flag.
     * 
     * @return true if running a Bukkit-based server
     */
    default boolean isBukkit() {
        return false;
    }

    /**
     * Bungee flag.
     * 
     * @return true if running a BungeeCord-based server
     */
    default boolean isBungeeCord() {
        return false;
    }

    /**
     * Get the server logger.
     * 
     * @return the logger
     */
    Logger getLogger();
}
