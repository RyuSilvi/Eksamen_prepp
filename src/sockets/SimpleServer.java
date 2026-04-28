package sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Example 1 – Simple Echo Server (single-threaded)
 *
 * A server that accepts one client, reads lines from it,
 * echoes each line back in uppercase, and shuts down when
 * the client sends "quit".
 *
 * Run order:
 *   1. Start SimpleServer in one terminal / run configuration.
 *   2. Start SimpleClient in another.
 *
 * Concepts:
 *   ServerSocket – listens for incoming TCP connections on a port
 *   Socket       – represents one end of a TCP connection
 *   BufferedReader / PrintWriter – line-oriented I/O over the socket stream
 */
public class SimpleServer {

    private static final int PORT = 5000;

    public static void main(String[] args) {
        System.out.println("Server starting on port " + PORT + "...");

        // ServerSocket.accept() blocks until a client connects
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Waiting for client...");

            // Accept exactly one client for this simple example
            try (Socket           clientSocket = serverSocket.accept();
                 BufferedReader   in           = new BufferedReader(
                         new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter      out          = new PrintWriter(
                         clientSocket.getOutputStream(), true)) {  // true = auto-flush

                System.out.println("Client connected: " + clientSocket.getRemoteSocketAddress());

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Received: " + inputLine);

                    if ("quit".equalsIgnoreCase(inputLine.trim())) {
                        out.println("Goodbye!");
                        break;
                    }

                    // Echo back in uppercase
                    out.println("ECHO: " + inputLine.toUpperCase());
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }

        System.out.println("Server shut down.");
    }
}
