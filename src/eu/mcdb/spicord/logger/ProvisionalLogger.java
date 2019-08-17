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

package eu.mcdb.spicord.logger;

// This class (or at least its structure) is temporary.
public class ProvisionalLogger implements eu.mcdb.internal.org.slf4j.Logger {

    private boolean debug;
    private boolean log;

    public ProvisionalLogger(boolean debug, boolean log) {
        this.debug = debug;
        this.log = log;
    }

    @Override
    public void error(Object... obj) {
        log("[ERROR]", obj);
    }

    @Override
    public void warn(Object... obj) {
        log("[WARN]", obj);
    }

    @Override
    public void info(Object... obj) {
        log("[INFO]", obj);
    }

    @Override
    public void debug(Object... obj) {
        if (debug)
            log("[DEBUG]", obj);
    }

    @Override
    public void trace(Object... obj) {
    }

    public void log(String prefix, Object... obj) {
        if (log) {
            String str = String.valueOf(obj[0]);
            try {
                for (int i = 1; i < obj.length; i++) {
                    Object curr = obj[i];
                    if (curr instanceof Throwable) {
                        ((Throwable) curr).printStackTrace();
                    } else {
                        str = str.replaceFirst("\\{\\}", String.valueOf(curr) + "");
                    }
                }
            } catch (Exception ignored) {
            }
            System.out.println(String.format("[JDA] %s %s", prefix, str));
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return debug;
    }
}
