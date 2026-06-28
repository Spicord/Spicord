package org.spicord.reflect;

@SuppressWarnings("unchecked")
public class ReflectBase<T> implements ExceptionHandler {

    private ReflectErrorRule errorRule = ReflectErrorRule.THROW;
    private ReflectExceptionHandler errorHandler;

    public T setErrorRule(ReflectErrorRule errorRule) {
        this.errorRule = errorRule;
        return (T) this;
    }

    public ReflectErrorRule getErrorRule() {
        return errorRule;
    }

    public T setErrorHandler(ReflectExceptionHandler handler) {
        this.errorHandler = handler;
        return (T) this;
    }

    @Override
    /*protected*/ public void handleException(Exception e) {
        switch (getErrorRule()) {
        case IGNORE:
            break;
        case PRINT:
            e.printStackTrace(System.err);
            break;
        case THROW:
            if (errorHandler == null) {
                throw new ReflectException(e);
            } else {
                errorHandler.handle(e);
            }
        }
    }
}
