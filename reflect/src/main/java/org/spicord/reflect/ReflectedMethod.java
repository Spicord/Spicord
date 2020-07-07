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

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

@SuppressWarnings("unchecked")
public final class ReflectedMethod extends ReflectBase<ReflectedMethod>
        implements AccessibleObject<ReflectedMethod>, InvokableObject, LambdaBuilder {

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

    @Override
    public MethodHandle getHandle(Lookup lookup) {
        try {
            return lookup.unreflect(m);
        } catch (IllegalAccessException e) {
            handleException(e);
        }
        return null;
    }

    @Override
    public Member getMember() {
        return m;
    }
}
