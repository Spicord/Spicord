package eu.mcdb.universal.config;

import java.util.List;

public interface GetBasedConfiguration extends BaseConfiguration {

    default Object get(String path, Object def) {
        final Object o = get(path);
        return o == null ? def : o;
    }

    default boolean getBoolean(String path) {
        return Boolean.valueOf(getString(path));
    }

    default boolean getBoolean(String path, boolean def) {
        final Boolean bool = getBoolean(path);
        return bool == null ? def : bool;
    }

    default List<Boolean> getBooleanList(String path) {
        return getList(Boolean.class, path);
    }

    default double getDouble(String path) {
        return Double.valueOf(getString(path));
    }

    default double getDouble(String path, double def) {
        final Double d = getDouble(path);
        return d == null ? def : d;
    }

    default List<Double> getDoubleList(String path) {
        return getList(Double.class, path);
    }

    default List<Float> getFloatList(String path) {
        return getList(Float.class, path);
    }

    default int getInt(String path) {
        return Integer.valueOf(getString(path));
    }

    default int getInt(String path, int def) {
        final Integer i = getInt(path);
        return i == null ? def : i;
    }

    default List<Integer> getIntegerList(String path) {
        return getList(Integer.class, path);
    }

    default List<?> getList(String path) {
        return getList(Object.class, path);
    }

    default List<?> getList(String path, List<?> def) {
        final List<?> l = getList(path);
        return l == null ? def : l;
    }

    default long getLong(String path) {
        return Long.valueOf(getString(path));
    }

    default long getLong(String path, long def) {
        final Long l = getLong(path);
        return l == null ? def : l;
    }

    default List<Long> getLongList(String path) {
        return getList(Long.class, path);
    }

    default String getString(String path) {
        return String.valueOf(get(path));
    }

    default String getString(String path, String def) {
        final String s = getString(path);
        return s == null ? def : s;
    }

    default List<String> getStringList(String path) {
        return getList(String.class, path);
    }

    @SuppressWarnings("unchecked")
    default <T> List<T> getList(Class<T> type, String path) {
        final Object o = get(path);

        if (o instanceof List) {
            return (List<T>) o;
        }

        return null;
    }
}
