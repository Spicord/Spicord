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

package eu.mcdb.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ArrayUtils {

    /**
     * Add the {@code element} to the {@code array} if it's not present.
     * 
     * @param array the array
     * @param element the element
     * @return the result array
     */
    public static <T> T[] push(T[] array, T element) {
        final List<T> l = toList(array);
        if (!l.contains(element)) l.add(element);
        return toArray(l, array);
    }

    /**
     * Remove the {@code element} from the {@code array}.
     * 
     * @param array the array
     * @param element the element
     * @return the result array
     */
    public static <T> T[] remove(T[] array, T element) {
        final List<T> l = toList(array);
        l.remove(element);
        return toArray(l, array);
    }

    /**
     * Remove the last element from the given array.
     * 
     * @param array the array
     * @return the result array
     */
    public static <T> T[] pop(T[] array) {
        if (array.length == 0)
            return array;

        final List<T> l = toList(array);
        l.remove(array.length - 1);
        return toArray(l, array);
    }

    /**
     * Remove the first element from the {@code array}.
     * 
     * @param array the array
     * @return the result array
     */
    public static <T> T[] shift(T[] array) {
        if (array.length == 0)
            return array;

        final List<T> l = toList(array);
        l.remove(0);
        return toArray(l, array);
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] toArray(List<T> list, T[] orig) {
        return (T[]) Arrays.copyOf(list.toArray(), list.size(), orig.getClass());
    }

    private static <T> LinkedList<T> toList(T[] array) {
        return new LinkedList<T>(Arrays.asList(array));
    }
}
