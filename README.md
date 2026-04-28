# Eksamen_prepp

Java code examples for exam preparation.  
Each topic has **multiple templates** you can copy and adapt for your own application.

---

## 📂 Project Structure

```
src/
├── db/                   Database connectivity
├── multithreads/         Threads & the Restaurant System
├── concurrency/          High-level concurrency utilities
├── threadlocks/          Locks, synchronization & deadlock prevention
├── algorithms/           Binary Search & Binary Search Tree
├── hashtable/            Hash Table (custom + built-in)
├── sockets/              Socket programming (server & client)
├── animations/           JavaFX Timeline & Transition animations
└── javafx/               JavaFX UI examples
```

---

## 1 · Database (DB) Connection — `src/db/`

| File | What it shows |
|------|---------------|
| `BasicJDBCConnection.java` | Open a JDBC connection, run a SELECT, iterate ResultSet, close resources with try-with-resources |
| `PreparedStatementExample.java` | INSERT / SELECT / UPDATE / DELETE with `PreparedStatement`; transaction (commit / rollback) |
| `DAOExample.java` | **Data Access Object** pattern — model class, DAO interface, JDBC implementation, main demo |

> **Tip:** Change the `DB_URL`, `USER`, and `PASSWORD` constants and add your JDBC driver (e.g. `mysql-connector-java`) to the classpath.

---

## 2 · Multithreads — `src/multithreads/`

| File | What it shows |
|------|---------------|
| `BasicThreadExample.java` | Extend `Thread`, implement `Runnable`, lambda threads; `start()` vs `run()`; `join()` |
| `ThreadPoolExample.java` | `ExecutorService` with fixed pool; `Runnable` vs `Callable`; `Future<T>`; single-thread executor |
| `RestaurantSystem.java` | **Full restaurant simulation** — customers, waiter, and chefs each on their own threads; producer-consumer pattern with `wait()`/`notifyAll()`; `synchronized` queues |

---

## 3 · Concurrency — `src/concurrency/`

| File | What it shows |
|------|---------------|
| `SemaphoreExample.java` | `Semaphore` for a parking lot (limited permits); binary semaphore as a mutex |
| `CountDownLatchAndBarrierExample.java` | `CountDownLatch` as a starter gun and as a "services ready" signal; `CyclicBarrier` for multi-phase work |
| `ProducerConsumerExample.java` | `BlockingQueue` (no manual sync needed); `AtomicInteger`; poison-pill shutdown pattern |

---

## 4 · Thread Locks & Deadlock Prevention — `src/threadlocks/`

| File | What it shows |
|------|---------------|
| `ReentrantLockExample.java` | `ReentrantLock` safe counter; `tryLock()` non-blocking attempt; `ReadWriteLock` for many readers / one writer |
| `DeadlockPreventionExample.java` | **Deadlock demo** (live deadlock, then interrupted); prevention via **lock ordering**; prevention via **tryLock with back-off** |
| `SynchronizedExample.java` | `synchronized` method; `synchronized` block; race condition demo; `wait()`/`notifyAll()` with a bounded buffer |

---

## 5 · Binary Search — `src/algorithms/`

| File | What it shows |
|------|---------------|
| `BinarySearch.java` | Iterative binary search; recursive binary search; find first/last occurrence (duplicates); full **Binary Search Tree** (BST) with insert, search, in-order traversal, min/max |

---

## 6 · Hash Table — `src/hashtable/`

| File | What it shows |
|------|---------------|
| `HashTableExample.java` | **Custom hash table** with chaining + resize; `HashMap` (word counter, `merge`, `getOrDefault`); `HashSet` (union, intersection, difference, duplicate detection) |

---

## 7 · Socket Programming — `src/sockets/`

| File | What it shows |
|------|---------------|
| `SimpleServer.java` | `ServerSocket`, accept one client, echo uppercase, clean shutdown |
| `SimpleClient.java` | `Socket`, connect to server, send messages, read responses |
| `MultiClientChatServer.java` | Multi-client server with a thread-pool (`newCachedThreadPool`); broadcast to all clients; `ConcurrentHashMap` as thread-safe client set |
| `ChatClient.java` | Separate reader thread for incoming messages; user input on main thread |

> Run `MultiClientChatServer` first, then open two or more `ChatClient` windows (or use `telnet localhost 5001`).

---

## 8 · Animations — `src/animations/`

| File | What it shows |
|------|---------------|
| `TimelineAnimationFX.java` | `Timeline` with `KeyFrame`/`KeyValue`; moving ball, blinking text, growing rectangle; pause/play controls |
| `AnimationTransitionsFX.java` | `TranslateTransition`, `FadeTransition`, `ScaleTransition`, `RotateTransition`, `FillTransition`; `SequentialTransition`; `ParallelTransition` |

---

## 9 · JavaFX — `src/javafx/`

| File | What it shows |
|------|---------------|
| `HelloWorldFX.java` | Minimal JavaFX app — `Application`, `Stage`, `Scene`, `Label`, `Button`, event handler |
| `TableViewFX.java` | `TableView` with `ObservableList`; property-based model; add/delete rows at runtime |
| `RegistrationFormFX.java` | `TextField`, `PasswordField`, `ComboBox`, `RadioButton`, `CheckBox`, `TextArea`; form validation; `GridPane` layout |

---

## ▶ Running the examples

### Non-JavaFX classes
```bash
# Compile (from the repo root)
javac -d out src/**/*.java

# Run (example)
java -cp out multithreads.RestaurantSystem
java -cp out algorithms.BinarySearch
java -cp out hashtable.HashTableExample
```

### JavaFX classes (requires JavaFX SDK)
```bash
# Compile
javac --module-path /path/to/javafx-sdk/lib \
      --add-modules javafx.controls \
      -d out src/**/*.java

# Run
java --module-path /path/to/javafx-sdk/lib \
     --add-modules javafx.controls \
     -cp out javafx.HelloWorldFX
```

Download the JavaFX SDK from [https://openjfx.io](https://openjfx.io).

---

## 📝 Notes

- All DB examples require a running MySQL (or compatible) database.  
  Change the connection string for PostgreSQL, SQLite, H2, etc.
- JavaFX examples require Java 11+ and a separate JavaFX SDK (or a JDK bundle that includes JavaFX).
- The `RestaurantSystem`, concurrency, and socket examples print output that interleaves across threads — run them and observe the ordering to understand how threads interact.
