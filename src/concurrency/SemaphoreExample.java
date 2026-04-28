package concurrency;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Example 1 – Semaphore
 *
 * A Semaphore controls access to a shared resource by maintaining a count of
 * "permits".  A thread must acquire a permit before accessing the resource,
 * and releases it afterwards.  If no permits are available the thread blocks
 * until another thread releases one.
 *
 * Classic use-case: limit the number of concurrent database connections,
 * parking spots, API calls, etc.
 *
 * Demo: A parking lot with only 3 spaces.  10 cars try to park.
 */
public class SemaphoreExample {

    // Only 3 cars can be parked at the same time
    private static final Semaphore parkingLot = new Semaphore(3);

    static class Car implements Runnable {
        private final String name;

        Car(String name) { this.name = name; }

        @Override
        public void run() {
            System.out.println(name + " is waiting for a parking space...");

            try {
                parkingLot.acquire();           // blocks until a space is free

                System.out.println(name + " parked!  Available spaces: "
                        + parkingLot.availablePermits());

                // Simulate time spent parked
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));

                System.out.println(name + " is leaving.");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                parkingLot.release();           // ALWAYS release in finally
                System.out.println(name + " freed a space.  Available: "
                        + parkingLot.availablePermits());
            }
        }
    }

    // -------------------------------------------------------------------------
    // Binary semaphore – acts like a mutex (mutual exclusion lock)
    // -------------------------------------------------------------------------
    static void binarySemaphoreDemo() throws InterruptedException {
        System.out.println("\n--- Binary Semaphore (mutex) demo ---");

        Semaphore mutex = new Semaphore(1);   // only 1 permit = mutex
        int[] sharedCounter = {0};            // array trick to allow lambda mutation

        Runnable incrementTask = () -> {
            for (int i = 0; i < 5; i++) {
                try {
                    mutex.acquire();          // enter critical section
                    sharedCounter[0]++;
                    System.out.printf("[%s] counter = %d%n",
                            Thread.currentThread().getName(), sharedCounter[0]);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    mutex.release();          // leave critical section
                }
            }
        };

        Thread t1 = new Thread(incrementTask, "MutexThread-1");
        Thread t2 = new Thread(incrementTask, "MutexThread-2");
        t1.start(); t2.start();
        t1.join();  t2.join();

        System.out.println("Final counter: " + sharedCounter[0]);   // always 10
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Parking Lot (Semaphore with 3 permits) ===");

        Thread[] cars = new Thread[10];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Thread(new Car("Car-" + (i + 1)));
            cars[i].start();
        }
        for (Thread car : cars) car.join();

        binarySemaphoreDemo();
        System.out.println("\nmain() finished.");
    }
}
