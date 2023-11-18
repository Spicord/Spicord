package org.spicord.util.sched;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SpicordSchedulerV1 implements ScheduledExecutorService {

    private boolean isShutdown = false;

    public SpicordSchedulerV1() {
    }

    @Override
    public void execute(Runnable command) {
        if (isShutdown) {
            return;
        }
        FutureTask.start(toCallable(command), null, null, null, null);
    }

    @Override
    public boolean isShutdown() {
        return isShutdown;
    }

    @Override
    public boolean isTerminated() {
        return isShutdown; // TODO
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return FutureTask.start(task, null, null, null, null);
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return FutureTask.start(toCallable(task, result), null, null, null, null);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return FutureTask.start(toCallable(task), null, null, null, null);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        List<Future<T>> list = new ArrayList<>();
        for (Callable<T> task : tasks) {
            Future<T> future = submit(task);
            list.add(future);
        }
        return list;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return invokeAll(tasks); // TODO: Implement timeout
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        Exception lastEx = null;
        for (Callable<T> task : tasks) {
            try {
                return task.call(); // TODO: Run tasks in parallel
            } catch (Exception e) {
                lastEx = e;
            }
        }
        throw new ExecutionException(lastEx);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return invokeAny(tasks); // TODO: Implement timeout
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return FutureTask.start(toCallable(command), delay, null, null, unit);
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return FutureTask.start(callable, delay, null, null, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return FutureTask.start(toCallable(command), initialDelay, null, period, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return FutureTask.start(toCallable(command), initialDelay, delay, null, unit);
    }

    private static <V> Callable<V> toCallable(Runnable command) {
        return toCallable(command, null);
    }

    private static <V> Callable<V> toCallable(Runnable command, V result) {
        return () -> { command.run(); return result; };
    }

    // ============================================================

    @Override
    public void shutdown() {
        Iterator<Thread> i = FutureTask.alive.iterator(); // TODO: Proper thread tracking

        while (i.hasNext()) {
            Thread t = i.next();
            i.remove();
            t.interrupt();
        }

        isShutdown = true;
    }

    @Override
    public List<Runnable> shutdownNow() {
        shutdown();
        return Collections.emptyList(); // TODO: Return list of non-executed tasks
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return true; // TODO
    }
}
