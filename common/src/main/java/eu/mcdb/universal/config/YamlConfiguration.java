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
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import eu.mcdb.universal.Server;

public abstract class YamlConfiguration implements BaseConfiguration {

    private final static Gson GSON = new Gson();

    public static YamlConfiguration load(final File file) {
        switch (Server.getServerType()) {
        case BUKKIT:
            return new BukkitYamlConfiguration(file);
        case BUNGEECORD:
            return new BungeeYamlConfiguration(file);
        default:
            return new SnakeYamlConfiguration(file);
        }
    }

    public static YamlConfiguration load(final String file) {
        return load(new File(file));
    }

    /**
     * Convert this data into an object of type T.
     * 
     * @param <T> the type of the object
     * @param clazz the class of T
     * @return the result object
     */
    public <T> T to(Class<T> clazz) {
        final JsonElement json = GSON.toJsonTree(getValues());

        if (clazz == JsonElement.class) {
            return clazz.cast(json);
        }

        return GSON.fromJson(json, clazz);
    }

    @Override
    public String toString() {
        return GSON.toJson(getValues());
    }
}
