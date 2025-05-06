package org.example;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Dictionary {
    private final Set<String> words = new HashSet<>();

    public Dictionary() {
        loadDefault();
    }

    private void loadDefault() {
        try (Scanner scanner = new Scanner(new File("dictionary.txt"))) {
            while (scanner.hasNext()) {
                String word = scanner.next().toUpperCase().replaceAll("[^A-Z]", "");
                if (!word.isEmpty()) {
                    words.add(word);
                }
            }
        } catch (Exception e) {
            words.addAll(List.of("CAT", "DOG", "JAVA", "CODE", "GAME", "HELLO", "WORLD"));
        }
    }

    public Set<String> getWords() {
        return words;
    }
}
