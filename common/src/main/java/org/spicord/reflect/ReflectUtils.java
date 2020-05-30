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

package org.spicord.reflect;

import java.lang.reflect.Array;
import java.util.Optional;

public final class ReflectUtils {

    public static Class<?> getArrayClass(Class<?> clazz) {
        return Array.newInstance(clazz, 0).getClass();
    }

    public static Optional<Class<?>> findClass(String name) {
        try {
            return Optional.of(Class.forName(name));
        } catch (ClassNotFoundException e) {}
        return Optional.empty();
    }

    public static <T> T nullOnException(ExceptionConsumer<T> action) {
        return action.runHandled();
    }
}
