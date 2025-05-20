package org.example;

import java.util.Random;

public class AIPlayer {
    private final int id;
    private final Random random = new Random();

    public AIPlayer(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int[] generateMove(int[][] board) {
        int size = board.length, x, y;
        do {
            x = random.nextInt(size);
            y = random.nextInt(size);
        } while (board[x][y] != 0);
        return new int[]{x, y};
    }
}
