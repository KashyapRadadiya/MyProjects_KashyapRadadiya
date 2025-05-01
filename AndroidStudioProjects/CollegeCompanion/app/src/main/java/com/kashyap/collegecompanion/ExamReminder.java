package com.kashyap.collegecompanion;

public class ExamReminder {
    private String id;
    private String text;
    private long timestamp;

    public ExamReminder() {
        // Required empty constructor for Firestore
    }

    public ExamReminder(String id, String text, long timestamp) {
        this.id = id;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }
}