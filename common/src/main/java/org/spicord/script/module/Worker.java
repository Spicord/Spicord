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

package org.spicord.script.module;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.spicord.script.ScriptEngine;

public final class Worker {

	private final ScriptEngine engine;
    private final ScheduledExecutorService pool;
    private final Map<String, Future<?>> tasks;
    private int count = 1;

    public Worker(ScriptEngine engine) {
	    this.engine = engine;
	    this.pool = Executors.newScheduledThreadPool(1);
	    this.tasks = new HashMap<>();
    }

	public int setTimeout(Object fn, int delay) {
	    Future<?> f = pool.schedule(() -> engine.callFunction(fn), delay, TimeUnit.MILLISECONDS);
        int id = count++;
	    tasks.put("T"+id, f);
        return id;
	}

	public int setInterval(Object fn, int delay) {
	    Future<?> f = pool.scheduleAtFixedRate(() -> engine.callFunction(fn), delay, delay, TimeUnit.MILLISECONDS);
        int id = count++;
        tasks.put("I"+id, f);
	    return id;
	}

	public void clearTimeout(int id) {
		cancelTask("T"+id);
	}

	public void clearInterval(int id) {
		cancelTask("I"+id);
	}

	private void cancelTask(String id) {
	    if (tasks.containsKey(id)) {
	        tasks.remove(id).cancel(false);
	    }
	}
}
