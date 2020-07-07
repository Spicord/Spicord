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

public final class ConstructorAccessor extends ReflectBase<ConstructorAccessor> {

    private final ReflectedMethod method;

    ConstructorAccessor(Object accessor) {
        if (accessor == null) throw new NullPointerException();

        this.method = new ReflectedObject(accessor)
                .setErrorRule(getErrorRule())
                .getMethod("newInstance", Object[].class).setAccessible();
    }

    /**
     * 
     * @param args
     * @return
     */
    public Object newInstance(Object[] args) {
        return method.invoke(new Object[] { args });
    }
}
