package org.spicord.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Marker;
import org.slf4j.helpers.LegacyAbstractLogger;

public class JDALoggerImpl extends LegacyAbstractLogger {

    private static final long serialVersionUID = 1L;

    private final Logger log;
    private final String prefix;

    public JDALoggerImpl(String name, Logger log) {
        super.name = name;
        this.log = log;

        this.prefix = "[JDA:" + name + "] ";
    }

    @Override
    public boolean isDebugEnabled() {
        return isLevelEnabled(Level.CONFIG);
    }

    @Override
    public boolean isInfoEnabled() {
        return isLevelEnabled(Level.INFO);
    }

    @Override
    public boolean isWarnEnabled() {
        return isLevelEnabled(Level.WARNING);
    }

    @Override
    public boolean isErrorEnabled() {
        return isLevelEnabled(Level.SEVERE);
    }

    private boolean isLevelEnabled(Level level) {
        return log.getLevel().intValue() <= level.intValue();
    }

    @Override
    protected void handleNormalizedLoggingCall(org.slf4j.event.Level level, Marker m, String format, Object[] arguments, Throwable t) {
        final boolean hasArguments = arguments != null && arguments.length > 0;

        final String message;
        if (hasArguments) {
            message = prefix + format(format, arguments);
        } else {
            message = prefix + format;
        }

        switch (level) {
        case DEBUG:
        case TRACE:
            log.config(message); break;
        case ERROR:
            log.severe(message); break;
        case INFO:
            log.info(message); break;
        case WARN:
            log.warning(message); break;
        }

        if (t != null) {
            t.printStackTrace(System.err);
        }
    }

    private static String toString(Object object) {
        if (object == null) {
            return "null";
        }
        return object.toString();
    }

    private static String format(String format, Object... arguments) {
        for (int i = 0; i < arguments.length; i++) {
            format = format.replaceFirst(
                Pattern.quote("{}"),
                Matcher.quoteReplacement(toString(arguments[i]))
            );
        }
        return format;
    }

    @Override
    protected String getFullyQualifiedCallerName() {
        return null;
    }

    @Override
    public boolean isTraceEnabled() {
        return isDebugEnabled();
    }
}
