package com.spamdetector.model;

import com.spamdetector.data.EmailRecord;
import com.spamdetector.utils.Utils;

import java.util.*;

public class NaiveBayesClassifier {
    // Vocabulary -> index
    private Map<String, Integer> vocab = new HashMap<>();
    // class priors
    private double priorSpam = 0.5;
    private double priorHam = 0.5;
    // term counts per class
    private Map<String, Integer> spamCounts = new HashMap<>();
    private Map<String, Integer> hamCounts = new HashMap<>();
    // total token counts
    private int totalSpamTokens = 0;
    private int totalHamTokens = 0;
    // smoothing
    private final double alpha = 1.0;

    public NaiveBayesClassifier() {}

    public TrainingResult train(List<EmailRecord> dataset) {
        // Build counts
        int spamDocs = 0, hamDocs = 0;
        for (EmailRecord r : dataset) {
            String label = r.getLabel();
            String processed = r.getProcessed();
            if (processed == null) continue;
            String[] tokens = processed.split("\\s+");
            if ("spam".equals(label)) {
                spamDocs++;
                for (String t : tokens) {
                    spamCounts.put(t, spamCounts.getOrDefault(t, 0) + 1);
                    totalSpamTokens++;
                    vocab.putIfAbsent(t, vocab.size());
                }
            } else {
                hamDocs++;
                for (String t : tokens) {
                    hamCounts.put(t, hamCounts.getOrDefault(t, 0) + 1);
                    totalHamTokens++;
                    vocab.putIfAbsent(t, vocab.size());
                }
            }
        }
        int totalDocs = spamDocs + hamDocs;
        priorSpam = spamDocs / (double) totalDocs;
        priorHam = hamDocs / (double) totalDocs;

        // Evaluate training accuracy
        int correct = 0;
        for (EmailRecord r : dataset) {
            String pred = predict(r.getProcessed()) ? "spam" : "ham";
            if (pred.equals(r.getLabel())) correct++;
        }
        double accuracy = correct / (double) totalDocs;
        return new TrainingResult(accuracy, vocab.size());
    }

    /**
     * Predict label (true = spam) from processed text
     */
    public boolean predict(String processed) {
        return predictProbability(processed) >= 0.5;
    }

    /**
     * Returns probability that text is spam
     */
    public double predictProbability(String processed) {
        if (processed == null || processed.isBlank()) return 0.0;
        String[] tokens = processed.split("\\s+");
        double logSpam = Math.log(priorSpam);
        double logHam = Math.log(priorHam);
        int V = vocab.size();

        for (String t : tokens) {
            int countSpam = spamCounts.getOrDefault(t, 0);
            int countHam = hamCounts.getOrDefault(t, 0);
            double probTGivenSpam = (countSpam + alpha) / (totalSpamTokens + alpha * V);
            double probTGivenHam = (countHam + alpha) / (totalHamTokens + alpha * V);
            // avoid zeros
            logSpam += Math.log(probTGivenSpam);
            logHam += Math.log(probTGivenHam);
        }
        // Convert log odds to probability
        double max = Math.max(logSpam, logHam);
        double expSpam = Math.exp(logSpam - max);
        double expHam = Math.exp(logHam - max);
        double probSpam = expSpam / (expSpam + expHam);
        return probSpam;
    }

    // Getters for serialization
    public Map<String, Integer> getVocab() { return vocab; }
    public double getPriorSpam() { return priorSpam; }
    public double getPriorHam() { return priorHam; }
    public Map<String, Integer> getSpamCounts() { return spamCounts; }
    public Map<String, Integer> getHamCounts() { return hamCounts; }
    public int getTotalSpamTokens() { return totalSpamTokens; }
    public int getTotalHamTokens() { return totalHamTokens; }
    public double getAlpha() { return alpha; }

    // Setters for deserialization
    public void setVocab(Map<String, Integer> vocab) { this.vocab = vocab; }
    public void setPriorSpam(double priorSpam) { this.priorSpam = priorSpam; }
    public void setPriorHam(double priorHam) { this.priorHam = priorHam; }
    public void setSpamCounts(Map<String, Integer> spamCounts) { this.spamCounts = spamCounts; }
    public void setHamCounts(Map<String, Integer> hamCounts) { this.hamCounts = hamCounts; }
    public void setTotalSpamTokens(int totalSpamTokens) { this.totalSpamTokens = totalSpamTokens; }
    public void setTotalHamTokens(int totalHamTokens) { this.totalHamTokens = totalHamTokens; }
}
