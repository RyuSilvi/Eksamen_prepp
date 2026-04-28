package chatapp.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * SOCKETS / CONCURRENCY – Text-based chat client.
 *
 * After connecting, this client runs two concurrent activities:
 *
 *   Thread 1 (readerThread – background daemon):
 *     Continuously reads lines from the server and prints them.
 *     Runs independently of what the user is typing.
 *
 *   Thread 2 (main thread):
 *     Reads user input from the keyboard and sends it to the server.
 *
 * Why two threads?
 *   Without the reader thread, the client would have to alternate between
 *   "wait for server" and "wait for user input" – you could not receive a
 *   message while the user is typing, and vice versa.
 *   Two threads let both happen simultaneously.
 *
 * ─────────────────────────────────────────────────────────────
 *  USAGE
 * ─────────────────────────────────────────────────────────────
 *   1. Start ChatServer.
 *   2. Run this class (open multiple instances to simulate many users).
 *   3. Type:
 *        NAME:Alice          → register your username
 *        Hello everyone!     → broadcast message
 *        HISTORY:20          → show last 20 messages from DB
 *        quit                → disconnect
 */
public class ChatClient {

    private static final String SERVER_HOST = "localhost";
    private static final int    SERVER_PORT = 5001;

    public static void main(String[] args) {
        System.out.println("[Client] Connecting to " + SERVER_HOST + ":" + SERVER_PORT + " …");

        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)) {

            PrintWriter    out      = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader serverIn = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            // ---- CONCURRENCY: Background thread reads messages from server ----------
            // This thread runs at the same time as the main thread below.
            // "daemon" means it will be killed automatically when the main thread ends.
            Thread readerThread = new Thread(() -> {
                try {
                    String serverMsg;
                    while ((serverMsg = serverIn.readLine()) != null) {
                        System.out.println(serverMsg);
                    }
                } catch (IOException e) {
                    System.out.println("[Client] Lost connection to server.");
                }
            }, "server-reader");

            readerThread.setDaemon(true);   // killed when main thread ends
            readerThread.start();           // start running concurrently

            // ---- Main thread: reads user keyboard input and sends to server ----------
            System.out.println("[Client] Connected.  Commands:");
            System.out.println("  NAME:<name>    – register your username (do this first!)");
            System.out.println("  HISTORY:<n>    – show last n messages");
            System.out.println("  quit           – disconnect");
            System.out.println("─────────────────────────────────────────");

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String userInput = scanner.nextLine();
                out.println(userInput);                      // send to server
                if ("quit".equalsIgnoreCase(userInput.trim())) {
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("[Client] Connection error: " + e.getMessage());
        }

        System.out.println("[Client] Disconnected.  Goodbye!");
    }
}
