package com.example.habittracker.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

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

    public int streak(ArrayList<EntryModel> entries){
        if(isFinished()) return needed;
        LocalDate now = LocalDate.now();
        Collections.sort(entries, (first, second) -> second.getDate().compareTo(first.getDate()));

        if(!(entries.get(0).getDate().equals(now.toString()))) {
            now = now.minusDays(1);
        } else if (!entries.get(0).getData().equals("true")) {
            now = now.minusDays(1);
            entries.remove(0);
        }

        int streak = 0;
        for(int i=0; i<entries.size(); i++) {
            LocalDate entryDate = LocalDate.parse(entries.get(i).getDate());
            if(entryDate.isEqual(now) && entries.get(i).getData().equals("true")) {
                now = now.minusDays(1);
                streak++;
            } else {
                break;
            }
        }
        System.out.println(this.getHabit() + " - " + streak);
        return streak;
    }
}
