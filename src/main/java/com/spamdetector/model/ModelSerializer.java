package com.spamdetector.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class ModelSerializer {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void save(NaiveBayesClassifier model, File out) throws IOException {
        try (Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out), "UTF-8"))) {
            GSON.toJson(model, w);
        }
    }

    public static NaiveBayesClassifier load(File in) throws IOException {
        try (Reader r = new BufferedReader(new InputStreamReader(new FileInputStream(in), "UTF-8"))) {
            NaiveBayesClassifier model = GSON.fromJson(r, NaiveBayesClassifier.class);
            return model;
        }
    }
}
