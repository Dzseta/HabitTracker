package com.example.habittracker.models;

public class EntryModel {

    // variables
    private String habit;
    private String date;
    private String data;
    private int success; // -1 if not set, 0 if false, 1 if true
    private String comment;

    public EntryModel(String habit, String date, String data, int success) {
        this.habit = habit;
        this.date = date;
        this.data = data;
        this.success = success;
        this.comment = "";
    }

    public EntryModel(String habit, String date, String data, int success, String comment) {
        this.habit = habit;
        this.date = date;
        this.data = data;
        this.success = success;
        this.comment = comment;
    }

    public String getHabit() {
        return habit;
    }

    public void setHabit(String habit) {
        this.habit = habit;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
