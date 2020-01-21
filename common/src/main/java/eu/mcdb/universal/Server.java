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

package eu.mcdb.universal;

import static eu.mcdb.util.ReflectionUtils.classExists;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import lombok.Setter;

/**
 * This class provides methods that can be accessed no matter what server
 * software you are using.
 * 
 * @author OopsieWoopsie
 * @version 1.0
 */
public abstract class Server implements IServer {

    @Getter
    private static ServerType serverType;

    @Getter
    private static Server instance;

    @Getter
    @Setter
    private boolean debugEnabled; // false by default

    static {
        final boolean console = false;

        if (console) {
            serverType = ServerType.UNKNOWN;
            instance = new DummyServer();
        } else if (classExists("net.md_5.bungee.BungeeCord")) {
            serverType = ServerType.BUNGEECORD;
            instance = new BungeeServer();
        } else if (classExists("org.bukkit.Bukkit")) {
            serverType = ServerType.BUKKIT;
            instance = new BukkitServer();
        } else if (classExists("com.velocitypowered.api.proxy.ProxyServer")) {
            serverType = ServerType.VELOCITY;
            instance = new VelocityServer();
        } else {
            serverType = ServerType.UNKNOWN;
            instance = new DummyServer();
        }
    }

    public static void setVelocityHandle(ProxyServer proxy) {
        VelocityServer.setHandle(proxy);
    }
}
