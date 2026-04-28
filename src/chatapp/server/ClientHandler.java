package chatapp.server;

import chatapp.db.DatabaseManager;
import chatapp.db.MessageDAO;
import chatapp.db.MessageDAOImpl;
import chatapp.db.UserDAO;
import chatapp.db.UserDAOImpl;
import chatapp.model.Message;
import chatapp.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CONCURRENCY / OOP / SOCKETS – Handles one connected client on its own thread.
 *
 * Each time a client connects, ChatServer creates a new ClientHandler and
 * submits it to an ExecutorService.  The ExecutorService assigns it a thread
 * from its pool, so many clients can be served simultaneously.
 *
 * Implements Runnable (OOP interface) so it can be submitted to ExecutorService.
 *
 * Concurrency highlights in this class:
 *   • AtomicInteger  – thread-safe counter; no synchronized keyword needed
 *   • ConcurrentHashMap (clients map) – shared across ALL ClientHandler threads;
 *     safe to read and write concurrently
 *
 * Protocol (plain text over TCP):
 *   Client sends  →  "NAME:<username>"      register (must be first message)
 *   Client sends  →  any text               broadcast to all + save to DB
 *   Client sends  →  "HISTORY:<n>"          show last n messages from DB
 *   Client sends  →  "quit"                 disconnect gracefully
 */
public class ClientHandler implements Runnable {

    /**
     * CONCURRENCY – AtomicInteger counts total messages sent this server session.
     *
     * Why AtomicInteger and not a plain int?
     * Multiple ClientHandler threads call totalMessages.incrementAndGet()
     * concurrently.  AtomicInteger performs the increment atomically (as one
     * indivisible CPU operation), so no two threads can corrupt the value.
     * With a plain int, two threads could read the same value, both add 1,
     * and write back the same result – losing one increment.
     */
    static final AtomicInteger totalMessages = new AtomicInteger(0);

    private final Socket                    socket;
    private final Map<String, PrintWriter>  clients;   // shared ConcurrentHashMap

    public ClientHandler(Socket socket, Map<String, PrintWriter> clients) {
        this.socket  = socket;
        this.clients = clients;
    }

    // =========================================================================
    // run() – executed on a thread-pool thread by ExecutorService
    // =========================================================================
    @Override
    public void run() {
        String     username = socket.getRemoteSocketAddress().toString(); // fallback name
        PrintWriter out     = null;

        try {
            // Set up I/O streams over the socket
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true /*autoFlush*/);

            // ---- Step 1: NAME registration ----------------------------------------
            out.println("[Server] Welcome! Send NAME:<yourname> to register.");
            String firstLine = in.readLine();
            if (firstLine != null && firstLine.startsWith("NAME:")) {
                username = firstLine.substring(5).trim();
            }

            // ---- Step 2: Persist user to DB ----------------------------------------
            // DB operations are wrapped in try-catch so a DB failure doesn't kill the chat
            try {
                DatabaseManager dbm     = DatabaseManager.getInstance();
                UserDAO         userDAO = new UserDAOImpl(dbm.getConnection());
                userDAO.save(new User(username));          // INSERT IGNORE – safe to call on reconnect
            } catch (SQLException e) {
                System.err.println("[DB] Could not save user '" + username + "': " + e.getMessage());
            }

            // ---- Step 3: Register client in shared map (ConcurrentHashMap) ---------
            clients.put(username, out);
            ChatServer.broadcast("[Server] " + username + " joined the chat.  " +
                    "(" + clients.size() + " user(s) online)", clients);
            System.out.println("[Server] " + username + " registered.");

            // ---- Step 4: Message loop -----------------------------------------------
            String line;
            while ((line = in.readLine()) != null) {
                String trimmed = line.trim();

                // "quit" – exit the loop, trigger finally block
                if ("quit".equalsIgnoreCase(trimmed)) {
                    break;
                }

                // "HISTORY:<n>" – send recent messages back to this client only
                if (trimmed.toUpperCase().startsWith("HISTORY:")) {
                    handleHistory(out, trimmed);
                    continue;
                }

                // Normal message – broadcast + save to DB
                Message msg = new Message(username, line);
                saveMessageToDB(msg);
                totalMessages.incrementAndGet();           // AtomicInteger: thread-safe +1
                ChatServer.broadcast(msg.toString(), clients);
            }

        } catch (IOException e) {
            System.err.println("[Server] I/O error for '" + username + "': " + e.getMessage());
        } finally {
            // ---- Cleanup on disconnect (even if an exception occurred) ---------------
            clients.remove(username);
            try { socket.close(); } catch (IOException ignored) { }
            ChatServer.broadcast(
                    "[Server] " + username + " left the chat.  " +
                    "(Total messages this session: " + totalMessages.get() + ")",
                    clients);
            System.out.println("[Server] " + username + " disconnected.");
        }
    }

    // =========================================================================
    // handleHistory – respond to "HISTORY:<n>" command
    // =========================================================================
    private void handleHistory(PrintWriter out, String command) {
        int n = 10; // default
        try {
            n = Integer.parseInt(command.substring(8).trim());
        } catch (NumberFormatException ignored) { }

        out.println("--- Last " + n + " messages ---");
        try {
            DatabaseManager dbm    = DatabaseManager.getInstance();
            MessageDAO      msgDAO = new MessageDAOImpl(dbm.getConnection());
            List<Message>   hist   = msgDAO.getRecent(n);
            if (hist.isEmpty()) {
                out.println("(no messages yet)");
            } else {
                hist.forEach(m -> out.println(m.toString()));
            }
        } catch (SQLException e) {
            out.println("(error fetching history: " + e.getMessage() + ")");
        }
        out.println("--- End of history ---");
    }

    // =========================================================================
    // saveMessageToDB – persist one message, swallow DB errors gracefully
    // =========================================================================
    private void saveMessageToDB(Message msg) {
        try {
            DatabaseManager dbm    = DatabaseManager.getInstance();
            MessageDAO      msgDAO = new MessageDAOImpl(dbm.getConnection());
            msgDAO.save(msg);
        } catch (SQLException e) {
            System.err.println("[DB] Could not save message: " + e.getMessage());
        }
    }
}
