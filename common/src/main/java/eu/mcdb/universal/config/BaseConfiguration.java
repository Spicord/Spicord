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

import java.io.IOException;
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

    long getLong(String path);
    long getLong(String path, long def);

    List<Long> getLongList(String path);

    String getString(String path);
    String getString(String path, String def);

    List<String> getStringList(String path);

    Map<String, Object> getValues();

    void save() throws IOException;

}
