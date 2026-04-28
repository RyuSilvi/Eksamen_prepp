package sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Example 1 – Simple Echo Client
 *
 * Connects to SimpleServer, sends a few messages, prints the responses,
 * then sends "quit" to cleanly close the session.
 *
 * Run SimpleServer FIRST, then run this class.
 */
public class SimpleClient {

    private static final String HOST = "localhost";
    private static final int    PORT = 5000;

    public static void main(String[] args) {
        System.out.println("Connecting to " + HOST + ":" + PORT + "...");

        try (Socket         socket = new Socket(HOST, PORT);
             PrintWriter    out    = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in     = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Connected!");

            String[] messages = {"Hello, Server!", "How are you?", "Java sockets are fun", "quit"};

            for (String msg : messages) {
                System.out.println("Sending: " + msg);
                out.println(msg);

                // Read the server's response
                String response = in.readLine();
                System.out.println("Response: " + response);

                if (response != null && response.equals("Goodbye!")) break;
            }

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }

        System.out.println("Client disconnected.");
    }
}
