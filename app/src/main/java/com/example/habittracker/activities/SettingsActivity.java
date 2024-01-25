package com.example.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.habittracker.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    // onClick - open new activity
    public void openActivity(View view) {
        Intent i = new Intent();
        switch (view.getId()) {
            case R.id.todayButton:
                i.setClass(this, TodayActivity.class);
                startActivity(i);
                break;
            case R.id.calendarButton:
                i.setClass(this, CalendarActivity.class);
                startActivity(i);
                break;
            case R.id.statsButton:
                i.setClass(this, StatsActivity.class);
                startActivity(i);
                break;
            case R.id.settingsButton:
                i.setClass(this, SettingsActivity.class);
                startActivity(i);
                break;
            case R.id.habitsButton:
                i.setClass(this, HabitsActivity.class);
                startActivity(i);
                break;
            case R.id.goalsButton:
                i.setClass(this, GoalsActivity.class);
                startActivity(i);
                break;
            case R.id.newHabitButton:
                i.setClass(this, NewHabitActivity.class);
                startActivity(i);
                break;
            case R.id.newCategoryButton:
                i.setClass(this, NewCategoryActivity.class);
                startActivity(i);
                break;
            case R.id.newGoalButton:
                i.setClass(this, NewGoalActivity.class);
                startActivity(i);
                break;
            default: break;
        }
    }
}