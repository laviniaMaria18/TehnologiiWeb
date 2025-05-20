package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameServer {
    private static final int PORT = 1234;
    private static boolean running = true;

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (running) {
                Socket socket = serverSocket.accept();
                pool.execute(new ClientThread(socket));
            }
        } catch (IOException e) {
            System.err.println("Server exception: " + e.getMessage());
        }
        pool.shutdown();
        System.out.println("Server shut down.");
    }

    public static void stopServer() {
        running = false;
    }
}
