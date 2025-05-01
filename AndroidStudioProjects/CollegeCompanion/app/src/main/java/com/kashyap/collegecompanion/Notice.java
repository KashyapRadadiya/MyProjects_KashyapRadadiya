package com.kashyap.collegecompanion;

public class Notice {
    private String id;
    private String text;
    private long timestamp;

    public Notice() {
        // Required empty constructor for Firestore
    }

    public Notice(String id, String text, long timestamp) {
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