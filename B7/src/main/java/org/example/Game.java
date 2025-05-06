package org.example;

import org.example.Bag;
import org.example.Board;
import org.example.Dictionary;

import java.util.ArrayList;
import java.util.List;

class Game {
    private final Bag bag;
    private final Board board; //tabla unde se inscriu cuv
    private final Dictionary dictionary;//lista de cuvinte valide
    private final List<Player> players = new ArrayList<>();
    private final TurnManager turnManager;// randul jucatorilor
    private Timekeeper timekeeper;// opreste jocul automat

    public Game(int numberOfPlayers) {
        bag = new Bag();
        board = new Board();
        dictionary = new Dictionary();
        turnManager = new TurnManager(numberOfPlayers);

        int i;
        for (i = 1; i <= numberOfPlayers; i++) {
            players.add(new Player("Player" + i, bag, board, dictionary, turnManager));
        }
    }

    //start cronometru
    public void start() {
        timekeeper = new Timekeeper(this, 60000); // 60 secunde limită
        timekeeper.start();

        for (Player p : players) {
            p.start();
        }

        for (Player p : players) {
            try {
                p.join();
            } catch (InterruptedException e) {
                p.interrupt();
            }
        }

        board.printSubmissions();
        announceWinner();
        testPrefixLookup();
    }
    private void testPrefixLookup() {
        System.out.println("\n[Bonus] Testare căutare după prefix cu SimpleTrie...");

        SimpleTrie trie = new SimpleTrie();
        for (String word : dictionary.getWords()) {
            trie.insert(word);
        }

        String prefix = "CO";

        long startParallel = System.nanoTime();
        List<String> parallelResults = trie.searchParallel(prefix);
        long endParallel = System.nanoTime();

        long startTrie = System.nanoTime();
        List<String> trieResults = trie.searchByPrefix(prefix);
        long endTrie = System.nanoTime();

        System.out.println("Rezultate pentru prefixul '" + prefix + "':");
        System.out.println("  Parallel stream: " + parallelResults.size() + " cuvinte găsite în " +
                (endParallel - startParallel) / 1_000_000 + " ms");
        System.out.println("  Trie search:     " + trieResults.size() + " cuvinte găsite în " +
                (endTrie - startTrie) / 1_000_000 + " ms");
    }


    public void stop() {
        for (Player p : players) {
            p.interrupt();
        }
        turnManager.stopGame();
    }

    private void announceWinner() {
        Player winner = null;
        int maxScore = 0;
        for (Player p : players) {
            if (p.getScore() > maxScore) {
                maxScore = p.getScore();
                winner = p;
            }
        }
        if (winner != null) {
            System.out.println("\nCâștigător: " + winner.getName() + " cu scorul: " + maxScore);
        }
    }
}
