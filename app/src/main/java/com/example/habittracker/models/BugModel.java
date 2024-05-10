package com.example.habittracker.models;

public class BugModel {

    private String uid;
    private String time;
    private String bug;

    public BugModel(String uid, String time, String bug) {
        this.uid = uid;
        this.time = time;
        this.bug = bug;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBug() {
        return bug;
    }

    public void setBug(String bug) {
        this.bug = bug;
    }
}
