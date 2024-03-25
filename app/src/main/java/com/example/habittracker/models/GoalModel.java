package com.example.habittracker.models;

public class GoalModel {

    // variables
    private HabitModel habit;
    private int needed;
    private int successes;

    public GoalModel(HabitModel habit, int needed, int successes) {
        this.habit = habit;
        this.needed = needed;
        this.successes = successes;
    }

    public HabitModel getHabit() {
        return habit;
    }

    public void setHabit(HabitModel habit) {
        this.habit = habit;
    }

    public int getNeeded() {
        return needed;
    }

    public void setNeeded(int needed) {
        this.needed = needed;
    }

    public int getSuccesses() {
        return successes;
    }

    public void setSuccesses(int successes) {
        this.successes = successes;
    }
}
