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
