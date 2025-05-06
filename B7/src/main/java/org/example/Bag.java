package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

class Bag {
    private final List<Tile> tiles = new ArrayList<>();
    private final Object lock = new Object();

    public Bag() {
        char letter;
        int i;
        int points;
        for (letter = 'A'; letter <= 'Z'; letter++) {
            for (i = 0; i < 10; i++) {
                points = ThreadLocalRandom.current().nextInt(1, 11);
                tiles.add(new Tile(letter, points));
            }
        }
        Collections.shuffle(tiles);
    }

    public List<Tile> extractTiles(int count) {
        int i;
        List<Tile> extracted = new ArrayList<>();
        synchronized (lock) {
            for ( i = 0; i < count && !tiles.isEmpty(); i++) {
                extracted.add(tiles.remove(tiles.size() - 1));
            }
        }
        return extracted;
    }

    public boolean isEmpty() {
        synchronized (lock) {
            return tiles.isEmpty();
        }
    }
}
