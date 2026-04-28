package threadlocks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Example 2 – Deadlock: Demonstration and Prevention
 *
 * WHAT IS A DEADLOCK?
 *   Thread A holds Lock-1 and is waiting for Lock-2.
 *   Thread B holds Lock-2 and is waiting for Lock-1.
 *   Neither can proceed → deadlock.
 *
 * THREE PREVENTION STRATEGIES SHOWN:
 *   1. Lock ordering   – always acquire locks in the same global order
 *   2. tryLock()       – attempt to acquire; back off and retry if not available
 *   3. One coarse lock – use a single lock instead of two fine-grained ones
 *                        (simplest; reduces concurrency but eliminates deadlock)
 */
public class DeadlockPreventionExample {

    // =========================================================================
    // Deadlock DEMONSTRATION (do not use this pattern in production!)
    // =========================================================================
    static class DeadlockDemo {
        private final Object lock1 = new Object();
        private final Object lock2 = new Object();

        /** Thread A: acquires lock1 then tries to get lock2 */
        void methodA() throws InterruptedException {
            synchronized (lock1) {
                System.out.println("Thread A: holding lock1, waiting for lock2...");
                Thread.sleep(50);   // give Thread B time to acquire lock2
                synchronized (lock2) {
                    System.out.println("Thread A: acquired both locks");
                }
            }
        }

        /** Thread B: acquires lock2 then tries to get lock1 – opposite order! */
        void methodB() throws InterruptedException {
            synchronized (lock2) {
                System.out.println("Thread B: holding lock2, waiting for lock1...");
                Thread.sleep(50);
                synchronized (lock1) {
                    System.out.println("Thread B: acquired both locks");
                }
            }
        }

        /**
         * This will deadlock.  The threads are interrupted after 3 seconds
         * so that the program can continue to the prevention demos.
         */
        void demonstrate() throws InterruptedException {
            System.out.println("=== Deadlock DEMONSTRATION (will deadlock, then be interrupted) ===");

            Thread threadA = new Thread(() -> {
                try { methodA(); } catch (InterruptedException e) {
                    System.out.println("Thread A interrupted (deadlock broken)");
                    Thread.currentThread().interrupt();
                }
            }, "Thread-A");

            Thread threadB = new Thread(() -> {
                try { methodB(); } catch (InterruptedException e) {
                    System.out.println("Thread B interrupted (deadlock broken)");
                    Thread.currentThread().interrupt();
                }
            }, "Thread-B");

            threadA.start();
            threadB.start();

            // Give threads 2 seconds then forcibly interrupt them
            Thread.sleep(2000);
            threadA.interrupt();
            threadB.interrupt();
            threadA.join();
            threadB.join();
            System.out.println("Deadlock demo finished (threads interrupted).\n");
        }
    }

    // =========================================================================
    // Prevention Strategy 1 – LOCK ORDERING
    // Both threads always acquire the locks in the same order (lock1 then lock2).
    // =========================================================================
    static class LockOrderingFix {
        private final Object lock1 = new Object();
        private final Object lock2 = new Object();

        void method() throws InterruptedException {
            synchronized (lock1) {         // same order in BOTH threads
                System.out.println(Thread.currentThread().getName() + ": got lock1");
                Thread.sleep(50);
                synchronized (lock2) {
                    System.out.println(Thread.currentThread().getName() + ": got lock2 (safe)");
                }
            }
        }

        void demonstrate() throws InterruptedException {
            System.out.println("=== Prevention 1: Lock Ordering ===");

            Thread t1 = new Thread(() -> { try { method(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); } }, "P1-Thread-A");
            Thread t2 = new Thread(() -> { try { method(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); } }, "P1-Thread-B");
            t1.start(); t2.start();
            t1.join();  t2.join();
            System.out.println();
        }
    }

    // =========================================================================
    // Prevention Strategy 2 – tryLock() WITH TIMEOUT / BACK-OFF
    // If a thread can't get both locks it releases the one it has and retries.
    // =========================================================================
    static class TryLockFix {
        private final Lock lock1 = new ReentrantLock();
        private final Lock lock2 = new ReentrantLock();

        void method(String name) throws InterruptedException {
            while (true) {
                boolean got1 = false;
                boolean got2 = false;
                try {
                    got1 = lock1.tryLock();
                    got2 = lock2.tryLock();
                } finally {
                    if (got1 && got2) {
                        try {
                            System.out.println(name + ": working with both locks");
                            Thread.sleep(100);
                            return;  // done
                        } finally {
                            lock1.unlock();
                            lock2.unlock();
                        }
                    }
                    // Could not get both – release whatever we have and back off
                    if (got1) lock1.unlock();
                    if (got2) lock2.unlock();
                }
                System.out.println(name + ": could not acquire both locks, retrying...");
                Thread.sleep(10);   // small back-off to reduce contention
            }
        }

        void demonstrate() throws InterruptedException {
            System.out.println("=== Prevention 2: tryLock() with back-off ===");

            Thread t1 = new Thread(() -> { try { method("TL-Thread-A"); } catch (InterruptedException e) { Thread.currentThread().interrupt(); } });
            Thread t2 = new Thread(() -> { try { method("TL-Thread-B"); } catch (InterruptedException e) { Thread.currentThread().interrupt(); } });
            t1.start(); t2.start();
            t1.join();  t2.join();
            System.out.println();
        }
    }

    // =========================================================================
    // main
    // =========================================================================
    public static void main(String[] args) throws InterruptedException {
        new DeadlockDemo().demonstrate();
        new LockOrderingFix().demonstrate();
        new TryLockFix().demonstrate();
        System.out.println("All demos finished.");
    }
}
