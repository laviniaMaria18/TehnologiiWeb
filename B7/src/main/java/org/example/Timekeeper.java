package org.example;

//thread separat care cronometreaza jocul (dupa 60 de sec timpul a expirat)
class Timekeeper extends Thread {
    private final Game game;
    private final long timeLimitMillis;

    public Timekeeper(Game game, long timeLimitMillis) {
        this.game = game;
        this.timeLimitMillis = timeLimitMillis;
        setDaemon(true);
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        while (true) {
            if (System.currentTimeMillis() - startTime >= timeLimitMillis) {
                System.out.println("\nTimpul a expirat! Jocul se oprește.");
                game.stop();
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
