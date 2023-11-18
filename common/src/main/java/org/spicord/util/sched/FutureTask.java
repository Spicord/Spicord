package org.spicord.util.sched;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureTask<T> extends Thread implements ScheduledFuture<T> {

    static final Set<Thread> alive = new HashSet<>();

    private long startTime;
    private boolean cancelled = false;
    private boolean done = false;
    private Object doneLock = new Object(); 
    private T result = null;
    private ExecutionException error;

    private Callable<T> task;
    private Long initialDelay;
    private Long delayBetweenOps;
    private Long period;
    private TimeUnit unit;

    private FutureTask(Callable<T> task, Long initialDelay, Long delayBetweenOps, Long period, TimeUnit unit) {
        this.task = task;
        this.initialDelay = initialDelay;
        this.delayBetweenOps = delayBetweenOps;
        this.period = period;
        this.unit = unit;
    }

    @Override
    public void run() {
        alive.add(this);

        startTime = System.currentTimeMillis();

        try {
            if (initialDelay != null) {
                sleep(unit.toMillis(initialDelay));
            }

            if (period != null) {
                final long periodMillis = unit.toMillis(period);

                while (true) {
                    long start = System.currentTimeMillis();

                    task.call();

                    long end = System.currentTimeMillis();

                    long time = end - start;

                    long diff = periodMillis - time;

                    if (diff > 0) {
                        sleep(diff);
                    }
                }

            } else if (delayBetweenOps != null) {
                final long delayMillis = unit.toMillis(delayBetweenOps);

                while (true) {
                    task.call();

                    sleep(delayMillis);
                }
            } else {
                result = task.call();
            }
        } catch (Throwable e) {
            if (e instanceof InterruptedException) {
                cancelled = true;
            } else {
                error = new ExecutionException(e);
            }
        }

        synchronized (doneLock) {
            alive.remove(this);

            done = true;
            doneLock.notifyAll();
        }
    }

    @Override
    public long getDelay(TimeUnit otherUnit) {
        long now = System.currentTimeMillis();

        long delayMs = unit.toMillis(initialDelay);

        long resultMs = now - (startTime + delayMs);

        return otherUnit.convert(resultMs, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        TimeUnit theUnit = TimeUnit.MILLISECONDS;

        long other = o.getDelay(theUnit);
        long me = getDelay(theUnit);

        return (int) (me - other);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (mayInterruptIfRunning && isAlive()) {
            interrupt();

            cancelled = true;

            return true;
        }

        return false;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        try {
            return get(0, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        synchronized (doneLock) {
            if (!done) {
                doneLock.wait(unit.toMillis(timeout));

                if (!done) {
                    throw new TimeoutException();
                }
            }
            if (error != null) {
                throw error;
            }
            return result;
        }
    }

    public static <T> FutureTask<T> start(Callable<T> task, Long initialDelay, Long delayBetweenOps, Long period, TimeUnit unit) {
        FutureTask<T> future = new FutureTask<>(task, initialDelay, delayBetweenOps, period, unit);
        ((Thread) future).start();
        return future;
    }
}
