package eu.mcdb.universal.config;

import java.io.File;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public abstract class YamlConfiguration implements BaseConfiguration {

    private final static Gson GSON = new Gson();

    public static YamlConfiguration load(final File file) {
        return new SnakeYamlConfiguration(file);
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
