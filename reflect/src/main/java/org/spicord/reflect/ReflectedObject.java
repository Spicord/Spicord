package org.spicord.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Function;

public final class ReflectedObject extends ReflectBase<ReflectedObject> {

    private final Object o;
    private final Class<?> c;

    /**
     * 
     * @param c
     */
    public ReflectedObject(Class<?> c) {
        this(c, null);
    }

    /**
     * 
     * @param c
     * @param o
     */
    public ReflectedObject(Class<?> c, Object o) {
        if (c == null) throw new NullPointerException();
        if (o != null && !c.isInstance(o)) throw new IllegalArgumentException();

        this.c = c;
        this.o = o;
    }

    /**
     * 
     * @param o
     */
    public ReflectedObject(Object o) {
        if (o == null) throw new NullPointerException();

        if (o instanceof Class) {
            this.o = null;
            this.c = (Class<?>)o;
        } else {
            this.o = o;
            this.c = o.getClass();
        }
    }

    /**
     * 
     * @param name
     * @return
     */
    public ReflectedField getField(String name) {
        try {
            return new ReflectedField(o, c.getDeclaredField(name)).setErrorRule(getErrorRule());
        } catch (NoSuchFieldException e) {
            handleException(e);
        } catch (SecurityException e) {
            handleException(e);
        }
        return null;
    }

    /**
     * 
     * @param fun
     * @return
     */
    public ReflectedField findField(Function<Field, Boolean> fun) {
        for (Field f : c.getDeclaredFields()) {
            if (fun.apply(f)) {
                return new ReflectedField(o, f).setErrorRule(getErrorRule());
            }
        }
        return null;
    }

    /**
     * 
     * @param name
     * @param parameterTypes
     * @return
     */
    public ReflectedMethod getMethod(String name, Class<?>... parameterTypes) {
        try {
            return new ReflectedMethod(o, c.getDeclaredMethod(name, parameterTypes)).setErrorRule(getErrorRule());
        } catch (NoSuchMethodException e) {
            handleException(e);
        } catch (SecurityException e) {
            handleException(e);
        }
        return null;
    }

    /**
     * 
     * @param fun
     * @return
     */
    public ReflectedMethod findMethod(Function<Method, Boolean> fun) {
        for (Method m : c.getDeclaredMethods()) {
            if (fun.apply(m)) {
                return new ReflectedMethod(o, m).setErrorRule(getErrorRule());
            }
        }
        return null;
    }

    /**
     * 
     * @param parameterTypes
     * @return
     */
    public ReflectedConstructor getConstructor(Class<?>... parameterTypes) {
        try {
            return new ReflectedConstructor(c.getDeclaredConstructor(parameterTypes)).setErrorRule(getErrorRule());
        } catch (NoSuchMethodException e) {
            handleException(e);
        } catch (SecurityException e) {
            handleException(e);
        }
        return null;
    }

    public boolean isEnum() {
        return c.isEnum();
    }

    public boolean isArray() {
        return c.isArray();
    }
}
