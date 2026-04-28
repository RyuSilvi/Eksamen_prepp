package multithreads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.ArrayList;

/**
 * Example 2 – Thread Pool with ExecutorService
 *
 * Creating a new Thread for every task is expensive.
 * A thread pool keeps a fixed number of threads alive and reuses them.
 *
 * Key ExecutorService factory methods (Executors class):
 *   newFixedThreadPool(n)        – exactly n threads
 *   newCachedThreadPool()        – grows/shrinks automatically
 *   newSingleThreadExecutor()    – single background thread, tasks queued
 *   newScheduledThreadPool(n)    – for delayed / periodic tasks
 *
 * Key concepts:
 *   Runnable  – no return value, cannot throw checked exceptions
 *   Callable  – returns a value (Future<T>), can throw checked exceptions
 *   Future<T> – placeholder for a result that is not yet available
 */
public class ThreadPoolExample {

    // =========================================================================
    // A simple task that simulates work and returns a result
    // =========================================================================
    static class NumberSquarer implements Callable<Integer> {
        private final int number;

        NumberSquarer(int number) { this.number = number; }

        @Override
        public Integer call() throws Exception {
            System.out.printf("[%s] Squaring %d%n",
                    Thread.currentThread().getName(), number);
            Thread.sleep(200);          // simulate work
            return number * number;
        }
    }

    // =========================================================================
    // Demo 1 – fixed thread pool with Runnable tasks
    // =========================================================================
    static void fixedPoolDemo() throws InterruptedException {
        System.out.println("\n--- Fixed thread pool (3 threads, 6 tasks) ---");

        ExecutorService pool = Executors.newFixedThreadPool(3);

        for (int i = 1; i <= 6; i++) {
            final int taskId = i;
            pool.execute(() -> {   // execute() accepts Runnable
                System.out.printf("[%s] Running task %d%n",
                        Thread.currentThread().getName(), taskId);
                try { Thread.sleep(300); } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // Gracefully shut down: no new tasks accepted; existing tasks finish
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println("Fixed pool finished.");
    }

    // =========================================================================
    // Demo 2 – submit Callable tasks and collect Future results
    // =========================================================================
    static void callableFutureDemo() throws Exception {
        System.out.println("\n--- Callable + Future (squared numbers) ---");

        ExecutorService pool = Executors.newFixedThreadPool(4);

        List<Future<Integer>> futures = new ArrayList<>();
        for (int n = 1; n <= 8; n++) {
            futures.add(pool.submit(new NumberSquarer(n)));  // submit() returns Future
        }

        // Collect results – get() blocks until the result is ready
        for (int i = 0; i < futures.size(); i++) {
            int result = futures.get(i).get();  // blocks here
            System.out.printf("  %d^2 = %d%n", i + 1, result);
        }

        pool.shutdown();
        System.out.println("Callable pool finished.");
    }

    // =========================================================================
    // Demo 3 – single-thread executor (tasks run one at a time, in order)
    // =========================================================================
    static void singleThreadDemo() throws InterruptedException {
        System.out.println("\n--- Single-thread executor (sequential tasks) ---");

        ExecutorService single = Executors.newSingleThreadExecutor();

        for (int i = 1; i <= 4; i++) {
            final int id = i;
            single.execute(() ->
                System.out.printf("[%s] Sequential task %d%n",
                        Thread.currentThread().getName(), id));
        }

        single.shutdown();
        single.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("Single-thread executor finished.");
    }

    // =========================================================================
    // main
    // =========================================================================
    public static void main(String[] args) throws Exception {
        fixedPoolDemo();
        callableFutureDemo();
        singleThreadDemo();
        System.out.println("\nmain() finished.");
    }
}
