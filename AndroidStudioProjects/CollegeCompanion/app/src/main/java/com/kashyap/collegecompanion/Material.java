package com.kashyap.collegecompanion;

public class Material {
    private String id;
    private String title;
    private String link;
    private long timestamp;

    public Material() {
        // Required empty constructor for Firestore
    }

    public Material(String id, String title, String link, long timestamp) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public long getTimestamp() {
        return timestamp;
    }
}