package com.spamdetector.utils;

import java.util.*;

public class Utils {
    public static Map<String, Integer> tokenizeToFreqMap(String processed) {
        Map<String, Integer> map = new HashMap<>();
        if (processed == null) return map;
        String[] tokens = processed.split("\\s+");
        for (String t : tokens) {
            if (t.isBlank()) continue;
            map.put(t, map.getOrDefault(t, 0) + 1);
        }
        return map;
    }

    public static List<String> toList(String... items) {
        return Arrays.asList(items);
    }
}
