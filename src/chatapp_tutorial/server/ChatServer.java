package chatapp_tutorial.server;

import chatapp_tutorial.db.DatabaseManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ═══════════════════════════════════════════════════════════════════
 * SOCKETS / CONCURRENCY TUTORIAL – ChatServer (main server)
 * ═══════════════════════════════════════════════════════════════════
 *
 * CONCEPTS COVERED HERE:
 *
 * 1. ServerSocket
 *    Opens a TCP port and waits for client connections.
 *    serverSocket.accept() BLOCKS until a client connects,
 *    then returns a Socket representing that client.
 *
 * 2. ExecutorService (thread pool)
 *    Instead of creating a raw "new Thread(...).start()" for each client,
 *    we use a thread pool.  The pool:
 *      - Reuses threads (cheaper than creating new ones)
 *      - Limits max concurrency (no runaway thread creation)
 *      - Provides clean shutdown via pool.shutdown()
 *
 *    Executors.newFixedThreadPool(n) → pool of exactly n threads.
 *    Extra tasks wait in a queue until a thread is free.
 *
 * 3. ConcurrentHashMap
 *    Stores username → PrintWriter for every connected client.
 *    Multiple ClientHandler threads read and write this map at the same time.
 *    ConcurrentHashMap handles the locking internally – you don't need
 *    "synchronized" blocks to use it safely.
 *
 * 4. broadcast()
 *    A static method so any ClientHandler can call it without a reference
 *    to the ChatServer object.  Iterating ConcurrentHashMap.values() is
 *    safe even while other threads add/remove entries.
 *
 * ─────────────────────────────────────────────────────────────────
 *  RUNNING THE APP
 * ─────────────────────────────────────────────────────────────────
 *   1. Start ChatServer (this class).
 *   2. Run ChatClient in two or more separate windows.
 *   3. Type NAME:Alice in one and NAME:Bob in another.
 *   4. Send messages and observe them appear in all windows.
 *   5. Try HISTORY:5 to see the last 5 messages from the DB.
 * ═══════════════════════════════════════════════════════════════════
 */
public class ChatServer {

    private static final int PORT        = 5001;
    private static final int MAX_THREADS = 20;

    /**
     * CONCURRENCY – Thread-safe client map.
     *
     * ConcurrentHashMap<String, PrintWriter>:
     *   Key   = username  (String)
     *   Value = output stream to that client  (PrintWriter)
     *
     * All ClientHandler threads share this ONE map instance.
     * "static final" ensures every thread sees the same object.
     *
     * TODO 1: This field is already declared.  Read it and understand
     *         why ConcurrentHashMap is used here instead of HashMap.
     */
    static final Map<String, PrintWriter> clients = new ConcurrentHashMap<>();

    // ─────────────────────────────────────────────────────────────────────────
    // broadcast
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Send a message to every currently connected client.
     *
     * Called from multiple ClientHandler threads simultaneously.
     * Iterating ConcurrentHashMap.values() is safe under concurrent modification
     * (we may miss a client that disconnects mid-iteration, but we will never
     * throw ConcurrentModificationException).
     *
     * TODO 2: Implement this method.
     *   a) Print the message to System.out (server console log).
     *   b) For each PrintWriter in clientMap.values(), call writer.println(message).
     *
     * HINT:
     *   System.out.println(message);
     *   for (PrintWriter writer : clientMap.values()) {
     *       writer.println(message);
     *   }
     */
    public static void broadcast(String message, Map<String, PrintWriter> clientMap) {
        // TODO 2
    }

    // ─────────────────────────────────────────────────────────────────────────
    // main
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * TODO 3: Implement the server startup sequence:
     *
     * STEP 1 – Database:
     *   a) Call DatabaseManager.getInstance() inside a try-catch(SQLException).
     *      On success: print "[Server] Database ready."
     *      On failure: print a warning and continue (chat works without DB).
     *
     * STEP 2 – Thread pool:
     *   b) Create: ExecutorService pool = Executors.newFixedThreadPool(MAX_THREADS);
     *
     * STEP 3 – Accept loop:
     *   c) Open a ServerSocket on PORT using try-with-resources.
     *   d) Loop forever:
     *        Socket clientSocket = serverSocket.accept();   // blocks until connection
     *        pool.execute(new ClientHandler(clientSocket, clients));  // run on pool thread
     *
     * STEP 4 – Shutdown:
     *   e) In a finally block: pool.shutdown()
     *
     * HINT – overall structure:
     *   try {
     *       DatabaseManager.getInstance();
     *   } catch (SQLException e) { ... }
     *
     *   ExecutorService pool = Executors.newFixedThreadPool(MAX_THREADS);
     *   try (ServerSocket serverSocket = new ServerSocket(PORT)) {
     *       while (true) {
     *           Socket clientSocket = serverSocket.accept();
     *           pool.execute(new ClientHandler(clientSocket, clients));
     *       }
     *   } catch (IOException e) { ... }
     *   finally { pool.shutdown(); }
     */
    public static void main(String[] args) {
        System.out.println("[Server] Starting ChatServer on port " + PORT);

        // TODO 3a: initialise DB
        // TODO 3b: create thread pool
        // TODO 3c-d: open ServerSocket and accept loop
        // TODO 3e: shutdown pool in finally
    }
}
