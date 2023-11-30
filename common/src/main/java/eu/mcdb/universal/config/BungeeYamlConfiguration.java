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
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;

class BungeeYamlConfiguration extends YamlConfiguration {

    private final File file;
    private final ConfigurationProvider provider;
    private final Configuration config;

    BungeeYamlConfiguration(final File file) {
        try {
            this.file = file;
            this.provider = ConfigurationProvider.getProvider(net.md_5.bungee.config.YamlConfiguration.class);
            this.config = provider.load(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BungeeYamlConfiguration(final Configuration config) {
        this.file = null;
        this.provider = null;
        this.config = config;
    }

    @Override
    public void set(String path, Object value) {
        config.set(path, value);
    }

    @Override
    public Object get(String path) {
        Object o = config.get(path);

        if (o instanceof Configuration) {
        	o = new BungeeYamlConfiguration((Configuration) o);
        }

        return o;
    }

    @Override
    public Object get(String path, Object def) {
        Object o = config.get(path, def);

        if (o instanceof Configuration) {
        	o = new BungeeYamlConfiguration((Configuration) o);
        }

        return o;
    }

    @Override
    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    @Override
    public boolean getBoolean(String path, boolean def) {
        return config.getBoolean(path, def);
    }

    @Override
    public List<Boolean> getBooleanList(String path) {
        return config.getBooleanList(path);
    }

    @Override
    public double getDouble(String path) {
        return config.getDouble(path);
    }

    @Override
    public double getDouble(String path, double def) {
        return config.getDouble(path, def);
    }

    @Override
    public List<Double> getDoubleList(String path) {
        return config.getDoubleList(path);
    }

    @Override
    public List<Float> getFloatList(String path) {
        return config.getFloatList(path);
    }

    @Override
    public int getInt(String path) {
        return config.getInt(path);
    }

    @Override
    public int getInt(String path, int def) {
        return config.getInt(path, def);
    }

    @Override
    public List<Integer> getIntegerList(String path) {
        return config.getIntList(path);
    }

    @Override
    public List<?> getList(String path) {
        return config.getList(path);
    }

    @Override
    public List<?> getList(String path, List<?> def) {
        return config.getList(path, def);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<?, ?>> getMapList(String path) {
        return (List<Map<?, ?>>) config.getList(path);
    }

    @Override
    public long getLong(String path) {
        return config.getLong(path);
    }

    @Override
    public long getLong(String path, long def) {
        return config.getLong(path, def);
    }

    @Override
    public List<Long> getLongList(String path) {
        return config.getLongList(path);
    }

    @Override
    public String getString(String path) {
        return config.getString(path);
    }

    @Override
    public String getString(String path, String def) {
        return config.getString(path, def);
    }

    @Override
    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    @Override
    public boolean contains(String path) {
        return config.contains(path);
    }

    @Override
    public BaseConfiguration getConfiguration(String path) {
        return new BungeeYamlConfiguration(config.getSection(path));
    }

    @Override
    public Collection<String> getKeys() {
    	return config.getKeys();
    }

    @Override
    public Map<String, Object> getValues() {
        return dig(config);
    }

    private Map<String, Object> dig(Configuration c) {
        final Map<String, Object> values = new HashMap<String, Object>();

        for (final String key : c.getKeys()) {
            Object o = c.get(key);

            if (o instanceof Configuration) {
                o = dig((Configuration) o);
            }

            values.put(key, o);
        }

        return values;
    }

    @Override
    public void save() throws IOException {
    	if (provider != null) {
            provider.save(config, file);
    	}
    }
}
