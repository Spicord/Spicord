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
