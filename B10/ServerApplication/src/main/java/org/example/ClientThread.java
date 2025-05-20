package org.example;

import java.io.*;
import java.net.Socket;

public class ClientThread implements Runnable {
    private final Socket socket;

    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String command;
            while ((command = in.readLine()) != null) {
                if (command.equalsIgnoreCase("stop")) {
                    GameServer.stopServer();
                    out.println("Server stopped");
                    break;
                } else {
                    String response = GameManager.processCommand(command, socket);
                    out.println(response);
                }
            }
        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}