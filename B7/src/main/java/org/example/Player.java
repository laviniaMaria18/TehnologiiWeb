package org.example;

import java.util.*;

class Player extends Thread {
    private final Bag bag;
    private final Board board;
    private final Dictionary dictionary;
    private final TurnManager turnManager;
    private int score = 0;

    public Player(String name, Bag bag, Board board, Dictionary dictionary, TurnManager turnManager) {
        super(name);
        this.bag = bag;
        this.board = board;
        this.dictionary = dictionary;
        this.turnManager = turnManager;
    }

    private Map<Character, Integer> getFrequency(List<Tile> tiles) {
        Map<Character, Integer> freq = new HashMap<>();
        for (Tile tile : tiles) {
            freq.put(tile.getLetter(), freq.getOrDefault(tile.getLetter(), 0) + 1);
        }
        return freq;
    }

    private boolean canForm(String word, List<Tile> hand) {
        Map<Character, Integer> handFreq = getFrequency(hand);
        for (char ch : word.toCharArray()) {
            if (handFreq.getOrDefault(ch, 0) <= 0) {
                return false;
            }
            handFreq.put(ch, handFreq.get(ch) - 1);
        }
        return true;
    }

    private int calculateScore(String word, List<Tile> hand) {
        int wordScore = 0;
        List<Tile> tempHand = new ArrayList<>(hand);
        for (char ch : word.toCharArray()) {
            for (Iterator<Tile> it = tempHand.iterator(); it.hasNext(); ) {
                Tile t = it.next();
                if (t.getLetter() == ch) {
                    wordScore += t.getPoints();
                    it.remove();
                    break;
                }
            }
        }
        return wordScore;
    }

    private List<Tile> removeUsedTiles(String word, List<Tile> hand) {
        List<Tile> remaining = new ArrayList<>(hand);
        for (char ch : word.toCharArray()) {
            for (Iterator<Tile> it = remaining.iterator(); it.hasNext(); ) {
                Tile t = it.next();
                if (t.getLetter() == ch) {
                    it.remove();
                    break;
                }
            }
        }
        return remaining;
    }

    private String chooseWord(List<Tile> hand) {
        List<String> dictWords = new ArrayList<>(dictionary.getWords());
        Collections.shuffle(dictWords);
        for (String word : dictWords) {
            if (canForm(word, hand)) {
                return word;
            }
        }
        return null;
    }

    @Override
    public void run() {
        int myIndex = Integer.parseInt(getName().replace("Player", "")) - 1;
        List<Tile> hand = bag.extractTiles(7);

        while (!bag.isEmpty() && !Thread.currentThread().isInterrupted()) {
            turnManager.waitTurn(myIndex);

            if (!bag.isEmpty() && !hand.isEmpty()) {
                System.out.println(getName() + " are: " + hand);

                String word = chooseWord(hand);
                if (word != null) {
                    int wordScore = calculateScore(word, hand);
                    score += wordScore;
                    board.submitWord(getName(), word, wordScore);
                    hand = removeUsedTiles(word, hand);
                    List<Tile> newTiles = bag.extractTiles(word.length());
                    hand.addAll(newTiles);
                } else {
                    System.out.println(getName() + " nu poate forma un cuvânt, își aruncă mâna.");
                    hand = bag.extractTiles(7);
                }
            }

            turnManager.endTurn();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println(getName() + " se oprește cu scorul: " + score);
    }

    public int getScore() {
        return score;
    }
}
