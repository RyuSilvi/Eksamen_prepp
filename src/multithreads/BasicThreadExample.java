package multithreads;

/**
 * Example 1 – Two ways to create and start a Thread
 *
 * Java gives you two basic approaches:
 *   A) Extend the Thread class and override run()
 *   B) Implement the Runnable interface and pass it to a Thread
 *
 * Approach B is preferred in most cases because:
 *   - Your class can still extend another class
 *   - The same Runnable can be reused across multiple Thread objects
 *   - It pairs naturally with ExecutorService (see ThreadPoolExample)
 */
public class BasicThreadExample {

    // =========================================================================
    // Approach A – extend Thread
    // =========================================================================
    static class CounterThread extends Thread {

        private final String threadName;
        private final int    limit;

        CounterThread(String threadName, int limit) {
            super(threadName);          // sets the thread's name (visible in logs/debugger)
            this.threadName = threadName;
            this.limit      = limit;
        }

        @Override
        public void run() {
            for (int i = 1; i <= limit; i++) {
                System.out.println(threadName + " -> " + i);
                try {
                    Thread.sleep(50);   // pause 50 ms between each print
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // restore interrupted status
                    System.out.println(threadName + " was interrupted!");
                    return;
                }
            }
            System.out.println(threadName + " finished.");
        }
    }

    // =========================================================================
    // Approach B – implement Runnable
    // =========================================================================
    static class PrintTask implements Runnable {

        private final String message;
        private final int    repeat;

        PrintTask(String message, int repeat) {
            this.message = message;
            this.repeat  = repeat;
        }

        @Override
        public void run() {
            for (int i = 0; i < repeat; i++) {
                System.out.println(Thread.currentThread().getName() + " | " + message);
                try {
                    Thread.sleep(80);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    // =========================================================================
    // main
    // =========================================================================
    public static void main(String[] args) throws InterruptedException {

        System.out.println("=== Approach A: Extend Thread ===");
        Thread t1 = new CounterThread("Thread-A", 5);
        Thread t2 = new CounterThread("Thread-B", 5);

        t1.start();     // start() creates a new OS thread and calls run()
        t2.start();     // do NOT call run() directly – that executes on the current thread!

        t1.join();      // main thread waits until t1 finishes
        t2.join();

        System.out.println("\n=== Approach B: Implement Runnable ===");
        Thread t3 = new Thread(new PrintTask("Hello from Runnable", 4), "Runnable-1");
        Thread t4 = new Thread(new PrintTask("World from Runnable", 4), "Runnable-2");

        t3.start();
        t4.start();

        t3.join();
        t4.join();

        // Lambda shorthand (Runnable is a functional interface)
        System.out.println("\n=== Lambda Runnable ===");
        Thread t5 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                System.out.println("Lambda thread: " + i);
            }
        }, "Lambda-Thread");

        t5.start();
        t5.join();

        System.out.println("main() finished.");
    }
}
