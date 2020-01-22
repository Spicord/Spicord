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

package eu.mcdb.spicord.embed;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import com.google.common.base.Preconditions;
import eu.mcdb.spicord.api.Node;
import eu.mcdb.util.ZipExtractor;

public class EmbedLoader implements Node {

    private static final Charset charset = Charset.forName("UTF-8");
    private final Map<String, Embed> embeds;

    public EmbedLoader() {
        this.embeds = new HashMap<String, Embed>();
    }

    /**
     * Loads the embed files located into the {@code dir}.
     * 
     * @param dir the directory
     */
    public void load(final File dir) {
        Preconditions.checkArgument(dir.isDirectory(), "dir");

        for (final File file : dir.listFiles()) {
            String name = file.getName().trim();

            if (file.isFile() && name.endsWith(".json")) {
                try {
                    name = name.substring(0, name.length() - 5).trim();
                    final String content = new String(Files.readAllBytes(file.toPath()), charset);
                    embeds.put(name, Embed.fromJson(content));
                } catch (IOException e) {
                    getLogger().warning("Cannot load the embed '" + file.getName() + "'.");
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * This method extracts the json (embed) files located inside the "embed" folder
     * which should be at the root of the jar/zip {@code file}. <br>
     * A file will be extracted only if it don't exist into the {@code out} folder.
     * <br>
     * After that, all the embed files located into the {@code out} folder will be
     * loaded into a new {@link EmbedLoader} instance.
     * 
     * @param file the file jar file
     * @param out  the output directory
     * @return the {@link EmbedLoader} instance with the loaded embeds
     * @throws IOException if an I/O error has occurred
     */
    public static EmbedLoader extractAndLoad(final File file, final File out) throws IOException {
        out.mkdirs();
        Preconditions.checkArgument(out.isDirectory(), "out");

        final ZipExtractor ex = new ZipExtractor(file);
        ex.filter("embed\\/.*\\.json");
        ex.extract(out);
        ex.close();

        final EmbedLoader loader = new EmbedLoader();
        loader.load(out);

        return loader;
    }

    /**
     * Gets an embed instance previously loaded by using its name.
     * <br>
     * The embed name has the same name as its file but without the .json extension.
     * 
     * @see {@link #load(File)}
     * @param name the embed name
     * @return the {@link Embed} instance
     */
    public Embed getEmbedByName(final String name) {
        return embeds.get(name);
    }
}
