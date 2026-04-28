package threadlocks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Example 1 – ReentrantLock and ReadWriteLock
 *
 * ReentrantLock is an explicit lock that gives more control than synchronized:
 *   - tryLock()              – attempt to acquire without blocking
 *   - lockInterruptibly()    – can be interrupted while waiting
 *   - fair ordering option   – threads get the lock in arrival order
 *
 * ReadWriteLock distinguishes between:
 *   - Read lock   – multiple threads can hold it simultaneously (no writes happening)
 *   - Write lock  – exclusive; no other readers or writers allowed
 *   Ideal for data structures that are read frequently but written rarely.
 */
public class ReentrantLockExample {

    // =========================================================================
    // Demo 1 – ReentrantLock: safe counter
    // =========================================================================
    static class SafeCounter {
        private int  count = 0;
        private final Lock lock = new ReentrantLock();

        public void increment() {
            lock.lock();
            try {
                count++;
            } finally {
                lock.unlock();  // ALWAYS unlock in finally to avoid lock leaks
            }
        }

        public int get() {
            lock.lock();
            try {
                return count;
            } finally {
                lock.unlock();
            }
        }
    }

    static void reentrantLockDemo() throws InterruptedException {
        System.out.println("=== Demo 1: ReentrantLock ===");

        SafeCounter counter = new SafeCounter();

        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) counter.increment();
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();

        System.out.println("Final count (should be 10000): " + counter.get());
    }

    // =========================================================================
    // Demo 2 – tryLock() to avoid blocking indefinitely
    // =========================================================================
    static void tryLockDemo() {
        System.out.println("\n=== Demo 2: tryLock() ===");

        Lock lock = new ReentrantLock();

        Runnable task = () -> {
            if (lock.tryLock()) {
                try {
                    System.out.println(Thread.currentThread().getName() + " acquired lock");
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock();
                    System.out.println(Thread.currentThread().getName() + " released lock");
                }
            } else {
                System.out.println(Thread.currentThread().getName()
                        + " could not acquire lock – doing something else");
            }
        };

        new Thread(task, "TryLock-1").start();
        new Thread(task, "TryLock-2").start();
    }

    // =========================================================================
    // Demo 3 – ReadWriteLock: many readers, one writer
    // =========================================================================
    static class SharedData {
        private String value = "initial";
        private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
        private final Lock readLock  = rwLock.readLock();
        private final Lock writeLock = rwLock.writeLock();

        public String read() {
            readLock.lock();
            try {
                System.out.println("[" + Thread.currentThread().getName() + "] reading: " + value);
                Thread.sleep(100);
                return value;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            } finally {
                readLock.unlock();
            }
        }

        public void write(String newValue) {
            writeLock.lock();
            try {
                System.out.println("[" + Thread.currentThread().getName() + "] writing: " + newValue);
                Thread.sleep(200);
                value = newValue;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                writeLock.unlock();
            }
        }
    }

    static void readWriteLockDemo() throws InterruptedException {
        System.out.println("\n=== Demo 3: ReadWriteLock ===");

        SharedData data = new SharedData();

        // 5 readers + 2 writers
        Thread[] threads = new Thread[7];
        for (int i = 0; i < 5; i++) {
            threads[i] = new Thread(() -> data.read(), "Reader-" + (i + 1));
        }
        threads[5] = new Thread(() -> data.write("updated-by-writer-1"), "Writer-1");
        threads[6] = new Thread(() -> data.write("updated-by-writer-2"), "Writer-2");

        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
    }

    public static void main(String[] args) throws InterruptedException {
        reentrantLockDemo();
        tryLockDemo();
        readWriteLockDemo();
        System.out.println("\nmain() finished.");
    }
}
