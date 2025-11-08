package com.spamdetector.nlp;

import java.util.List;
import java.util.stream.Collectors;

public class Preprocessor {

    public String process(String text) {
        List<String> tokens = Tokenizer.tokenize(text);
        // remove stopwords and short tokens
        tokens = tokens.stream()
                .filter(t -> t.length() > 1)
                .filter(t -> !StopWords.STOP.contains(t))
                .collect(Collectors.toList());
        return String.join(" ", tokens);
    }
}
