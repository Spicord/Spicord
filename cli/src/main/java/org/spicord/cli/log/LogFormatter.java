/*
 * Copyright (C) 2020  OopsieWoopsie
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

package org.spicord.cli.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import eu.mcdb.util.chat.ChatColor;

class LogFormatter extends Formatter {

    private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    @Override
    public String format(final LogRecord record) {
        final StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append(this.time(record));
        builder.append("] [");
        builder.append(record.getLevel().toString());
        builder.append("] ");
        builder.append(this.color(record.getMessage()));
        builder.append("\r\n");
        return builder.toString();
    }

    private String color(String message) {
        return ChatColor.stripColor(message);
    }

    private String time(LogRecord record) {
        return this.sdf.format(new Date(record.getMillis()));
    }
}
