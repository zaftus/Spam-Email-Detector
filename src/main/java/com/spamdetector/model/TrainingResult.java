package com.spamdetector.model;

public class TrainingResult {
    private final double accuracy;
    private final int vocabSize;

    public TrainingResult(double accuracy, int vocabSize) {
        this.accuracy = accuracy;
        this.vocabSize = vocabSize;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public int getVocabSize() {
        return vocabSize;
    }
}
