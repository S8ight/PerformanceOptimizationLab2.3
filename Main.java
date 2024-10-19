package edu.pro;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    private static final String FILE_PATH = "src/edu/pro/txt/harry.txt";
    private static final int TOP_N_WORDS = 30;

    public static List<String> extractWords(String content) {
        List<String> words = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (char c : content.toCharArray()) {
            if (Character.isAlphabetic(c)) {
                sb.append(Character.toLowerCase(c));
            } else if (sb.length() > 0) {
                words.add(sb.toString());
                sb.setLength(0);
            }
        }
        if (sb.length() > 0) {
            words.add(sb.toString());
        }
        return words;
    }

    public static void main(String[] args) {
        try {
            long startTime = System.currentTimeMillis();

            String content = Files.readString(Paths.get(FILE_PATH));

            List<String> words = extractWords(content);

            Map<String, Integer> wordFreq = new HashMap<>();
            for (String word : words) {
                wordFreq.merge(word, 1, Integer::sum);
            }

            PriorityQueue<Map.Entry<String, Integer>> minHeap = new PriorityQueue<>(TOP_N_WORDS, (e1, e2) -> {
                int freqCompare = e1.getValue().compareTo(e2.getValue());
                if (freqCompare != 0) {
                    return freqCompare;
                } else {
                    return e2.getKey().compareTo(e1.getKey());
                }
            });

            for (Map.Entry<String, Integer> entry : wordFreq.entrySet()) {
                if (minHeap.size() < TOP_N_WORDS) {
                    minHeap.offer(entry);
                } else {
                    Map.Entry<String, Integer> top = minHeap.peek();
                    int cmp = minHeap.comparator().compare(entry, top);
                    if (cmp > 0) {
                        minHeap.poll();
                        minHeap.offer(entry);
                    }
                }
            }

            List<Map.Entry<String, Integer>> sortedWords = new ArrayList<>(minHeap);
            sortedWords.sort((e1, e2) -> {
                int freqCompare = e2.getValue().compareTo(e1.getValue());
                if (freqCompare != 0) {
                    return freqCompare;
                } else {
                    return e1.getKey().compareTo(e2.getKey());
                }
            });

            for (Map.Entry<String, Integer> entry : sortedWords) {
                System.out.println(entry.getKey() + " " + entry.getValue());
            }

            long endTime = System.currentTimeMillis();
            System.out.println("Execution time: " + (endTime - startTime) + " ms");

        } catch (IOException e) {
            System.err.println("File reading error: " + e.getMessage());
        }
    }
}
