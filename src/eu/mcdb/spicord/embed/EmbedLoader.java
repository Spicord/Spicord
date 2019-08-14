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

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import com.google.common.base.Preconditions;
import eu.mcdb.spicord.Spicord;

public class EmbedLoader {

    private Map<String, Embed> embeds = new HashMap<String, Embed>();

    public EmbedLoader() {
    }

    public void load(File dir) {
        Preconditions.checkArgument(dir.isDirectory(), "dir");

        for (File f : dir.listFiles()) {
            String name = f.getName().trim();

            if (f.isFile() && name.endsWith(".json")) {
                try {
                    name = name.substring(0, name.length() - 5).trim();
                    String content = new String(Files.readAllBytes(f.toPath()), Charset.forName("UTF-8"));
                    embeds.put(name, Embed.fromJson(content));
                } catch (Exception e) {
                    Spicord.getInstance().getLogger().warning("Cannot load the embed '" + f.getName() + "'.");
                }
            }
        }
    }

    public Embed getEmbedByName(String name) {
        return embeds.get(name);
    }
}
