package org.spicord.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Function;

public final class ReflectedObject extends ReflectBase<ReflectedObject> {

    private final Object o;
    private final Class<?> c;

    public ReflectedObject(Class<?> c) {
        this(c, null);
    }

    public ReflectedObject(Class<?> c, Object o) {
        if (c == null) throw new NullPointerException();
        if (o != null && !c.isInstance(o)) throw new IllegalArgumentException();

        this.c = c;
        this.o = o;
    }

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

    public ReflectedField findField(Function<Field, Boolean> fun) {
        for (Field f : c.getDeclaredFields()) {
            if (fun.apply(f)) {
                return new ReflectedField(o, f).setErrorRule(getErrorRule());
            }
        }
        return null;
    }

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

    public ReflectedMethod findMethod(Function<Method, Boolean> fun) {
        for (Method m : c.getDeclaredMethods()) {
            if (fun.apply(m)) {
                return new ReflectedMethod(o, m).setErrorRule(getErrorRule());
            }
        }
        return null;
    }

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
