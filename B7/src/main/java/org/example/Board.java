package org.example;

import java.util.ArrayList;
import java.util.List;

class Board {
    private final List<String> submissions = new ArrayList<>();
    private final Object lock = new Object();

    public void submitWord(String playerName, String word, int score) {
        synchronized (lock) {
            String entry = playerName + " a înscris cuvântul '" + word + "' (" + score + " puncte)";
            submissions.add(entry);
            System.out.println(entry);
        }
    }

    public void printSubmissions() {
        synchronized (lock) {
            System.out.println("\nCuvinte înscrise pe tablă:");
            for (String s : submissions) {
                System.out.println(s);
            }
        }
    }
}
