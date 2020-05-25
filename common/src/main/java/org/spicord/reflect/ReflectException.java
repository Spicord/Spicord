package org.spicord.reflect;

public class ReflectException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ReflectException() {}

    public ReflectException(String message) {
        super(message);
    }

    public ReflectException(Exception cause) {
        super(cause);
    }

    public ReflectException(String message, Exception cause) {
        super(message, cause);
    }

    @Override
    public Exception getCause() {
        return (Exception) super.getCause();
    }
}
