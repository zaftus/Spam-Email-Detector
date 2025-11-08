package com.spamdetector.eval;

import com.spamdetector.data.EmailRecord;
import com.spamdetector.model.NaiveBayesClassifier;

import java.util.List;

public class Evaluator {

    public void evaluate(NaiveBayesClassifier model, List<EmailRecord> testData) {
        int tp = 0, tn = 0, fp = 0, fn = 0;
        for (EmailRecord r : testData) {
            boolean predSpam = model.predict(r.getProcessed());
            boolean actualSpam = "spam".equals(r.getLabel());
            if (predSpam && actualSpam) tp++;
            if (predSpam && !actualSpam) fp++;
            if (!predSpam && actualSpam) fn++;
            if (!predSpam && !actualSpam) tn++;
        }
        int total = tp + tn + fp + fn;
        double accuracy = (tp + tn) / (double) total;
        double precision = tp + fp == 0 ? 0 : tp / (double) (tp + fp);
        double recall = tp + fn == 0 ? 0 : tp / (double) (tp + fn);
        double f1 = (precision + recall) == 0 ? 0 : 2 * precision * recall / (precision + recall);

        System.out.println("Evaluation results:");
        System.out.println("Total: " + total);
        System.out.printf("Accuracy: %.4f\n", accuracy);
        System.out.printf("Precision: %.4f\n", precision);
        System.out.printf("Recall: %.4f\n", recall);
        System.out.printf("F1 Score: %.4f\n", f1);
        System.out.println("Confusion Matrix: TP=" + tp + " FP=" + fp + " FN=" + fn + " TN=" + tn);
    }
}
