package com.example.habittracker.models;

public class GoalModel {

    // variables
    private String habit;
    private int needed;
    private boolean finished;

    public GoalModel(String habit, int needed, boolean finished) {
        this.habit = habit;
        this.needed = needed;
        this.finished = finished;
    }

    public String getHabit() {
        return habit;
    }

    public void setHabit(String habit) {
        this.habit = habit;
    }

    public int getNeeded() {
        return needed;
    }

    public void setNeeded(int needed) {
        this.needed = needed;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
