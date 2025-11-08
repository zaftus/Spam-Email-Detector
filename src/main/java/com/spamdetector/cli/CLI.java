package com.spamdetector.cli;

import com.spamdetector.data.DatasetLoader;
import com.spamdetector.data.EmailRecord;
import com.spamdetector.model.NaiveBayesClassifier;
import com.spamdetector.model.ModelSerializer;
import com.spamdetector.eval.Evaluator;
import com.spamdetector.nlp.Preprocessor;
import com.spamdetector.model.TrainingResult;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class CLI {
    private final Scanner scanner = new Scanner(System.in);

    public void run(String[] args) {
        System.out.println("Spam Detector CLI");
        System.out.println("-----------------");
        System.out.println("Commands: train <csv-path> <model-out.json> | eval <csv-path> <model.json> | predict <model.json>");
        if (args.length >= 1) {
            handleArgs(args);
            return;
        }
        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine();
            if (line == null || line.trim().equalsIgnoreCase("quit") || line.trim().equalsIgnoreCase("exit")) {
                break;
            }
            String[] parts = line.trim().split("\\s+");
            if (parts.length == 0) continue;
            handleArgs(parts);
        }
    }

    private void handleArgs(String[] args) {
        try {
            String cmd = args[0].toLowerCase();
            switch (cmd) {
                case "train":
                    if (args.length < 3) {
                        System.out.println("Usage: train <csv-path> <model-out.json>");
                        return;
                    }
                    train(args[1], args[2]);
                    break;
                case "eval":
                    if (args.length < 3) {
                        System.out.println("Usage: eval <csv-path> <model.json>");
                        return;
                    }
                    evaluate(args[1], args[2]);
                    break;
                case "predict":
                    if (args.length < 2) {
                        System.out.println("Usage: predict <model.json>");
                        return;
                    }
                    interactivePredict(args[1]);
                    break;
                default:
                    System.out.println("Unknown command: " + cmd);
            }
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void train(String csvPath, String outModel) throws Exception {
        System.out.println("Loading dataset...");
        List<EmailRecord> records = DatasetLoader.loadFromCsv(csvPath);
        Preprocessor pre = new Preprocessor();
        records.forEach(r -> r.setProcessed(pre.process(r.getSubject() + " " + r.getBody())));
        System.out.println("Training classifier...");
        NaiveBayesClassifier nb = new NaiveBayesClassifier();
        TrainingResult res = nb.train(records);
        System.out.println("Training complete. Training accuracy (on same data): " + String.format("%.4f", res.getAccuracy()));
        System.out.println("Saving model to " + outModel);
        ModelSerializer.save(nb, new File(outModel));
    }

    private void evaluate(String csvPath, String modelPath) throws Exception {
        System.out.println("Loading model...");
        NaiveBayesClassifier nb = ModelSerializer.load(new File(modelPath));
        System.out.println("Loading dataset...");
        List<EmailRecord> records = DatasetLoader.loadFromCsv(csvPath);
        Preprocessor pre = new Preprocessor();
        records.forEach(r -> r.setProcessed(pre.process(r.getSubject() + " " + r.getBody())));
        System.out.println("Evaluating...");
        Evaluator eval = new Evaluator();
        eval.evaluate(nb, records);
    }

    private void interactivePredict(String modelPath) throws Exception {
        System.out.println("Loading model...");
        NaiveBayesClassifier nb = ModelSerializer.load(new File(modelPath));
        Preprocessor pre = new Preprocessor();

        System.out.println("Enter email subject (empty to quit):");
        while (true) {
            System.out.print("Subject> ");
            String subj = scanner.nextLine();
            if (subj == null || subj.trim().isEmpty()) break;
            System.out.print("Body> ");
            String body = scanner.nextLine();
            String processed = pre.process(subj + " " + body);
            double probSpam = nb.predictProbability(processed);
            System.out.println("Spam probability: " + String.format("%.4f", probSpam) + " => " + (probSpam >= 0.5 ? "SPAM" : "HAM"));
        }
    }
}
