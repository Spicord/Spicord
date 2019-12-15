package eu.mcdb.spicord.velocity;

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

    @Override
    public void log(LogRecord record) {
        // TODO: not send all messages with #info()
        logger.info(record.getMessage());
    }

    public Logger getSLF4JLogger() {
        return logger;
    }
}
