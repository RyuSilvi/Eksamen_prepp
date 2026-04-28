package multithreads;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Example 3 – Multithreaded Restaurant System
 *
 * Simulates a restaurant with three types of concurrent actors:
 *
 *   Customer  – arrives, places an order (adds to the order queue)
 *   Waiter    – picks up customer orders and passes them to the kitchen
 *   Chef      – cooks orders from the kitchen queue
 *
 * Key concurrency concepts demonstrated:
 *   - Producer/Consumer pattern with a shared queue
 *   - synchronized methods / blocks to protect shared state
 *   - wait() / notifyAll() for inter-thread communication
 *   - ExecutorService thread pool to manage the threads
 */
public class RestaurantSystem {

    // =========================================================================
    // Shared order queues (the critical sections)
    // =========================================================================

    /** Orders placed by customers, waiting to be picked up by a waiter. */
    static final Queue<String> orderQueue   = new LinkedList<>();

    /** Orders handed to the kitchen by the waiter, waiting to be cooked. */
    static final Queue<String> kitchenQueue = new LinkedList<>();

    static final int MAX_QUEUE_SIZE  = 5;  // max orders allowed in each queue
    static final Object orderLock   = new Object();
    static final Object kitchenLock = new Object();

    // =========================================================================
    // Customer – places orders into orderQueue
    // =========================================================================
    static class Customer implements Runnable {
        private final String  name;
        private final int     ordersToPlace;
        private final Random  random = new Random();
        private static final String[] MENU = {
                "Burger", "Pizza", "Pasta", "Salad", "Steak", "Sushi"
        };

        Customer(String name, int ordersToPlace) {
            this.name          = name;
            this.ordersToPlace = ordersToPlace;
        }

        @Override
        public void run() {
            for (int i = 0; i < ordersToPlace; i++) {
                String dish = MENU[random.nextInt(MENU.length)];
                String order = name + " orders " + dish;

                synchronized (orderLock) {
                    // Wait if the queue is full (waiter hasn't caught up yet)
                    while (orderQueue.size() >= MAX_QUEUE_SIZE) {
                        try {
                            System.out.println(name + " is waiting – order queue full");
                            orderLock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    orderQueue.add(order);
                    System.out.println("[Customer]  " + order + " (queue=" + orderQueue.size() + ")");
                    orderLock.notifyAll();  // wake up the waiter
                }

                // Small pause before placing next order
                try { Thread.sleep(random.nextInt(300) + 100); }
                catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
            }
        }
    }

    // =========================================================================
    // Waiter – picks orders from orderQueue and puts them into kitchenQueue
    // =========================================================================
    static class Waiter implements Runnable {
        private final int  ordersToHandle;
        private final Random random = new Random();

        Waiter(int ordersToHandle) {
            this.ordersToHandle = ordersToHandle;
        }

        @Override
        public void run() {
            int handled = 0;
            while (handled < ordersToHandle) {
                String order = null;

                // Step 1 – pick up order from customer queue
                synchronized (orderLock) {
                    while (orderQueue.isEmpty()) {
                        try {
                            orderLock.wait(500);   // wait up to 500 ms
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    order = orderQueue.poll();
                    orderLock.notifyAll();   // wake up waiting customers
                }

                if (order == null) continue;

                // Step 2 – pass order to the kitchen queue
                synchronized (kitchenLock) {
                    while (kitchenQueue.size() >= MAX_QUEUE_SIZE) {
                        try {
                            System.out.println("[Waiter]    Waiting – kitchen queue full");
                            kitchenLock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    kitchenQueue.add(order);
                    System.out.println("[Waiter]    Sent to kitchen: " + order);
                    kitchenLock.notifyAll();  // wake up the chef
                }

                handled++;
                try { Thread.sleep(random.nextInt(200) + 50); }
                catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
            }
        }
    }

    // =========================================================================
    // Chef – cooks orders from kitchenQueue
    // =========================================================================
    static class Chef implements Runnable {
        private final int  ordersToCook;
        private final Random random = new Random();

        Chef(int ordersToCook) { this.ordersToCook = ordersToCook; }

        @Override
        public void run() {
            int cooked = 0;
            while (cooked < ordersToCook) {
                String order = null;

                synchronized (kitchenLock) {
                    while (kitchenQueue.isEmpty()) {
                        try {
                            kitchenLock.wait(500);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    order = kitchenQueue.poll();
                    kitchenLock.notifyAll();  // wake up waiting waiter
                }

                if (order == null) continue;

                // Simulate cooking time
                try { Thread.sleep(random.nextInt(400) + 200); }
                catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }

                System.out.println("[Chef]      DONE: " + order);
                cooked++;
            }
        }
    }

    // =========================================================================
    // main – wire everything together
    // =========================================================================
    public static void main(String[] args) throws InterruptedException {
        final int TOTAL_ORDERS = 12;

        ExecutorService pool = Executors.newFixedThreadPool(6);

        // 3 customers, each placing 4 orders (= 12 total)
        pool.execute(new Customer("Alice",   4));
        pool.execute(new Customer("Bob",     4));
        pool.execute(new Customer("Charlie", 4));

        // 1 waiter handles all 12 orders
        pool.execute(new Waiter(TOTAL_ORDERS));

        // 2 chefs share the cooking; each handles ~6 orders
        pool.execute(new Chef(TOTAL_ORDERS / 2));
        pool.execute(new Chef(TOTAL_ORDERS / 2));

        pool.shutdown();
        pool.awaitTermination(30, TimeUnit.SECONDS);
        System.out.println("\nRestaurant closed for the day.");
    }
}
