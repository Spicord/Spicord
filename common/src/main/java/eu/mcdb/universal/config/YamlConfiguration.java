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

package eu.mcdb.universal.config;

import java.io.File;
import eu.mcdb.universal.Server;

public abstract class YamlConfiguration implements BaseConfiguration {

    public static YamlConfiguration load(final File file) {
        switch (Server.getServerType()) {
        case BUKKIT:
            return new BukkitYamlConfiguration(file);
        case BUNGEECORD:
            return new BungeeYamlConfiguration(file);
        default:
            throw new IllegalStateException("server version not supported yet");
        }
    }
}
