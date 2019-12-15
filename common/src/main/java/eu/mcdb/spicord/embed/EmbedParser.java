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

package eu.mcdb.spicord.embed;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;

public class EmbedParser {

    protected static final Gson GSON;

    static {
        GSON = new Gson();
    }

    /**
     * Converts a json string to a {@link Embed} object.
     * 
     * @param json the json to be parsed.
     * @return the {@link Embed} object.
     */
    public static Embed parse(String json) {
        Preconditions.checkNotNull(json, "json");

        return GSON.fromJson(json, Embed.class);
    }
}
