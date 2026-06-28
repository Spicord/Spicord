package eu.mcdb.util;

import java.util.logging.LogRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SLF4JWrapper extends java.util.logging.Logger {

    private final Logger logger;

    public SLF4JWrapper() {
        this(Logger.ROOT_LOGGER_NAME);
    }

    public SLF4JWrapper(String name) {
        super(name, null);
        this.logger = LoggerFactory.getLogger(name);
    }

    public SLF4JWrapper(Logger logger) {
        super(logger.getName(), null);
        this.logger = logger;
    }

    @Override
    public void log(LogRecord record) {
        final String message = record.getMessage();

        switch (record.getLevel().toString()) {
        case "CONFIG":
        case "FINE":
        case "FINER":
        case "FINEST":
            logger.debug(message); break;
        case "SEVERE":
            logger.error(message); break;
        case "WARNING":
            logger.warn(message); break;
        case "INFO":
        case "ALL":
        case "OFF":
        default:
            logger.info(message); break;
        }
    }

    public Logger getSLF4JLogger() {
        return logger;
    }
}
