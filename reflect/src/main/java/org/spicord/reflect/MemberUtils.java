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
