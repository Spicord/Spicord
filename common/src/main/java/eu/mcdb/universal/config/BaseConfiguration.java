package eu.mcdb.universal.config;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface BaseConfiguration {

    void set(String path, Object value);

    Object get(String path);
    Object get(String path, Object def);

    boolean getBoolean(String path);
    boolean getBoolean(String path, boolean def);

    List<Boolean> getBooleanList(String path);

    double getDouble(String path);
    double getDouble(String path, double def);

    List<Double> getDoubleList(String path);
    List<Float> getFloatList(String path);

    int getInt(String path);
    int getInt(String path, int def);

    List<Integer> getIntegerList(String path);

    List<?> getList(String path);
    List<?> getList(String path, List<?> def);

    List<Map<?, ?>> getMapList(String path);

    long getLong(String path);
    long getLong(String path, long def);

    List<Long> getLongList(String path);

    String getString(String path);
    String getString(String path, String def);

    List<String> getStringList(String path);

    boolean contains(String path);

    BaseConfiguration getConfiguration(String path);

    Collection<String> getKeys();
    Map<String, Object> getValues();

    void save() throws IOException;

}
