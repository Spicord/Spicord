/*
 * Copyright (C) 2020  OopsieWoopsie
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

import org.spicord.reflect.ReflectUtils;

public enum ServerType {

    BUKKIT     ("org.bukkit.Bukkit"),
    VELOCITY   ("com.velocitypowered.api.proxy.ProxyServer"),
    BUNGEECORD ("net.md_5.bungee.api.ProxyServer"),
    SPONGE     ("org.spongepowered.api.Game"),
    UNKNOWN    (null);

    private final String serverClass;

    private ServerType(String serverClass) {
        this.serverClass = serverClass;
    }

    private boolean isCurrent() {
        if (serverClass == null) {
            return false;
        }

        return ReflectUtils.findClass(serverClass).isPresent();
    }

    public static ServerType auto() {
        if (System.getProperty("SPICORD_CONSOLE", "false").equals("true")) {
            return UNKNOWN;
        }

        for (ServerType serverType : ServerType.values()) {
            if (serverType.isCurrent()) {
                return serverType;
            }
        }

        return UNKNOWN;
    }
}
