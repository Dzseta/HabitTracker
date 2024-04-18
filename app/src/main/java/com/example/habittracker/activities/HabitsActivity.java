package com.example.habittracker.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.habittracker.R;
import com.example.habittracker.adapters.DatabaseHandler;
import com.example.habittracker.adapters.HabitsAdapter;
import com.example.habittracker.models.HabitModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class HabitsActivity extends AppCompatActivity {

    public View hamburgerMenu;
    private ImageView habitsIW;
    private TextView habitsTW;
    private ImageView createButton;
    private DatabaseHandler dbHandler;
    private HabitsAdapter habitsAdapter;
    private RecyclerView habitsRecyclerView;
    private ArrayList<HabitModel> habitsArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits);

        // hamburger menu
        hamburgerMenu = findViewById(R.id.hamburgerMenu);
        habitsIW = findViewById(R.id.habitsImageView);
        habitsIW.setColorFilter(ContextCompat.getColor(this, R.color.light_gray));
        habitsTW = findViewById(R.id.habitsTextView);
        habitsTW.setTextColor(ContextCompat.getColor(this, R.color.light_gray));
        // buttons
        createButton = findViewById(R.id.createButton);
        // database handler
        dbHandler = new DatabaseHandler(HabitsActivity.this);
        dbHandler.deleteHabit("tttzzzjdkgnh");
        // get habits
        habitsArrayList = dbHandler.readAllHabits();
        // passing list to adapter
        habitsAdapter = new HabitsAdapter(habitsArrayList, HabitsActivity.this);
        habitsRecyclerView = findViewById(R.id.habitsRecyclerView);
        // setting layout manager for recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HabitsActivity.this, RecyclerView.VERTICAL, false);
        habitsRecyclerView.setLayoutManager(linearLayoutManager);
        // setting adapter to recycler view
        habitsRecyclerView.setAdapter(habitsAdapter);

        createButton.setOnClickListener(view -> {
            Intent i = new Intent();
            i.putExtra("mode", "new");
            i.setClass(this, NewHabitActivity.class);
            startActivity(i);
        });
    }

    // ############################################ ONCLICKS #####################################################
    // open and close the hamburger menu
    public void openCloseHamburgerMenu(View view) {
        if (hamburgerMenu.getVisibility() == View.VISIBLE) {
            hamburgerMenu.setVisibility(View.INVISIBLE);
        } else {
            hamburgerMenu.setVisibility(View.VISIBLE);
        }
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
            case R.id.goalsButton:
                i.setClass(this, GoalsActivity.class);
                startActivity(i);
                break;
            case R.id.categoriesButton:
                i.setClass(this, CategoriesActivity.class);
                startActivity(i);
                break;
            case R.id.habitsButton:
                i.setClass(this, HabitsActivity.class);
                startActivity(i);
                break;
            case R.id.settingsButton:
                i.setClass(this, SettingsActivity.class);
                startActivity(i);
                break;
            case R.id.profileButton:
                i.setClass(this, ProfileActivity.class);
                startActivity(i);
                break;
            case R.id.backupButton:
                i.setClass(this, BackupActivity.class);
                startActivity(i);
                break;
            case R.id.ratingButton:
                i.setClass(this, RatingActivity.class);
                startActivity(i);
                break;
            default: break;
        }
    }
}