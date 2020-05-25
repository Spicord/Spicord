package org.spicord.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressWarnings("unchecked")
public final class ReflectedMethod extends ReflectBase<ReflectedMethod>
        implements AccessibleObject<ReflectedMethod>, InvokableObject {

    private final Object o;
    private final Method m;

    ReflectedMethod(Object o, Method m) {
        if (m == null) throw new NullPointerException();

        this.o = o;
        this.m = m;
    }

    @Override
    public <T> T invoke(Object... args) {
        try {
            return (T) m.invoke(o, args);
        } catch (IllegalAccessException e) {
            handleException(e);
        } catch (IllegalArgumentException e) {
            handleException(e);
        } catch (InvocationTargetException e) {
            handleException(e);
        }
        return null;
    }

    @Override
    public ReflectedObject invokeReflect(Object... args) {
        Object res = invoke(args);
        if (res == null) return null;
        return new ReflectedObject(res).setErrorRule(getErrorRule());
    }

    @Override
    public ReflectedMethod setAccessible(boolean flag) {
        m.setAccessible(flag);
        return this;
    }
}
