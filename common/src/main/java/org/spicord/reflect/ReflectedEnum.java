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
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unchecked")
public final class ReflectedEnum<T> {

    private final Class<T> clazz;
    private final Class<?>[] types;

    ReflectedEnum(Class<T> clazz, Class<?>[] parameterTypes) {
        if (!clazz.isEnum())
            throw new IllegalArgumentException();

        this.clazz = clazz;
        this.types = buildParameters(parameterTypes);
    }

    public T addValue(String name, Object... args) {
        ReflectedObject obj = new ReflectedObject(clazz);
        ConstructorAccessor accessor = obj.getConstructor(types).getConstructorAccessor();

        ReflectedField valuesField = obj
                .findField(f1 -> f1.getName().contains("$VALUES"))
                .setAccessible().setModifiable();

        Object[] values = valuesField.getValue();

        args = mergeArrays(new Object[] { name, values.length }, args);
        Object newValue = accessor.newInstance(args);

        values = mergeArrays(values, new Object[] { newValue });
        valuesField.setValue(values);

        return (T) newValue;
    }

    private static Class<?>[] buildParameters(Class<?>[] ptypes) {
        return mergeArrays(new Class[] { String.class, int.class }, ptypes);
    }

    private static <E> E[] mergeArrays(E[] first, E[] second) {
        Object[] arr = (Object[]) Array.newInstance(first.getClass().getComponentType(), first.length + second.length);
        List<Object> list = new LinkedList<>();
        for (Object a : first) list.add(a);
        for (Object a : second) list.add(a);
        return (E[]) list.toArray(arr);
    }
}
