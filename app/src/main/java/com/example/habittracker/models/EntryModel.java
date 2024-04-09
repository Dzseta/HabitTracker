package com.example.habittracker.models;

public class EntryModel {

    // variables
    private String habit;
    private String date;
    private String data;

    public EntryModel(String habit, String date, String data) {
        this.habit = habit;
        this.date = date;
        this.data = data;
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
}
