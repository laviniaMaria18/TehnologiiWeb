package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
    private ServerSocket serverSocket;
    private boolean running;

    public GameServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            running = true;
            System.out.println("Server started on port " + port);
            run();
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }

    private void run() {
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);
                new ClientThread(this, clientSocket).start();
            } catch (IOException e) {
                System.err.println("Error accepting client: " + e.getMessage());
            }
        }
        stop();
    }

    public void stop() {
        running = false;
        try {
            serverSocket.close();
            System.out.println("Server stopped.");
        } catch (IOException e) {
            System.err.println("Error closing server: " + e.getMessage());
        }
    }

    public void stopServerAndNotify(ClientThread thread) {
        thread.sendMessage("Server stopped");
        stop();
    }

    public static void main(String[] args) {
        int port = 12345;
        new GameServer(port);
    }
}
