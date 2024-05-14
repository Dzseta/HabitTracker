package com.example.habittracker.models;

public class BackupModel {

    String uid;
    String categories;
    String habits;
    String goals;
    String dayentries;
    String entries;

    public BackupModel(String uid, String categories, String habits, String goals, String dayentries, String entries) {
        this.uid = uid;
        this.categories = categories;
        this.habits = habits;
        this.goals = goals;
        this.dayentries = dayentries;
        this.entries = entries;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getHabits() {
        return habits;
    }

    public void setHabits(String habits) {
        this.habits = habits;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public String getDayentries() {
        return dayentries;
    }

    public void setDayentries(String dayentries) {
        this.dayentries = dayentries;
    }

    public String getEntries() {
        return entries;
    }

    public void setEntries(String entries) {
        this.entries = entries;
    }
}
