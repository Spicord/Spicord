package eu.mcdb.util;

import java.lang.reflect.Array;
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

        T[] toReturn = create(array.getClass().getComponentType(), array.length - 1);

        for (int i = 0; i < toReturn.length; i++) {
            toReturn[i] = array[i];
        }

        return toReturn;
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

        T[] toReturn = create(array.getClass().getComponentType(), array.length - 1);

        for (int i = 0; i < toReturn.length; i++) {
            toReturn[i] = array[i+1];
        }

        return toReturn;
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] toArray(List<T> list, T[] orig) {
        return (T[]) Arrays.copyOf(list.toArray(), list.size(), orig.getClass());
    }

    private static <T> LinkedList<T> toList(T[] array) {
        return new LinkedList<T>(Arrays.asList(array));
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] create(Class<?> componentType, int length) {
        return (T[]) Array.newInstance(componentType, length);
    }
}
