package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class GameClient {
    public static void main(String[] args) {
        try (
                Socket socket = new Socket("localhost", 1234);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Connected to server.");
            while (true) {
                System.out.print("Enter command: ");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("exit")) break;
                out.println(input);
                String response = in.readLine();
                System.out.println("Server: " + response);
            }
        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}
