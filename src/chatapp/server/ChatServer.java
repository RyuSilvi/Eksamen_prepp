package chatapp.server;

import chatapp.db.DatabaseManager;

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
 * SOCKETS / CONCURRENCY – The main chat server.
 *
 * Responsibilities:
 *   1. Open a ServerSocket and listen for incoming client connections.
 *   2. For each connection, create a ClientHandler and submit it to an
 *      ExecutorService (thread pool) – this is the core concurrency pattern.
 *   3. Provide a static broadcast() method that ClientHandler threads call
 *      to send a message to all connected clients.
 *
 * ─────────────────────────────────────────────────────────────
 *  CONCURRENCY CONCEPTS USED
 * ─────────────────────────────────────────────────────────────
 *  ExecutorService (newFixedThreadPool)
 *    Manages a pool of reusable threads.  Instead of "new Thread(...).start()"
 *    for every client, we call pool.execute(handler).  The pool reuses threads
 *    and limits the maximum concurrency to MAX_THREADS.
 *
 *  ConcurrentHashMap<String, PrintWriter>
 *    Stores username → output stream for every connected client.
 *    Multiple ClientHandler threads read and write this map simultaneously,
 *    so a thread-safe map is essential.  ConcurrentHashMap uses internal
 *    segmented locking – much faster than wrapping a HashMap in synchronized.
 *
 * ─────────────────────────────────────────────────────────────
 *  PROTOCOL SUMMARY
 * ─────────────────────────────────────────────────────────────
 *  Client → Server:  NAME:<username>   register (send this first)
 *  Client → Server:  <any text>        broadcast message to everyone
 *  Client → Server:  HISTORY:<n>       request last n messages from DB
 *  Client → Server:  quit              graceful disconnect
 *
 * Run ChatServer first, then open one or more ChatClient windows.
 * You can also use:  telnet localhost 5001
 */
public class ChatServer {

    private static final int PORT        = 5001;
    private static final int MAX_THREADS = 20;     // max simultaneous clients

    /**
     * CONCURRENCY – Thread-safe map: username → PrintWriter (client output stream).
     *
     * ConcurrentHashMap is safe to read and write from multiple threads
     * without any additional synchronization.
     * All ClientHandler threads share this single map instance.
     */
    static final Map<String, PrintWriter> clients = new ConcurrentHashMap<>();

    // =========================================================================
    // broadcast – send a message to every connected client
    //
    // This method is called from multiple ClientHandler threads concurrently.
    // Iterating over ConcurrentHashMap.values() is safe under concurrent
    // modification (we may miss a client that disconnects mid-iteration,
    // but we will never get a ConcurrentModificationException).
    // =========================================================================
    public static void broadcast(String message, Map<String, PrintWriter> clientMap) {
        System.out.println(message);                          // log on server console
        for (PrintWriter writer : clientMap.values()) {
            writer.println(message);                          // send to each client
        }
    }

    // =========================================================================
    // main
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║      ChatApp Server v1.0         ║");
        System.out.println("╚══════════════════════════════════╝");
        System.out.println("[Server] Starting on port " + PORT);

        // ---- Initialise database connection and create tables ------------------
        try {
            DatabaseManager.getInstance();   // triggers schema creation
            System.out.println("[Server] Database ready.");
        } catch (SQLException e) {
            System.err.println("[Server] WARNING – DB init failed: " + e.getMessage());
            System.err.println("[Server] Chat will work but messages will NOT be persisted.");
        }

        // ---- Create thread pool ------------------------------------------------
        // newFixedThreadPool(n) keeps at most n threads alive at once.
        // If more than n clients connect, extra connections wait in a queue.
        ExecutorService pool = Executors.newFixedThreadPool(MAX_THREADS);

        // ---- Accept loop --------------------------------------------------------
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[Server] Listening on port " + PORT + " …  (Ctrl-C to stop)");

            while (true) {
                // accept() BLOCKS until a client connects – this is fine because
                // the actual work is done on pool threads, not this one.
                Socket clientSocket = serverSocket.accept();
                System.out.println("[Server] New connection: "
                        + clientSocket.getRemoteSocketAddress());

                // Hand the socket off to a ClientHandler running on a pool thread.
                // pool.execute() returns immediately; the handler runs concurrently.
                pool.execute(new ClientHandler(clientSocket, clients));
            }

        } catch (IOException e) {
            System.err.println("[Server] Fatal server error: " + e.getMessage());
        } finally {
            pool.shutdown();   // wait for active handlers to finish before exiting
            System.out.println("[Server] Server shut down.");
        }
    }
}
