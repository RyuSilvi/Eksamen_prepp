package threadlocks;

/**
 * Example 3 – synchronized keyword (built-in Java monitor lock)
 *
 * The synchronized keyword is the simplest way to protect shared state.
 * It uses each object's built-in "monitor" lock.
 *
 * Forms:
 *   synchronized method        – locks on 'this' for the duration of the method
 *   synchronized(object) block – locks on a specific object (narrower scope)
 *   synchronized static method – locks on the Class object (shared across instances)
 *
 * Key rules:
 *   - Only ONE thread can hold a monitor lock at a time
 *   - Releasing the lock happens automatically when the block/method exits
 *   - Java's synchronized is REENTRANT: the same thread can re-acquire a lock it holds
 */
public class SynchronizedExample {

    // =========================================================================
    // Demo 1 – Without synchronization (race condition)
    // =========================================================================
    static class UnsafeCounter {
        private int count = 0;

        public void increment() { count++; }   // NOT thread-safe!
        public int  get()       { return count; }
    }

    // =========================================================================
    // Demo 2 – Synchronized method
    // =========================================================================
    static class SyncMethodCounter {
        private int count = 0;

        public synchronized void increment() { count++; }
        public synchronized int  get()       { return count; }
    }

    // =========================================================================
    // Demo 3 – Synchronized block (finer-grained locking)
    // =========================================================================
    static class SyncBlockCounter {
        private int count = 0;
        private final Object lock = new Object();

        public void increment() {
            // Only the critical section is locked, not the whole method
            synchronized (lock) {
                count++;
            }
        }

        public int get() {
            synchronized (lock) {
                return count;
            }
        }
    }

    // =========================================================================
    // Demo 4 – wait() / notify() pattern (condition variable)
    // =========================================================================
    static class BoundedBuffer {
        private final int[]  buffer;
        private int          count = 0, putIndex = 0, takeIndex = 0;

        BoundedBuffer(int capacity) { buffer = new int[capacity]; }

        /** Producer calls this – blocks when buffer is full */
        public synchronized void put(int value) throws InterruptedException {
            while (count == buffer.length) {
                System.out.println("[Buffer] Full – producer waiting");
                wait();     // releases the lock and suspends this thread
            }
            buffer[putIndex] = value;
            putIndex = (putIndex + 1) % buffer.length;
            count++;
            System.out.println("[Buffer] Put " + value + ", size=" + count);
            notifyAll(); // wake up any waiting consumers
        }

        /** Consumer calls this – blocks when buffer is empty */
        public synchronized int take() throws InterruptedException {
            while (count == 0) {
                System.out.println("[Buffer] Empty – consumer waiting");
                wait();
            }
            int value = buffer[takeIndex];
            takeIndex = (takeIndex + 1) % buffer.length;
            count--;
            System.out.println("[Buffer] Took " + value + ", size=" + count);
            notifyAll(); // wake up any waiting producers
            return value;
        }
    }

    // =========================================================================
    // main
    // =========================================================================
    public static void main(String[] args) throws InterruptedException {

        final int INCREMENTS = 10_000;

        // --- Race condition demo ---
        System.out.println("=== Demo 1: Unsafe counter (race condition) ===");
        UnsafeCounter unsafe = new UnsafeCounter();
        Thread u1 = new Thread(() -> { for (int i=0;i<INCREMENTS;i++) unsafe.increment(); });
        Thread u2 = new Thread(() -> { for (int i=0;i<INCREMENTS;i++) unsafe.increment(); });
        u1.start(); u2.start(); u1.join(); u2.join();
        System.out.println("Expected " + (2 * INCREMENTS) + ", got: " + unsafe.get()
                + " (may be less due to race!)");

        // --- Synchronized method ---
        System.out.println("\n=== Demo 2: Synchronized method ===");
        SyncMethodCounter safe1 = new SyncMethodCounter();
        Thread s1 = new Thread(() -> { for (int i=0;i<INCREMENTS;i++) safe1.increment(); });
        Thread s2 = new Thread(() -> { for (int i=0;i<INCREMENTS;i++) safe1.increment(); });
        s1.start(); s2.start(); s1.join(); s2.join();
        System.out.println("Expected " + (2 * INCREMENTS) + ", got: " + safe1.get());

        // --- Synchronized block ---
        System.out.println("\n=== Demo 3: Synchronized block ===");
        SyncBlockCounter safe2 = new SyncBlockCounter();
        Thread b1 = new Thread(() -> { for (int i=0;i<INCREMENTS;i++) safe2.increment(); });
        Thread b2 = new Thread(() -> { for (int i=0;i<INCREMENTS;i++) safe2.increment(); });
        b1.start(); b2.start(); b1.join(); b2.join();
        System.out.println("Expected " + (2 * INCREMENTS) + ", got: " + safe2.get());

        // --- wait / notify ---
        System.out.println("\n=== Demo 4: wait() / notifyAll() with BoundedBuffer ===");
        BoundedBuffer buf = new BoundedBuffer(3);
        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 6; i++) {
                try { buf.put(i); Thread.sleep(100); }
                catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        });
        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 6; i++) {
                try { buf.take(); Thread.sleep(200); }
                catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        });
        producer.start(); consumer.start();
        producer.join();  consumer.join();

        System.out.println("\nmain() finished.");
    }
}
