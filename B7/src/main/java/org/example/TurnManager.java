package org.example;

//controleaza randul jucatorului
class TurnManager {
    private int currentPlayer = 0;
    private final int totalPlayers;
    private boolean gameRunning = true;

    public TurnManager(int totalPlayers) {
        this.totalPlayers = totalPlayers;
    }

    public synchronized void waitTurn(int playerIndex) {
        while (gameRunning && playerIndex != currentPlayer) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public synchronized void endTurn() {
        currentPlayer = (currentPlayer + 1) % totalPlayers;
        notifyAll();
    }

    public synchronized void stopGame() {
        gameRunning = false;
        notifyAll();
    }
}
