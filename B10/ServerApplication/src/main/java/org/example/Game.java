package org.example;

import java.net.Socket;
import java.util.Stack;

public class Game {
    private final int id;
    private final int size = 11;
    private final int[][] board = new int[size][size];
    private final Player[] players = new Player[2];
    private int playerCount = 0;
    private int currentPlayer = 0;
    private boolean isVsAI = false;
    private AIPlayer aiPlayer;

    public Game(int id) {
        this.id = id;
    }

    public String addPlayer(Player player) {
        if (playerCount >= 2) return "Game full.";
        players[playerCount++] = player;
        return "Player added to game " + id + ".";
    }

    public void enableAI() {
        this.isVsAI = true;
        this.aiPlayer = new AIPlayer(2);
        this.players[1] = null;
        this.playerCount = 2;
        this.currentPlayer = 0;
    }

    public String submitMove(Socket socket, int x, int y) {
        if ((isVsAI && players[0] == null) || (!isVsAI && (players[0] == null || players[1] == null)))
            return "Waiting for opponent.";

        int playerIndex = getPlayerIndex(socket);
        if (playerIndex != currentPlayer) return "Not your turn.";
        if (!isValidMove(x, y)) return "Invalid move.";

        Player player = players[playerIndex];
        player.updateTime();
        if (player.getRemainingTime() <= 0) return "Time out. Player " + (playerIndex + 1) + " lost.";

        board[x][y] = playerIndex + 1;
        if (checkWin(playerIndex + 1)) return "Player " + (playerIndex + 1) + " wins!";

        currentPlayer = 1 - currentPlayer;
        String result = "Move accepted.";

        if (isVsAI && currentPlayer == 1) {
            int[] aiMove = aiPlayer.generateMove(board);
            board[aiMove[0]][aiMove[1]] = aiPlayer.getId();
            if (checkWin(aiPlayer.getId())) return result + " AI moved at (" + aiMove[0] + "," + aiMove[1] + "). AI wins!";
            currentPlayer = 0;
            result += " AI moved at (" + aiMove[0] + "," + aiMove[1] + ")";
        }

        return result;
    }

    private int getPlayerIndex(Socket socket) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null && players[i].getSocket().equals(socket)) return i;
        }
        return -1;
    }

    private boolean isValidMove(int x, int y) {
        return x >= 0 && y >= 0 && x < size && y < size && board[x][y] == 0;
    }

    public boolean checkWin(int playerId) {
        boolean[][] visited = new boolean[size][size];
        Stack<int[]> stack = new Stack<>();
        if (playerId == 1) {
            for (int i = 0; i < size; i++) if (board[i][0] == playerId) stack.push(new int[]{i, 0});
        } else {
            for (int i = 0; i < size; i++) if (board[0][i] == playerId) stack.push(new int[]{0, i});
        }

        while (!stack.isEmpty()) {
            int[] cell = stack.pop();
            int x = cell[0], y = cell[1];
            if (visited[x][y]) continue;
            visited[x][y] = true;
            if ((playerId == 1 && y == size - 1) || (playerId == 2 && x == size - 1)) return true;
            for (int[] dir : directions()) {
                int nx = x + dir[0], ny = y + dir[1];
                if (inBounds(nx, ny) && board[nx][ny] == playerId && !visited[nx][ny])
                    stack.push(new int[]{nx, ny});
            }
        }
        return false;
    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < size && y < size;
    }

    private int[][] directions() {
        return new int[][]{{-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}};
    }
}