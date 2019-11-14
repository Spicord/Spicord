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

import static eu.mcdb.util.ReflectionUtils.classExists;
import lombok.Getter;
import lombok.Setter;

/**
 * This class provides methods that can be accesed no matter what server
 * software are you using.
 * 
 * @author OopsieWoopsie
 * @version 1.0
 */
public abstract class Server implements IServer {

    /**
     * The server type.
     */
    @Getter
    private static ServerType serverType;

    @Getter
    private static Server instance;

    @Getter
    @Setter
    private boolean debugEnabled = false;

    static {
        if (classExists("net.md_5.bungee.BungeeCord")) {
            serverType = ServerType.BUNGEECORD;
            instance = new BungeeServer();
        } else if (classExists("org.bukkit.Bukkit")) {
            serverType = ServerType.BUKKIT;
            instance = new BukkitServer();
        } else {
            serverType = ServerType.UNKNOWN;
            instance = new DummyServer();
        }
    }
}
