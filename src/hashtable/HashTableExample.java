package hashtable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Hash Table Examples
 *
 * Part 1 – Custom hash table implementation using chaining (linked lists)
 *   Demonstrates: hash function, collision handling, put, get, remove, resize.
 *
 * Part 2 – Using Java's built-in HashMap
 *   Demonstrates: put, get, containsKey, iterate, merge, getOrDefault.
 *
 * Part 3 – Using Java's built-in HashSet
 *   Demonstrates: add, contains, set operations (union, intersection, difference).
 */
public class HashTableExample {

    // =========================================================================
    // Part 1 – Custom Hash Table (separate chaining)
    // =========================================================================
    static class CustomHashTable<K, V> {

        private static final int   DEFAULT_CAPACITY     = 16;
        private static final float DEFAULT_LOAD_FACTOR  = 0.75f;

        // Each bucket is a linked list of key-value pairs
        @SuppressWarnings("unchecked")
        private LinkedList<Entry<K, V>>[] buckets =
                new LinkedList[DEFAULT_CAPACITY];

        private int size       = 0;
        private int capacity   = DEFAULT_CAPACITY;

        // Inner class to store one key-value pair
        static class Entry<K, V> {
            K key;
            V value;
            Entry(K key, V value) { this.key = key; this.value = value; }
        }

        // ---- hash function ------------------------------------------------
        private int hash(K key) {
            int h = key.hashCode();
            // Spread bits to reduce clustering
            h = h ^ (h >>> 16);
            return Math.abs(h) % capacity;
        }

        // ---- put -----------------------------------------------------------
        public void put(K key, V value) {
            int index = hash(key);

            if (buckets[index] == null) {
                buckets[index] = new LinkedList<>();
            }

            // Update existing entry if key already exists
            for (Entry<K, V> entry : buckets[index]) {
                if (entry.key.equals(key)) {
                    entry.value = value;
                    return;
                }
            }

            // New key
            buckets[index].add(new Entry<>(key, value));
            size++;

            // Resize if load factor exceeded
            if ((float) size / capacity >= DEFAULT_LOAD_FACTOR) {
                resize();
            }
        }

        // ---- get -----------------------------------------------------------
        public V get(K key) {
            int index = hash(key);
            if (buckets[index] == null) return null;

            for (Entry<K, V> entry : buckets[index]) {
                if (entry.key.equals(key)) return entry.value;
            }
            return null;
        }

        // ---- remove --------------------------------------------------------
        public boolean remove(K key) {
            int index = hash(key);
            if (buckets[index] == null) return false;

            var iter = buckets[index].iterator();
            while (iter.hasNext()) {
                if (iter.next().key.equals(key)) {
                    iter.remove();
                    size--;
                    return true;
                }
            }
            return false;
        }

        // ---- containsKey ---------------------------------------------------
        public boolean containsKey(K key) { return get(key) != null; }

        // ---- size ----------------------------------------------------------
        public int size() { return size; }

        // ---- resize (double the capacity) ----------------------------------
        @SuppressWarnings("unchecked")
        private void resize() {
            int oldCapacity     = capacity;
            capacity           *= 2;
            LinkedList<Entry<K, V>>[] oldBuckets = buckets;
            buckets             = new LinkedList[capacity];
            size                = 0;

            for (int i = 0; i < oldCapacity; i++) {
                if (oldBuckets[i] != null) {
                    for (Entry<K, V> entry : oldBuckets[i]) {
                        put(entry.key, entry.value);   // re-hash into new buckets
                    }
                }
            }
            System.out.println("[CustomHashTable] Resized to capacity " + capacity);
        }

        // ---- print all entries ---------------------------------------------
        public void printAll() {
            System.out.println("CustomHashTable (size=" + size + "):");
            for (int i = 0; i < capacity; i++) {
                if (buckets[i] != null && !buckets[i].isEmpty()) {
                    System.out.print("  bucket[" + i + "]: ");
                    for (Entry<K, V> e : buckets[i]) {
                        System.out.print(e.key + "=" + e.value + "  ");
                    }
                    System.out.println();
                }
            }
        }
    }

    // =========================================================================
    // Part 2 – Java's built-in HashMap
    // =========================================================================
    static void hashMapDemo() {
        System.out.println("\n=== Built-in HashMap demo ===");

        Map<String, Integer> scores = new HashMap<>();

        // put
        scores.put("Alice",   95);
        scores.put("Bob",     80);
        scores.put("Charlie", 88);
        scores.put("Alice",   99);  // update: overwrites Alice's score

        // get
        System.out.println("Alice's score: " + scores.get("Alice"));

        // containsKey
        System.out.println("Has 'Bob':  " + scores.containsKey("Bob"));
        System.out.println("Has 'Dave': " + scores.containsKey("Dave"));

        // getOrDefault – safe retrieval with a fallback
        System.out.println("Dave's score: " + scores.getOrDefault("Dave", 0));

        // iterate entries
        System.out.println("All scores:");
        for (Map.Entry<String, Integer> e : scores.entrySet()) {
            System.out.println("  " + e.getKey() + " -> " + e.getValue());
        }

        // merge – add 5 bonus points to everyone
        scores.replaceAll((name, score) -> score + 5);
        System.out.println("After +5 bonus: " + scores);

        // Word-frequency counter (classic HashMap pattern)
        System.out.println("\nWord frequency:");
        String text = "apple banana apple cherry banana apple";
        Map<String, Integer> freq = new HashMap<>();
        for (String word : text.split(" ")) {
            freq.merge(word, 1, Integer::sum);  // add 1, or sum if key exists
        }
        freq.forEach((word, count) -> System.out.println("  " + word + ": " + count));
    }

    // =========================================================================
    // Part 3 – Java's built-in HashSet
    // =========================================================================
    static void hashSetDemo() {
        System.out.println("\n=== Built-in HashSet demo ===");

        Set<String> setA = new HashSet<>(Set.of("apple", "banana", "cherry", "date"));
        Set<String> setB = new HashSet<>(Set.of("banana", "date", "elderberry"));

        System.out.println("Set A: " + setA);
        System.out.println("Set B: " + setB);

        // Union
        Set<String> union = new HashSet<>(setA);
        union.addAll(setB);
        System.out.println("Union: " + union);

        // Intersection
        Set<String> intersection = new HashSet<>(setA);
        intersection.retainAll(setB);
        System.out.println("Intersection: " + intersection);

        // Difference (A - B)
        Set<String> difference = new HashSet<>(setA);
        difference.removeAll(setB);
        System.out.println("Difference (A - B): " + difference);

        // Duplicate detection
        int[] numbers = {1, 2, 3, 2, 4, 1, 5};
        Set<Integer> seen = new HashSet<>();
        System.out.print("Duplicates in [1,2,3,2,4,1,5]: ");
        for (int n : numbers) {
            if (!seen.add(n)) System.out.print(n + " ");
        }
        System.out.println();
    }

    // =========================================================================
    // main
    // =========================================================================
    public static void main(String[] args) {
        // --- Custom hash table ---
        System.out.println("=== Custom Hash Table ===");
        CustomHashTable<String, Integer> table = new CustomHashTable<>();
        table.put("one",   1);
        table.put("two",   2);
        table.put("three", 3);
        table.put("four",  4);
        table.put("one",   111);  // update

        table.printAll();
        System.out.println("get('two')  -> " + table.get("two"));
        System.out.println("get('five') -> " + table.get("five"));
        table.remove("three");
        System.out.println("After remove('three'), size=" + table.size());

        // --- Built-in collections ---
        hashMapDemo();
        hashSetDemo();
    }
}
