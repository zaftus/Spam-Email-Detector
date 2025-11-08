package com.spamdetector.model;

import com.spamdetector.data.EmailRecord;
import com.spamdetector.nlp.Preprocessor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NaiveBayesClassifierTest {

    @Test
    public void trainAndPredictWorks() {
        List<EmailRecord> dataset = new ArrayList<>();
        dataset.add(new EmailRecord("spam", "Win cash", "You won $1000 click here"));
        dataset.add(new EmailRecord("spam", "Cheap meds", "Buy cheap meds online"));
        dataset.add(new EmailRecord("ham", "Meeting", "Let's meet for coffee tomorrow"));
        dataset.add(new EmailRecord("ham", "Invoice", "Please find attached invoice"));

        Preprocessor p = new Preprocessor();
        for (EmailRecord r : dataset) {
            r.setProcessed(p.process(r.getSubject() + " " + r.getBody()));
        }

        NaiveBayesClassifier nb = new NaiveBayesClassifier();
        nb.train(dataset);

        // spam-like message
        String spamMsg = p.process("Cheap medications available now");
        double probSpam = nb.predictProbability(spamMsg);
        assertTrue(probSpam > 0.3, "Expected spam probability > 0.3 for spam-like message");

        // ham-like message
        String hamMsg = p.process("Project meeting scheduled next week");
        double probSpamHam = nb.predictProbability(hamMsg);
        assertTrue(probSpamHam < 0.7, "Expected spam probability < 0.7 for ham-like message");
    }
}
