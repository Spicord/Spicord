package org.spicord.reflect;

@SuppressWarnings("unchecked")
public class ReflectBase<T> {

    private ReflectErrorRule errorRule = ReflectErrorRule.THROW;
    private ReflectErrorHandler errorHandler;

    public T setErrorRule(ReflectErrorRule errorRule) {
        this.errorRule = errorRule;
        return (T) this;
    }

    public ReflectErrorRule getErrorRule() {
        return errorRule;
    }

    public T setErrorHandler(ReflectErrorHandler handler) {
        this.errorHandler = handler;
        return (T) this;
    }

    protected void handleException(Exception e) {
        switch (getErrorRule()) {
        case IGNORE:
            break;
        case PRINT:
            e.printStackTrace(System.err);
            break;
        case THROW:
            ReflectException ex = new ReflectException(e);
            if (errorHandler == null) {
                throw ex;
            } else {
                errorHandler.handle(ex);
            }
        }
    }
}
