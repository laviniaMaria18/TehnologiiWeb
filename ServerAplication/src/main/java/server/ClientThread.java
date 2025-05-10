package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private GameServer server;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientThread(GameServer server, Socket socket) {
        this.server = server;
        this.clientSocket = socket;
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            String request;
            while ((request = in.readLine()) != null) {
                System.out.println("Received from client: " + request);

                if ("stop".equalsIgnoreCase(request)) {
                    server.stopServerAndNotify(this);
                    break;
                } else {
                    out.println("Server received the request: " + request);
                }
            }

        } catch (IOException e) {
            System.err.println("ClientThread error: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }
}

