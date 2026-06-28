package org.spicord.embed;

import java.io.StringReader;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

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

        JsonReader jsonReader = new JsonReader(new StringReader(json.trim()));
        jsonReader.setLenient(true);

        return GSON.fromJson(jsonReader, Embed.class);
    }
}
