package com.example.habittracker.models;

public class DayentryModel {
    // variables
    private String date;
    private int mood;
    private String comment;

    public DayentryModel(String date, int mood, String comment) {
        this.date = date;
        this.mood = mood;
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMood() {
        return mood;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
