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
                    String content = new String(Files.readAllBytes(f.toPath()), Charset.defaultCharset());
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
