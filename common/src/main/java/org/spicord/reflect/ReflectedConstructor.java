package org.spicord.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("unchecked")
public final class ReflectedConstructor extends ReflectBase<ReflectedConstructor> implements AccessibleObject<ReflectedConstructor>, InvokableObject {

    private final Constructor<?> c;

    ReflectedConstructor(Constructor<?> c) {
        if (c == null) throw new NullPointerException();
        this.c = c;
    }

    public ConstructorAccessor getConstructorAccessor() {
        Object accessor = getAccessor();
        if (accessor == null) return null;
        return new ConstructorAccessor(accessor).setErrorRule(getErrorRule());
    }

    private Object getAccessor() {
        ReflectedObject obj = new ReflectedObject(Constructor.class, c)
                .setErrorRule(getErrorRule());

        Object accessor = obj.getField("constructorAccessor").setAccessible().getValue();

        if (accessor == null)
            accessor = obj.getMethod("acquireConstructorAccessor").setAccessible().invoke();

        return accessor;
    }

    @Override
    public <T> T invoke(Object... args) {
        try {
            return (T) c.newInstance(args);
        } catch (InstantiationException e) {
            handleException(e);
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
    public ReflectedConstructor setAccessible(boolean flag) {
        c.setAccessible(flag);
        return this;
    }
}
