package com.spamdetector.nlp;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    /**
     * Simple whitespace + punctuation tokenization
     */
    public static List<String> tokenize(String text) {
        List<String> tokens = new ArrayList<>();
        if (text == null) return tokens;
        // Replace non-alphanumeric with space, keep apostrophes removed
        String cleaned = text.replaceAll("[^a-zA-Z0-9]", " ").toLowerCase();
        for (String t : cleaned.split("\\s+")) {
            if (t.isBlank()) continue;
            tokens.add(t);
        }
        return tokens;
    }
}
