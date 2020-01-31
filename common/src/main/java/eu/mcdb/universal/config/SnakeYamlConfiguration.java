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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

@SuppressWarnings({"serial", "unchecked"})
class SnakeYamlConfiguration extends YamlConfiguration {

    private final File file;
    private final Yaml yaml;
    private final Map<String, Object> map;

    SnakeYamlConfiguration(final File file) {
        this.file = file;
        try {
            final DumperOptions options = new DumperOptions();
            options.setIndicatorIndent(1);
            this.yaml = new Yaml(options);
            this.map = yaml.load(new FileReader(file));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void set(String path, Object value) {
        final String[] parts = path.split("\\.");

        if (parts.length == 1) {
            this.map.put(parts[0], value);
            return;
        }

        Map<String, Object> last = null;

        int i = 0;
        for (final String k : parts) {
            final Object o = get(k);

            if (o instanceof Map) last = (Map<String, Object>) o;
            else break;

            i++;
        }

        if (last == null) {
            Map<String, Object> values = put(parts[parts.length-1], value);

            for (int i2 = parts.length - 2; i2 >= 0; i2--) {
                values = put(parts[i2], values);
            }

            this.map.putAll(values);
        } else {
            if ((i + 1) == parts.length) {
                last.put(parts[i], value);
            } else {
                Map<String, Object> values = put(parts[parts.length-1], value);

                for (int i2 = parts.length - 2; i2 >= i; i2--) {
                    values = put(parts[i2], values);
                }

                last.putAll(values);
            }
        }
    }

    private Map<String, Object> put(String k, Object v) {
        return new LinkedHashMap<String, Object>() {{ put(k, v); }};
    }

    @Override
    public Object get(String path) {
        if (path == null) return null;

        final String[] parts = path.split("\\.");
        Object v = map.get(parts[0]);

        if (parts.length == 1)
            return v;

        for (int i = 1; i < parts.length; i++) {
            if (v instanceof Map) {
                v = ((Map<String, Object>) v).get(parts[i]);
            }
        }

        return v;
    }

    @Override
    public Object get(String path, Object def) {
        final Object o = get(path);
        return o == null ? def : o;
    }

    @Override
    public boolean getBoolean(String path) {
        return Boolean.valueOf(getString(path));
    }

    @Override
    public boolean getBoolean(String path, boolean def) {
        final Boolean bool = getBoolean(path);
        return bool == null ? def : bool;
    }

    @Override
    public List<Boolean> getBooleanList(String path) {
        return getList(Boolean.class, path);
    }

    @Override
    public double getDouble(String path) {
        return Double.valueOf(getString(path));
    }

    @Override
    public double getDouble(String path, double def) {
        final Double d = getDouble(path);
        return d == null ? def : d;
    }

    @Override
    public List<Double> getDoubleList(String path) {
        return getList(Double.class, path);
    }

    @Override
    public List<Float> getFloatList(String path) {
        return getList(Float.class, path);
    }

    @Override
    public int getInt(String path) {
        return Integer.valueOf(getString(path));
    }

    @Override
    public int getInt(String path, int def) {
        final Integer i = getInt(path);
        return i == null ? def : i;
    }

    @Override
    public List<Integer> getIntegerList(String path) {
        return getList(Integer.class, path);
    }

    @Override
    public List<?> getList(String path) {
        return getList(Object.class, path);
    }

    @Override
    public List<?> getList(String path, List<?> def) {
        final List<?> l = getList(path);
        return l == null ? def : l;
    }

    @Override
    public long getLong(String path) {
        return Long.valueOf(getString(path));
    }

    @Override
    public long getLong(String path, long def) {
        final Long l = getLong(path);
        return l == null ? def : l;
    }

    @Override
    public List<Long> getLongList(String path) {
        return getList(Long.class, path);
    }

    @Override
    public String getString(String path) {
        return String.valueOf(get(path));
    }

    @Override
    public String getString(String path, String def) {
        final String s = getString(path);
        return s == null ? def : s;
    }

    @Override
    public List<String> getStringList(String path) {
        return getList(String.class, path);
    }

    @Override
    public Map<String, Object> getValues() {
        return map;
    }

    @Override
    public void save() throws IOException {
        final String str = yaml.dumpAsMap(map);
        final FileWriter fw = new FileWriter(file);
        fw.write(str.toCharArray());
        fw.flush();
        fw.close();
    }

    private <T> List<T> getList(Class<T> type, String path) {
        final Object o = get(path);

        if (o instanceof List) {
            return (List<T>) o;
        }

        return null;
    }
}
