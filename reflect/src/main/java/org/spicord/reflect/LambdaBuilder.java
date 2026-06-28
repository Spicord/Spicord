package org.spicord.reflect;

import static org.spicord.reflect.ReflectUtils.getInterfaceMethod;
import static org.spicord.reflect.ReflectUtils.getLookup;
import static org.spicord.reflect.ReflectUtils.isFunctionalInterface;
import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaConversionException;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

public interface LambdaBuilder extends ExceptionHandler {

    /**
     * 
     * @return
     */
    Member getMember();

    /**
     * 
     * @return
     */
    default MethodHandle getHandle() {
        return getHandle(MethodHandles.lookup());
    }

    /**
     * 
     * @param lookup
     * @return
     */
    MethodHandle getHandle(Lookup lookup);

    default <T> T createLambda(Class<T> interfaceClass) {

        Lookup lookup = getLookup(getMember());
        MethodHandle handle = getHandle(lookup);

        return createLambda(interfaceClass, handle, lookup);
    }

    /**
     * 
     * @param <T>
     * @param interfaceClass
     * @return
     */
    default <T> T createLambda(Class<T> interfaceClass, MethodHandle handle, Lookup lookup) {
        try {
            if (isFunctionalInterface(interfaceClass)) {
                Method method = getInterfaceMethod(interfaceClass);
                MethodType invokedType = MethodType.methodType(interfaceClass);
                MethodType samMethodType = MethodType.methodType(method.getReturnType(), method.getParameterTypes());

                try {
                    CallSite callsite = LambdaMetafactory.metafactory(
                            lookup,
                            method.getName(),
                            invokedType,
                            samMethodType,
                            handle,
                            handle.type()
                        );

                    MethodHandle target = callsite.getTarget();

                    return (T) target.invoke();
                } catch (LambdaConversionException e) {
                    handleException(e);
                }
            } else {
                throw new IllegalArgumentException("given class is not a functional interface");
            }
        } catch (Throwable e) {
            if (e instanceof Exception) {
                handleException((Exception) e);
            } else {
                handleException(new RuntimeException(e));
            }
        }
        return null;
    }
}
