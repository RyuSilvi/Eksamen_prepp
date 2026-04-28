package sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Example 2 – Chat Client for MultiClientChatServer
 *
 * Two threads are used:
 *   1. The main thread reads messages the user types and sends them to the server.
 *   2. A background "listener" thread reads incoming messages from the server
 *      and prints them to the console.
 *
 * This way, incoming messages are displayed even while the user is typing.
 */
public class ChatClient {

    private static final String HOST = "localhost";
    private static final int    PORT = 5001;

    public static void main(String[] args) throws IOException {

        System.out.print("Enter your name: ");
        Scanner keyboard = new Scanner(System.in);
        String name = keyboard.nextLine().trim();

        try (Socket       socket = new Socket(HOST, PORT);
             PrintWriter  out    = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader serverIn = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()))) {

            // --- Listener thread: prints messages arriving from the server ---
            Thread listener = new Thread(() -> {
                try {
                    String line;
                    while ((line = serverIn.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            }, "ServerListener");
            listener.setDaemon(true);  // dies when main thread exits
            listener.start();

            // Register nickname
            out.println("NAME:" + name);

            // --- Main thread: read user input and send to server ---
            System.out.println("Connected as '" + name + "'. Type messages (or 'quit' to exit):");
            while (keyboard.hasNextLine()) {
                String line = keyboard.nextLine();
                out.println(line);
                if ("quit".equalsIgnoreCase(line.trim())) break;
            }
        }

        keyboard.close();
        System.out.println("Client exited.");
    }
}
