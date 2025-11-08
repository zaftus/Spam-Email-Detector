package com.spamdetector.data;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DatasetLoader {

    /**
     * CSV expected to have header: label,subject,body
     */
    public static List<EmailRecord> loadFromCsv(String path) throws IOException {
        Reader in = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
        List<EmailRecord> list = new ArrayList<>();
        for (CSVRecord r : records) {
            String label = r.get("label").trim().toLowerCase();
            String subject = r.get("subject");
            String body = r.get("body");
            list.add(new EmailRecord(label, subject, body));
        }
        return list;
    }
}
