package org.example;

import java.net.Socket;
import java.util.*;

public class GameManager {
    private static final Map<Integer, Game> games = new HashMap<>();
    private static int gameIdCounter = 1;

    public static synchronized String processCommand(String command, Socket socket) {
        String[] tokens = command.trim().split(" ");
        switch (tokens[0]) {
            case "create":
                int id = gameIdCounter++;
                Game newGame = new Game(id);
                games.put(id, newGame);
                return newGame.addPlayer(new Player(socket)) + " Game ID: " + id;
            case "create_ai":
                int idAI = gameIdCounter++;
                Game aiGame = new Game(idAI);
                games.put(idAI, aiGame);
                String result = aiGame.addPlayer(new Player(socket)); // 1. adaugă jucătorul
                aiGame.enableAI();                                     // 2. abia apoi setează AI-ul
                return result + " Playing vs AI. Game ID: " + idAI;

            case "join":
                if (tokens.length < 2) return "Usage: join <gameId>";
                int joinId = Integer.parseInt(tokens[1]);
                Game game = games.get(joinId);
                if (game == null) return "Game not found.";
                return game.addPlayer(new Player(socket));
            case "move":
                if (tokens.length < 4) return "Usage: move <gameId> <x> <y>";
                int gid = Integer.parseInt(tokens[1]);
                int x = Integer.parseInt(tokens[2]);
                int y = Integer.parseInt(tokens[3]);
                Game g = games.get(gid);
                if (g == null) return "Game not found.";
                return g.submitMove(socket, x, y);
            default:
                return "Unknown command.";
        }
    }
}