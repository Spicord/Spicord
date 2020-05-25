package org.spicord.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@SuppressWarnings("unchecked")
public final class ReflectedField extends ReflectBase<ReflectedField> implements AccessibleObject<ReflectedField> {

    private final Object o;
    private final Field f;

    ReflectedField(Object o, Field f) {
        if (f == null) throw new NullPointerException();

        this.o = o;
        this.f = f;
    }

    public <T> T getValue() {
        try {
            return (T) f.get(o);
        } catch (IllegalArgumentException e) {
            handleException(e);
        } catch (IllegalAccessException e) {
            handleException(e);
        }
        return null;
    }

    public ReflectedObject getReflectValue() {
        Object val = getValue();
        if (val == null) return null;
        return new ReflectedObject(val).setErrorRule(getErrorRule());
    }

    public void setValue(final Object value) {
        try {
            f.set(o, value);
        } catch (IllegalArgumentException e) {
            handleException(e);
        } catch (IllegalAccessException e) {
            handleException(e);
        }
    }

    @Override
    public ReflectedField setAccessible(boolean flag) {
        f.setAccessible(flag);
        return this;
    }

    public ReflectedField setModifiable() {
        try {
            Field mf = Field.class.getDeclaredField("modifiers");
            mf.setAccessible(true);
            mf.set(f, f.getModifiers() & ~Modifier.FINAL);
        } catch (Exception e) {
            handleException(e);
        }
        return this;
    }
}
