package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class B7 {
    public static void main(String[] args) {
        generateDictionaryFileIfMissing("dictionary.txt", 1_000_000);

        Game game = new Game(6);
        game.start();
    }

    private static void generateDictionaryFileIfMissing(String fileName, int numberOfWords) {
        File file = new File(fileName);
        if (file.exists()) {
            System.out.println("Fișierul " + fileName + " există deja. Nu este necesară regenerarea.");
            return;
        }

        System.out.println("Generare fișier " + fileName + " cu " + numberOfWords + " cuvinte...");
        try (FileWriter writer = new FileWriter(file)) {
            Random random = new Random();
            String[] prefixes = {"AB", "CO", "TR", "UN", "PRE", "RE", "DE"};

            int i;
            int j;
            for (i = 0; i < numberOfWords; i++) {
                int length = 3 + random.nextInt(6); // lungime între 3-8
                StringBuilder word = new StringBuilder();

                String prefix = prefixes[random.nextInt(prefixes.length)];
                word.append(prefix);

                for (j = prefix.length(); j < length; j++) {
                    char letter = (char) ('A' + random.nextInt(26));
                    word.append(letter);
                }

                writer.write(word.toString());
                writer.write("\n");
            }

            System.out.println("Fișierul " + fileName + " a fost generat cu succes!");
        } catch (IOException e) {
            System.err.println("Eroare la scrierea fișierului: " + e.getMessage());
        }
    }
}
