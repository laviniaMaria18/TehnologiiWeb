package org.example;

import java.util.*;

public class SimpleTrie {
    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isWord = false;
    }

    private final TrieNode root = new TrieNode();
    private final Set<String> wordSet = new HashSet<>();

    public void insert(String word) {
        TrieNode node = root;
        wordSet.add(word);
        for (char c : word.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
        }
        node.isWord = true;
    }

    public List<String> searchByPrefix(String prefix) {
        List<String> results = new ArrayList<>();
        TrieNode node = root;

        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) return results;
            node = node.children.get(c);
        }

        dfs(node, new StringBuilder(prefix), results);
        return results;
    }

    private void dfs(TrieNode node, StringBuilder path, List<String> results) {
        if (node.isWord) results.add(path.toString());
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            path.append(entry.getKey());
            dfs(entry.getValue(), path, results);
            path.setLength(path.length() - 1);
        }
    }

    public List<String> searchParallel(String prefix) {
        return wordSet.parallelStream()
                .filter(word -> word.startsWith(prefix))
                .toList();
    }
}
