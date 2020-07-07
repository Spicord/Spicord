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

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

public final class MemberUtils {

    public static boolean isPublic(Member m) {
        return Modifier.isPublic(m.getModifiers());
    }

    public static boolean isAbstract(Member m) {
        return Modifier.isAbstract(m.getModifiers());
    }

    public static boolean isFinal(Member m) {
        return Modifier.isFinal(m.getModifiers());
    }

    public static boolean isStatic(Member m) {
        return Modifier.isStatic(m.getModifiers());
    }
}
