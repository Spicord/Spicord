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
     * Gets the amount of online players.
     * 
     * @return the amount of online players on the server.
     */
    int getOnlineCount();

    /**
     * Gets the max amount of players that can join the server.
     * 
     * @return the player limit of the server.
     */
    int getPlayerLimit();

    /**
     * Gets the names of the connected players.
     * 
     * @return the names of the online players.
     */
    String[] getOnlinePlayers();

    Map<String, List<String>> getServersAndPlayers();

    /**
     * Gets the server version.
     * 
     * @return the server version.
     */
    String getVersion();

    /**
     * Gets the names of the installed plugins.
     * 
     * @return the names of the installed plugins.
     */
    String[] getPlugins();

    boolean dispatchCommand(String command);

    /**
     * Bukkit flag.
     * 
     * @return true if running a Bukkit-based server.
     */
    default boolean isBukkit() {
        return false;
    }

    /**
     * Bungee flag.
     * 
     * @return true if running a BungeeCord-based server.
     */
    default boolean isBungeeCord() {
        return false;
    }

    Logger getLogger();
}
