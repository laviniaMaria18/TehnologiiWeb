package org.example;

import java.net.Socket;

public class Player {
    private final Socket socket;
    private long remainingTime = 3 * 60 * 1000;
    private long lastMoveTime = System.currentTimeMillis();

    public Player(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void updateTime() {
        long now = System.currentTimeMillis();
        remainingTime -= (now - lastMoveTime);
        lastMoveTime = now;
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    public void resetTimer() {
        lastMoveTime = System.currentTimeMillis();
    }
}