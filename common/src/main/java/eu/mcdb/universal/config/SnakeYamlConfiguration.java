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
import java.util.Map;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

@SuppressWarnings({"serial", "unchecked"})
class SnakeYamlConfiguration extends YamlConfiguration implements GetBasedConfiguration {

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
        } catch (IOException e) {
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
}
