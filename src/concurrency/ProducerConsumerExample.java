package concurrency;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Example 3 – Producer / Consumer with BlockingQueue and AtomicInteger
 *
 * BlockingQueue:
 *   A thread-safe queue where:
 *     put()  blocks when the queue is full
 *     take() blocks when the queue is empty
 *   No manual synchronization needed – the queue handles it internally.
 *
 * AtomicInteger:
 *   A counter that can be incremented, decremented, etc. without a
 *   synchronized block.  Uses CPU-level Compare-And-Swap (CAS) operations.
 *
 * Demo: a data pipeline.
 *   2 Producers generate numbers and put them into the queue.
 *   3 Consumers take numbers from the queue and process them.
 *   A poison-pill value (-1) signals consumers to stop.
 */
public class ProducerConsumerExample {

    private static final int QUEUE_CAPACITY  = 5;
    private static final int ITEMS_PER_PRODUCER = 6;
    private static final int POISON_PILL    = -1;   // sentinel value

    // Thread-safe counters – no synchronized needed
    private static final AtomicInteger totalProduced = new AtomicInteger(0);
    private static final AtomicInteger totalConsumed = new AtomicInteger(0);

    private static final BlockingQueue<Integer> queue =
            new LinkedBlockingQueue<>(QUEUE_CAPACITY);

    // =========================================================================
    // Producer
    // =========================================================================
    static class Producer implements Runnable {
        private final String name;

        Producer(String name) { this.name = name; }

        @Override
        public void run() {
            for (int i = 0; i < ITEMS_PER_PRODUCER; i++) {
                int value = totalProduced.incrementAndGet();
                try {
                    queue.put(value);    // blocks if queue is full
                    System.out.printf("[%s] produced %d  (queue size=%d)%n",
                            name, value, queue.size());
                    Thread.sleep((long)(Math.random() * 200) + 50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    // =========================================================================
    // Consumer
    // =========================================================================
    static class Consumer implements Runnable {
        private final String name;

        Consumer(String name) { this.name = name; }

        @Override
        public void run() {
            while (true) {
                try {
                    int value = queue.take();   // blocks if queue is empty
                    if (value == POISON_PILL) {
                        // Re-insert poison pill so other consumers also stop
                        queue.put(POISON_PILL);
                        System.out.println("[" + name + "] got poison pill, stopping.");
                        return;
                    }
                    totalConsumed.incrementAndGet();
                    System.out.printf("[%s] consumed %d%n", name, value);
                    Thread.sleep((long)(Math.random() * 300) + 100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        int numProducers = 2;
        int numConsumers = 3;

        // Start producers
        Thread[] producers = new Thread[numProducers];
        for (int i = 0; i < numProducers; i++) {
            producers[i] = new Thread(new Producer("Producer-" + (i + 1)));
            producers[i].start();
        }

        // Start consumers
        Thread[] consumers = new Thread[numConsumers];
        for (int i = 0; i < numConsumers; i++) {
            consumers[i] = new Thread(new Consumer("Consumer-" + (i + 1)));
            consumers[i].start();
        }

        // Wait for all producers to finish
        for (Thread p : producers) p.join();

        // Send one poison pill; consumers will propagate it
        queue.put(POISON_PILL);

        // Wait for all consumers to finish
        for (Thread c : consumers) c.join();

        System.out.printf("%nSummary: produced=%d  consumed=%d%n",
                totalProduced.get(), totalConsumed.get());
    }
}
