package concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;

/**
 * Example 2 – CountDownLatch and CyclicBarrier
 *
 * Both tools let threads wait for each other, but they work differently:
 *
 *  CountDownLatch – "start gun" or "all done" signal.
 *    A latch is initialised with a count.
 *    Threads call countDown() to decrement the count.
 *    Any thread that calls await() blocks until the count reaches zero.
 *    A latch CANNOT be reset.
 *
 *  CyclicBarrier  – "meeting point".
 *    All threads must reach the barrier before any of them can continue.
 *    After the barrier is released it automatically resets, so it can be
 *    used in loops (hence "cyclic").
 *
 * Demo A (Latch)   – Race start: all runners wait for the starter gun.
 * Demo B (Latch)   – Service startup: main waits until all services are ready.
 * Demo C (Barrier) – Multi-phase computation: threads sync at the end of each phase.
 */
public class CountDownLatchAndBarrierExample {

    // =========================================================================
    // Demo A – Starter Gun (CountDownLatch as a "go" signal)
    // =========================================================================
    static void starterGunDemo() throws InterruptedException {
        System.out.println("=== Demo A: Starter Gun ===");

        final CountDownLatch startSignal = new CountDownLatch(1);

        for (int i = 1; i <= 5; i++) {
            final String runnerName = "Runner-" + i;
            new Thread(() -> {
                System.out.println(runnerName + " is ready and waiting...");
                try {
                    startSignal.await();            // block until gun fires
                    System.out.println(runnerName + " is RUNNING!");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }

        Thread.sleep(500);  // let all runners get into position
        System.out.println("BANG! Starting gun fired.");
        startSignal.countDown();   // count goes from 1 → 0, all runners released
        Thread.sleep(500);
    }

    // =========================================================================
    // Demo B – Wait for all services to be ready (CountDownLatch as "done" signal)
    // =========================================================================
    static void serviceStartupDemo() throws InterruptedException {
        System.out.println("\n=== Demo B: Service Startup ===");

        String[] services = {"DatabaseService", "CacheService", "AuthService"};
        CountDownLatch readyLatch = new CountDownLatch(services.length);

        for (String service : services) {
            new Thread(() -> {
                System.out.println(service + " starting...");
                try { Thread.sleep((long)(Math.random() * 1000) + 500); }
                catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                System.out.println(service + " is READY");
                readyLatch.countDown();     // signal that this service is up
            }, service).start();
        }

        System.out.println("Main is waiting for all services...");
        readyLatch.await();                 // blocks until all services are ready
        System.out.println("All services ready – application starting!");
    }

    // =========================================================================
    // Demo C – CyclicBarrier: parallel computation with synchronisation phases
    // =========================================================================
    static void cyclicBarrierDemo() throws InterruptedException {
        System.out.println("\n=== Demo C: CyclicBarrier (2 phases) ===");

        final int THREADS = 3;
        // The barrier action runs once when all threads arrive at the barrier
        CyclicBarrier barrier = new CyclicBarrier(THREADS,
                () -> System.out.println(">>> All threads reached the barrier – next phase!"));

        for (int i = 1; i <= THREADS; i++) {
            final int id = i;
            new Thread(() -> {
                try {
                    // --- Phase 1 ---
                    System.out.println("Thread-" + id + " doing Phase-1 work");
                    Thread.sleep((long)(Math.random() * 500) + 200);
                    System.out.println("Thread-" + id + " finished Phase-1");
                    barrier.await();    // wait for everyone

                    // --- Phase 2 ---
                    System.out.println("Thread-" + id + " doing Phase-2 work");
                    Thread.sleep((long)(Math.random() * 500) + 200);
                    System.out.println("Thread-" + id + " finished Phase-2");
                    barrier.await();    // wait for everyone again

                    System.out.println("Thread-" + id + " all done!");

                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Worker-" + i).start();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        starterGunDemo();
        serviceStartupDemo();
        cyclicBarrierDemo();
        Thread.sleep(2000);   // let cyclic barrier threads finish before main exits
        System.out.println("\nmain() finished.");
    }
}
