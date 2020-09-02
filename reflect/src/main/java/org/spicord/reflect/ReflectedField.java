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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@SuppressWarnings("unchecked")
public final class ReflectedField extends ReflectBase<ReflectedField> implements AccessibleObject<ReflectedField> {

    private final Object o;
    private final Field f;

    public ReflectedField(Object o, String field) {
        this(o.getClass(), field);
    }

    public ReflectedField(Class<?> clazz, String field) {
        this.o = null;

        try {
            this.f = clazz.getDeclaredField(field);
        } catch (ReflectiveOperationException e) {
            throw new NullPointerException();
        }
    }

    ReflectedField(Object o, Field f) {
        if (f == null) throw new NullPointerException();

        this.o = o;
        this.f = f;
    }

    /**
     * 
     * @param <T>
     * @return
     */
    public <T> T getValue() {
        return getValue(o);
    }

    /**
     * 
     * @param <T>
     * @param obj
     * @return
     */
    public <T> T getValue(final Object obj) {
        try {
            return (T) f.get(obj);
        } catch (IllegalArgumentException e) {
            handleException(e);
        } catch (IllegalAccessException e) {
            handleException(e);
        }
        return null;
    }

    /**
     * 
     * @return
     */
    public ReflectedObject getReflectValue() {
        Object val = getValue();
        if (val == null) return null;
        return new ReflectedObject(val).setErrorRule(getErrorRule());
    }

    /**
     * 
     * @param value
     */
    public void setValue(final Object value) {
        setValue(o, value);
    }

    /**
     * 
     * @param obj
     * @param value
     */
    public void setValue(final Object obj, final Object value) {
        try {
            f.set(obj, value);
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

    /**
     * 
     * @return
     */
    public ReflectedField setModifiable() {
        try {
            Field mf = Field.class.getDeclaredField("modifiers");
            mf.setAccessible(true);
            mf.setInt(f, f.getModifiers() & ~Modifier.FINAL);
        } catch (Exception e) {
            handleException(e);
        }
        return this;
    }

    public boolean isStatic() {
        return Modifier.isStatic(f.getModifiers());
    }
}
