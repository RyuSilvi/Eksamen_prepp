package chatapp_tutorial.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * ═══════════════════════════════════════════════════════════════════
 * SOCKETS / CONCURRENCY TUTORIAL – ChatClient
 * ═══════════════════════════════════════════════════════════════════
 *
 * CONCEPTS:
 *
 * 1. Socket (client side)
 *    new Socket(host, port) opens a TCP connection to the server.
 *    You then get input/output streams to communicate, just like
 *    reading/writing a file.
 *
 * 2. Two-thread client design
 *    Problem:
 *      • Main thread is BLOCKED waiting for the user to type.
 *      • But we also need to receive messages from the server AT ANY TIME.
 *    Solution:
 *      • Background thread: only reads from server, prints to console.
 *      • Main thread:       only reads from keyboard, sends to server.
 *    The two threads run simultaneously (concurrently).
 *
 * 3. Daemon thread
 *    readerThread.setDaemon(true) means:
 *      "Kill this thread automatically when the main thread ends."
 *    Without daemon=true, the JVM would stay alive waiting for the
 *    reader thread even after the user typed "quit".
 *
 * ─────────────────────────────────────────────────────────────────
 *  USAGE (after ChatServer is running):
 *    1. Run this class.
 *    2. Type:  NAME:YourName      ← register
 *    3. Type:  Hello!             ← broadcast message
 *    4. Type:  HISTORY:10         ← last 10 messages from DB
 *    5. Type:  quit               ← disconnect
 * ═══════════════════════════════════════════════════════════════════
 */
public class ChatClient {

    private static final String SERVER_HOST = "localhost";
    private static final int    SERVER_PORT = 5001;

    public static void main(String[] args) {
        System.out.println("[Client] Connecting to " + SERVER_HOST + ":" + SERVER_PORT + " ...");

        /**
         * TODO 1: Open a Socket connection to the server.
         *
         * Use try-with-resources so the socket closes automatically:
         *   try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)) { ... }
         *
         * Inside the try block:
         *   a) Create a PrintWriter for sending to the server:
         *        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
         *   b) Create a BufferedReader for receiving from the server:
         *        BufferedReader serverIn = new BufferedReader(
         *            new InputStreamReader(socket.getInputStream()));
         */

        // TODO 1: open socket and create streams

        /**
         * TODO 2: Create and start the background reader thread.
         *
         * This thread:
         *   • Runs a loop: while ((msg = serverIn.readLine()) != null)
         *   • Prints each message to System.out
         *   • Catches IOException and prints "[Client] Lost connection."
         *
         * Steps:
         *   a) Create a Thread with a lambda:
         *        Thread readerThread = new Thread(() -> { ... }, "server-reader");
         *   b) Set it as daemon:  readerThread.setDaemon(true)
         *   c) Start it:          readerThread.start()
         *
         * WHY DAEMON?
         *   When the user types "quit" the main thread exits the loop and
         *   the try-with-resources closes the socket.  Without daemon=true
         *   the JVM would wait for the reader thread before exiting.
         *   With daemon=true it is killed automatically.
         */

        // TODO 2: create, configure, and start reader thread

        /**
         * TODO 3: Main thread – read user keyboard input and send to server.
         *
         * Steps:
         *   a) Create a Scanner reading from System.in.
         *   b) Print instructions to the user.
         *   c) Loop: while (scanner.hasNextLine())
         *        - String userInput = scanner.nextLine()
         *        - out.println(userInput)   ← send to server
         *        - if "quit" → break
         */

        // TODO 3: keyboard input loop

        System.out.println("[Client] Disconnected. Goodbye!");
    }
}
