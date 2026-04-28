# Eksamen_prepp

Java code examples for exam preparation.  
Each topic has **multiple templates** you can copy and adapt for your own application.

---

## 🚀 Capstone Projects

Two full projects that combine all four core exam topics — **OOP, Concurrency, Socket Programming, and DB** — into one realistic application.

| Project | Purpose |
|---------|---------|
| `src/chatapp/` | ✅ **Complete** — a working multi-client real-time chat server with DB persistence |
| `src/chatapp_tutorial/` | 📝 **Tutorial** — identical structure but with guided `// TODO` blanks to fill in |

See **[Section 10](#10--chatapp--complete-project)** below for a full description and how to run the app.

---

## 📂 Project Structure

```
src/
├── chatapp/              ✅ Complete capstone project (OOP + Sockets + Concurrency + DB)
│   ├── model/            User.java, Message.java
│   ├── db/               DatabaseManager, MessageDAO/Impl, UserDAO/Impl
│   ├── server/           ChatServer.java, ClientHandler.java
│   └── client/           ChatClient.java
├── chatapp_tutorial/     📝 Tutorial version with TODO blanks (same structure as above)
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

---

## 10 · ChatApp — Complete Project

### What it is

A **multi-client real-time chat server** that combines all four exam topics:

| Topic | Where it's used |
|-------|----------------|
| **OOP** | `User`, `Message` model classes; `MessageDAO`/`UserDAO` interfaces; `ClientHandler implements Runnable` |
| **Concurrency** | `ExecutorService` thread pool; `ConcurrentHashMap` for the client list; `AtomicInteger` for a thread-safe message counter |
| **Socket Programming** | `ServerSocket` accepts clients; `Socket` I/O streams carry messages; background reader thread on client side |
| **Database (JDBC)** | Singleton `DatabaseManager`; DAO pattern; `PreparedStatement` INSERT/SELECT; history query with subquery |

### Files

```
src/chatapp/
├── model/
│   ├── User.java          OOP: entity, encapsulation, constructors, toString
│   └── Message.java       OOP: entity, static constant, toString formatting
├── db/
│   ├── DatabaseManager.java   DB: Singleton, DriverManager, CREATE TABLE IF NOT EXISTS
│   ├── MessageDAO.java        OOP: DAO interface (contract)
│   ├── MessageDAOImpl.java    DB: PreparedStatement INSERT + subquery SELECT
│   ├── UserDAO.java           OOP: DAO interface
│   └── UserDAOImpl.java       DB: INSERT IGNORE, SELECT with ResultSet
├── server/
│   ├── ChatServer.java        Sockets: ServerSocket + accept loop; Concurrency: ExecutorService, ConcurrentHashMap
│   └── ClientHandler.java     Sockets: per-client I/O; Concurrency: AtomicInteger, shared ConcurrentHashMap
└── client/
    └── ChatClient.java        Sockets: Socket; Concurrency: daemon reader thread + main input thread
```

### How to run

**Step 1 — Set up the database**

```sql
-- MySQL
CREATE DATABASE chatapp_db;
-- tables are created automatically on server start
```

Or use H2 (no install needed) by changing `DatabaseManager.java`:
```java
DB_URL  = "jdbc:h2:mem:chatapp;DB_CLOSE_DELAY=-1"
DB_USER = "sa"
DB_PASS = ""
```

**Step 2 — Compile**

```bash
javac -d out src/chatapp/**/*.java src/chatapp/*.java
# or recursively:
find src/chatapp -name "*.java" | xargs javac -d out
```

**Step 3 — Run the server**

```bash
java -cp out chatapp.server.ChatServer
```

**Step 4 — Run one or more clients** (each in its own terminal)

```bash
java -cp out chatapp.client.ChatClient
```

**Step 5 — Chat!**

```
NAME:Alice          ← register your username (must be first)
Hello everyone!     ← broadcast to all clients
HISTORY:10          ← show last 10 messages from the database
quit                ← disconnect
```

---

## 11 · ChatApp Tutorial — Fill-in-the-Blanks

`src/chatapp_tutorial/` is an identical copy of `chatapp` but with all method bodies replaced by detailed `// TODO` comments.

### How to use it

1. Open the files in `src/chatapp_tutorial/` in your IDE.
2. Read the class-level Javadoc (explains the concept).
3. Read each `TODO` comment carefully — it tells you exactly what to write.
4. Fill in the blanks one file at a time (suggested order below).
5. Compare your solution against `src/chatapp/` when you're stuck.

### Suggested order

| Step | File | Concept |
|------|------|---------|
| 1 | `model/User.java` | OOP: fields, constructors, getters, toString |
| 2 | `model/Message.java` | OOP: same, plus static constant |
| 3 | `db/DatabaseManager.java` | DB: Singleton, JDBC connection, CREATE TABLE |
| 4 | `db/MessageDAOImpl.java` | DB: PreparedStatement INSERT + subquery SELECT |
| 5 | `db/UserDAOImpl.java` | DB: PreparedStatement INSERT IGNORE + SELECT |
| 6 | `server/ChatServer.java` | Sockets + Concurrency: ServerSocket, ExecutorService, ConcurrentHashMap |
| 7 | `server/ClientHandler.java` | Sockets + Concurrency: Runnable, socket I/O, AtomicInteger |
| 8 | `client/ChatClient.java` | Sockets + Concurrency: Socket client, daemon thread |

> The `MessageDAO.java` and `UserDAO.java` interface files are already complete — study them before implementing their Impl classes.

### Key concepts recap

| Concept | Class | What to look for |
|---------|-------|-----------------|
| Encapsulation | `User`, `Message` | private fields + public getters |
| Interface + Impl | `MessageDAO` + `MessageDAOImpl` | DAO pattern, separation of concerns |
| Singleton | `DatabaseManager` | `synchronized getInstance()`, private constructor |
| Thread pool | `ChatServer` | `Executors.newFixedThreadPool(n)`, `pool.execute(handler)` |
| Thread-safe map | `ChatServer.clients` | `ConcurrentHashMap` vs plain `HashMap` |
| Atomic counter | `ClientHandler.totalMessages` | `AtomicInteger.incrementAndGet()` vs plain `int++` |
| Socket server | `ChatServer` | `ServerSocket`, `accept()`, blocking call |
| Socket client | `ChatClient` | `Socket`, `getInputStream()`, `getOutputStream()` |
| Daemon thread | `ChatClient` | `setDaemon(true)`, reader thread lifecycle |
| PreparedStatement | `MessageDAOImpl`, `UserDAOImpl` | `?` placeholders prevent SQL injection |
