package com.spamdetector.nlp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StopWords {
    private static final String[] WORDS = {
            "a","an","the","and","or","but","if","is","it","to","for","with","on","in","at","by","of","from","this","that",
            "you","your","i","we","they","he","she","as","be","are","was","were","so","do","does","did","have","has"
    };

    public static final Set<String> STOP = new HashSet<>(Arrays.asList(WORDS));
}
