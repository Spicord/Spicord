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

package eu.mcdb.spicord.util;

import java.util.HashMap;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.helpers.MarkerIgnoringBase;

/**
 * Used for disable (hide) the JDA's messages.
 */
public class CustomMap<K, V> extends HashMap<String, Logger> {

	private static final long serialVersionUID = 1L;

	private final Logger logger = new MarkerIgnoringBase() {

		private static final long serialVersionUID = 1L;

		@Override
		public void debug(String arg0) {}

		@Override
		public void debug(String arg0, Object arg1) {}

		@Override
		public void debug(String arg0, Object... arg1) {}

		@Override
		public void debug(String arg0, Throwable arg1) {}

		@Override
		public void debug(String arg0, Object arg1, Object arg2) {}

		@Override
		public void error(String arg0) {}

		@Override
		public void error(String arg0, Object arg1) {}

		@Override
		public void error(String arg0, Object... arg1) {}

		@Override
		public void error(String arg0, Throwable arg1) {}

		@Override
		public void error(String arg0, Object arg1, Object arg2) {}

		@Override
		public void info(String arg0) {}

		@Override
		public void info(String arg0, Object arg1) {}

		@Override
		public void info(String arg0, Object... arg1) {}

		@Override
		public void info(String arg0, Throwable arg1) {}

		@Override
		public void info(String arg0, Object arg1, Object arg2) {}

		@Override
		public boolean isDebugEnabled() {
			return false;
		}

		@Override
		public boolean isErrorEnabled() {
			return false;
		}

		@Override
		public boolean isInfoEnabled() {
			return false;
		}

		@Override
		public boolean isTraceEnabled() {
			return false;
		}

		@Override
		public boolean isWarnEnabled() {
			return false;
		}

		@Override
		public void trace(String arg0) {}

		@Override
		public void trace(String arg0, Object arg1) {}

		@Override
		public void trace(String arg0, Object... arg1) {}

		@Override
		public void trace(String arg0, Throwable arg1) {}

		@Override
		public void trace(String arg0, Object arg1, Object arg2) {}

		@Override
		public void warn(String arg0) {}

		@Override
		public void warn(String arg0, Object arg1) {}

		@Override
		public void warn(String arg0, Object... arg1) {}

		@Override
		public void warn(String arg0, Throwable arg1) {}

		@Override
		public void warn(String arg0, Object arg1, Object arg2) {}
	};

	@Override
	public Logger computeIfAbsent(String key, Function<? super String, ? extends Logger> mappingFunction) {
		return logger;
	}

	@Override
	public Logger get(Object key) {
		return logger;
	}
}