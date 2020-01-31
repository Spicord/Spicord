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

import eu.mcdb.util.ReflectionUtils;

public enum ServerType {

    BUKKIT("org.bukkit.Bukkit"),
    BUNGEECORD("net.md_5.bungee.api.ProxyServer"),
    VELOCITY("com.velocitypowered.api.proxy.ProxyServer"),
    SPONGE("org.spongepowered.api.Game"),
    UNKNOWN(null);

    private final String clazz;

    private ServerType(String clazz) {
        this.clazz = clazz;
    }

    private boolean check() {
        if (clazz == null) return false;
        return ReflectionUtils.classExists(clazz);
    }

    public static ServerType auto() {
        if ("1".equals(System.getenv("SPICORD_CONSOLE")))
            return UNKNOWN;

        for (ServerType val : ServerType.values())
            if (val.check()) return val;

        return UNKNOWN;
    }
}
