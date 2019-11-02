/*
 * Copyright (C) 2019  OopsieWoopsie
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

package eu.mcdb.spicord.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ArrayUtils {

    /**
     * Adds the given element to the given array only if the element isn't there
     * 
     * @param array the array
     * @return the array with the given element
     */
    public static <T> T[] push(T[] array, T value) {
        final List<T> l = asList(array);
        if (!l.contains(value)) l.add(value);
        return toArray(l, array);
    }

    /**
     * Removes the given element of the given array
     * 
     * @param array the array
     * @return the array without the given element
     */
    public static <T> T[] remove(T[] array, T value) {
        final List<T> l = asList(array);
        l.remove(value);
        return toArray(l, array);
    }

    /**
     * Removes the last element of the given array
     * 
     * @param array the array
     * @return the array without the last element
     */
    public static <T> T[] pop(T[] array) {
        final List<T> l = asList(array);
        l.remove(array.length - 1);
        return toArray(l, array);
    }

    /**
     * Removes the first element of the given array
     * 
     * @param array the array
     * @return the array without the first element
     */
    public static <T> T[] shift(T[] array) {
        final List<T> l = asList(array);
        l.remove(0);
        return toArray(l, array);
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] toArray(List<T> list, T[] orig) {
        return (T[]) Arrays.copyOf(list.toArray(), list.size(), orig.getClass());
    }

    private static <T> LinkedList<T> asList(T[] array) {
        return new LinkedList<T>(Arrays.asList(array));
    }
}
