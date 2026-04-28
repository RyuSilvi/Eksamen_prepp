package sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Example 2 – Multi-Client Chat Server
 *
 * Handles multiple simultaneous clients using a thread pool.
 * Every message from a client is broadcast to ALL connected clients.
 *
 * Protocol (plain text over TCP):
 *   Client sends:  "NAME:Alice"       → registers nickname
 *   Client sends:  any text           → broadcast to everyone
 *   Client sends:  "quit"             → disconnect
 *
 * Key concurrency feature:
 *   ConcurrentHashMap is used as a thread-safe set of active PrintWriters
 *   so that multiple handler threads can broadcast safely without explicit locks.
 *
 * To test without the client class:
 *   Open 2+ terminal windows and run:
 *     telnet localhost 5001
 */
public class MultiClientChatServer {

    private static final int PORT = 5001;

    // Thread-safe set of all connected client output streams
    private static final Set<PrintWriter> clients =
            ConcurrentHashMap.newKeySet();

    // =========================================================================
    // ClientHandler – one instance per connected client, runs on its own thread
    // =========================================================================
    static class ClientHandler implements Runnable {
        private final Socket socket;

        ClientHandler(Socket socket) { this.socket = socket; }

        @Override
        public void run() {
            String clientName = socket.getRemoteSocketAddress().toString();
            PrintWriter out   = null;

            try (BufferedReader in = new BufferedReader(
                         new InputStreamReader(socket.getInputStream()))) {

                out = new PrintWriter(socket.getOutputStream(), true);
                clients.add(out);   // register this client
                broadcast("[Server] " + clientName + " connected. Total clients: " + clients.size());

                String line;
                while ((line = in.readLine()) != null) {
                    if ("quit".equalsIgnoreCase(line.trim())) break;

                    // NAME: registration
                    if (line.startsWith("NAME:")) {
                        clientName = line.substring(5).trim();
                        broadcast("[Server] " + clientName + " joined the chat.");
                    } else {
                        broadcast("[" + clientName + "] " + line);
                    }
                }

            } catch (IOException e) {
                System.err.println("Error with " + clientName + ": " + e.getMessage());
            } finally {
                if (out != null) clients.remove(out);
                try { socket.close(); } catch (IOException ignored) { }
                broadcast("[Server] " + clientName + " disconnected.");
                System.out.println(clientName + " disconnected.");
            }
        }
    }

    // =========================================================================
    // Broadcast – send a message to every connected client
    // =========================================================================
    static void broadcast(String message) {
        System.out.println(message);
        for (PrintWriter client : clients) {
            client.println(message);
        }
    }

    // =========================================================================
    // main
    // =========================================================================
    public static void main(String[] args) throws IOException {
        System.out.println("Chat server listening on port " + PORT);

        ExecutorService pool = Executors.newCachedThreadPool(); // one thread per client

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();    // blocks until client connects
                System.out.println("New connection: " + clientSocket.getRemoteSocketAddress());
                pool.execute(new ClientHandler(clientSocket));
            }
        }
    }
}
