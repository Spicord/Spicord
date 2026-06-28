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
        tasks.put("T" + id, f);
        return id;
    }

    public int setInterval(Object fn, int delay) {
        Future<?> f = pool.scheduleAtFixedRate(() -> engine.callFunction(fn), delay, delay, TimeUnit.MILLISECONDS);
        int id = count++;
        tasks.put("I" + id, f);
        return id;
    }

    public void clearTimeout(int id) {
        cancelTask("T" + id);
    }

    public void clearInterval(int id) {
        cancelTask("I" + id);
    }

    private void cancelTask(String id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id).cancel(false);
        }
    }
}
