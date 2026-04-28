package chatapp_tutorial.server;

import chatapp_tutorial.db.DatabaseManager;
import chatapp_tutorial.db.MessageDAO;
import chatapp_tutorial.db.MessageDAOImpl;
import chatapp_tutorial.db.UserDAO;
import chatapp_tutorial.db.UserDAOImpl;
import chatapp_tutorial.model.Message;
import chatapp_tutorial.model.User;

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
 * ═══════════════════════════════════════════════════════════════════
 * CONCURRENCY / OOP / SOCKETS TUTORIAL – ClientHandler
 * ═══════════════════════════════════════════════════════════════════
 *
 * This class handles ONE connected client.
 * Each ClientHandler runs on its own thread inside the ExecutorService
 * thread pool managed by ChatServer.
 *
 * IMPLEMENTS Runnable:
 *   The ExecutorService needs a Runnable (or Callable) to run.
 *   By implementing Runnable, our ClientHandler can be passed directly
 *   to pool.execute(new ClientHandler(...)).
 *
 * CONCURRENCY CONCEPTS IN THIS CLASS:
 * ────────────────────────────────────
 * 1. AtomicInteger (totalMessages)
 *    Counts how many messages have been sent this session.
 *    It is shared by ALL ClientHandler threads running simultaneously.
 *    AtomicInteger.incrementAndGet() is a single atomic CPU operation,
 *    so no two threads can corrupt the count.
 *    Compare to a plain int: if Thread A reads 5 and Thread B reads 5
 *    at the same moment, both write 6 – one increment is lost!
 *
 * 2. ConcurrentHashMap (clients parameter)
 *    Maps username → PrintWriter for each connected client.
 *    All ClientHandlers share this map.
 *    ConcurrentHashMap allows concurrent reads AND writes safely.
 *
 * PROTOCOL:
 *   Client → Server  "NAME:<name>"     register username (first message)
 *   Client → Server  any text           broadcast + save to DB
 *   Client → Server  "HISTORY:<n>"     show last n messages from DB
 *   Client → Server  "quit"            disconnect
 * ═══════════════════════════════════════════════════════════════════
 */
public class ClientHandler implements Runnable {

    /**
     * CONCURRENCY – Shared message counter.
     * "static" means ONE counter shared by all ClientHandler instances.
     * "AtomicInteger" makes increment thread-safe without synchronized.
     */
    static final AtomicInteger totalMessages = new AtomicInteger(0);

    private final Socket                   socket;
    private final Map<String, PrintWriter> clients;   // shared ConcurrentHashMap

    public ClientHandler(Socket socket, Map<String, PrintWriter> clients) {
        this.socket  = socket;
        this.clients = clients;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // run() – the entry point; called by the thread pool thread
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * TODO 1: Implement the run() method following these steps:
     *
     * SETUP:
     *   a) Create a BufferedReader reading from socket.getInputStream()
     *      (wrap with InputStreamReader first).
     *   b) Create a PrintWriter writing to socket.getOutputStream()
     *      with autoFlush=true (second constructor argument).
     *
     * STEP 1 – NAME REGISTRATION:
     *   c) Send the welcome prompt:
     *        out.println("[Server] Welcome! Send NAME:<yourname> to register.");
     *   d) Read the first line with in.readLine().
     *   e) If it starts with "NAME:", extract the username:
     *        username = line.substring(5).trim()
     *
     * STEP 2 – SAVE USER TO DB:
     *   f) Get the DatabaseManager instance.
     *   g) Create a UserDAOImpl with the connection.
     *   h) Call userDAO.save(new User(username)).
     *      Wrap in try-catch(SQLException) so a DB failure doesn't crash the chat.
     *
     * STEP 3 – REGISTER IN SHARED MAP:
     *   i) clients.put(username, out)       ← adds this client to the ConcurrentHashMap
     *   j) Call ChatServer.broadcast() to announce the new user.
     *
     * STEP 4 – MESSAGE LOOP:
     *   k) While (line = in.readLine()) != null:
     *        - If "quit" → break
     *        - If starts with "HISTORY:" → call handleHistory(out, line)
     *        - Otherwise:
     *            · Create new Message(username, line)
     *            · Call saveMessageToDB(msg)
     *            · totalMessages.incrementAndGet()    ← atomic +1
     *            · ChatServer.broadcast(msg.toString(), clients)
     *
     * FINALLY (always runs, even if an exception was thrown):
     *   l) clients.remove(username)     ← remove from ConcurrentHashMap
     *   m) socket.close()               ← release the OS socket resource
     *   n) Broadcast a "left the chat" message with the total message count.
     */
    @Override
    public void run() {
        String     username = socket.getRemoteSocketAddress().toString();
        PrintWriter out     = null;

        try {
            // TODO 1a: create BufferedReader 'in' from socket input stream
            // HINT: new BufferedReader(new InputStreamReader(socket.getInputStream()))

            // TODO 1b: create PrintWriter 'out' from socket output stream (autoFlush=true)
            // HINT: new PrintWriter(socket.getOutputStream(), true)

            // TODO 1c: send welcome prompt via out.println(...)

            // TODO 1d: read first line
            // String firstLine = in.readLine();

            // TODO 1e: extract username from "NAME:<name>"

            // TODO 1f-h: save user to DB (wrap in try-catch)

            // TODO 1i: register in shared map:  clients.put(username, out)

            // TODO 1j: broadcast join message via ChatServer.broadcast(...)

            // TODO 1k: message loop
            // String line;
            // while ((line = in.readLine()) != null) { ... }

        } catch (IOException e) {
            System.err.println("[Server] I/O error for '" + username + "': " + e.getMessage());
        } finally {
            // TODO 1l: clients.remove(username)
            // TODO 1m: socket.close() – wrap in try-catch IOException
            // TODO 1n: broadcast left-chat message
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // handleHistory
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * TODO 2: Respond to the HISTORY:<n> command.
     *
     * Steps:
     *   a) Parse n from the command string (substring after "HISTORY:").
     *      If parsing fails, default to 10.
     *   b) Send "--- Last n messages ---" to the client.
     *   c) Get DatabaseManager and create MessageDAOImpl.
     *   d) Call msgDAO.getRecent(n) to get the list.
     *   e) Print each message to the client, or "(no messages yet)" if empty.
     *   f) Send "--- End of history ---".
     *   Wrap DB calls in try-catch and send the error text to the client on failure.
     */
    private void handleHistory(PrintWriter out, String command) {
        int n = 10;
        // TODO 2a: parse n from command.substring(8).trim(), catch NumberFormatException

        // TODO 2b-f: fetch and send history
    }

    // ─────────────────────────────────────────────────────────────────────────
    // saveMessageToDB
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * TODO 3: Persist one Message to the database.
     *
     * Steps:
     *   a) Get DatabaseManager.getInstance()
     *   b) Create MessageDAOImpl with the connection.
     *   c) Call msgDAO.save(msg).
     *   d) Catch SQLException and print a warning (do NOT rethrow – a DB
     *      failure should not crash the chat).
     */
    private void saveMessageToDB(Message msg) {
        // TODO 3
    }
}
