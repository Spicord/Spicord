package org.spicord.reflect;

public final class ConstructorAccessor extends ReflectBase<ConstructorAccessor> {

    private final ReflectedMethod method;

    ConstructorAccessor(Object accessor) {
        if (accessor == null) throw new NullPointerException();

        this.method = new ReflectedObject(accessor)
                .setErrorRule(getErrorRule())
                .getMethod("newInstance", Object[].class).setAccessible();
    }

    public Object newInstance(Object[] args) {
        return method.invoke(new Object[] { args });
    }
}
