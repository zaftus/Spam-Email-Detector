package com.spamdetector.data;

public class EmailRecord {
    private String label; // "spam" or "ham"
    private String subject;
    private String body;
    private String processed; // preprocessed text

    public EmailRecord(String label, String subject, String body) {
        this.label = label;
        this.subject = subject;
        this.body = body;
    }

    public String getLabel() { return label; }
    public String getSubject() { return subject; }
    public String getBody() { return body; }
    public String getProcessed() { return processed; }
    public void setProcessed(String processed) { this.processed = processed; }
}
