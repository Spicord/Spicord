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
