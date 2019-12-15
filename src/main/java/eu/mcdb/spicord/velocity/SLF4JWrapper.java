<<<<<<< HEAD
/*
 * Copyright (C) 2019  OopsieWoopsie
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

=======
>>>>>>> 41854b324e3d05c7046534531eda7df28818b7b3
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
