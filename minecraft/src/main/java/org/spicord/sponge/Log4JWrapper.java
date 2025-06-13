package org.spicord.sponge;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.apache.logging.log4j.Logger;

public class Log4JWrapper extends java.util.logging.Logger {

    private final Map<Level, MessageLogger> levelToLoggerMap = new HashMap<>();

    protected Log4JWrapper(Logger logger) {
        super(null, null);

        levelToLoggerMap.put(Level.ALL, logger::info);
        levelToLoggerMap.put(Level.CONFIG, logger::debug);
        levelToLoggerMap.put(Level.FINE, logger::debug);
        levelToLoggerMap.put(Level.FINER, logger::debug);
        levelToLoggerMap.put(Level.FINEST, logger::debug);
        levelToLoggerMap.put(Level.INFO, logger::info);
        levelToLoggerMap.put(Level.OFF, s -> {});
        levelToLoggerMap.put(Level.SEVERE, logger::error);
        levelToLoggerMap.put(Level.WARNING, logger::warn);
    }

    @Override
    public void log(LogRecord record) {
        MessageLogger log = levelToLoggerMap.get(record.getLevel());

        if (log != null) {
            log.log(record.getMessage());
        }
    }

    private interface MessageLogger {
        void log(String m);
    }
}
