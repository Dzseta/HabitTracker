package com.example.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.habittracker.R;
import com.example.habittracker.adapters.CategoriesAdapter;
import com.example.habittracker.adapters.DatabaseHandler;
import com.example.habittracker.adapters.GoalsAdapter;
import com.example.habittracker.models.CategoryModel;
import com.example.habittracker.models.GoalModel;

import java.util.ArrayList;

public class GoalsActivity extends AppCompatActivity {

    private View hamburgerMenu;
    private ImageView goalsIW;
    private TextView goalsTW;
    private ImageButton createButton;
    private DatabaseHandler dbHandler;
    private GoalsAdapter goalsAdapter;
    private RecyclerView goalsRecyclerView;
    private ArrayList<GoalModel> goalsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        // hamburger menu
        hamburgerMenu = findViewById(R.id.hamburgerMenu);
        // toolbar
        goalsIW = findViewById(R.id.goalsImageView);
        goalsIW.setColorFilter(ContextCompat.getColor(this, R.color.light_gray));
        goalsTW = findViewById(R.id.goalsTextView);
        goalsTW.setTextColor(ContextCompat.getColor(this, R.color.light_gray));
        // buttons
        createButton = findViewById(R.id.createButton);
        // database handler
        dbHandler = new DatabaseHandler(GoalsActivity.this);
        // get goals
        goalsArrayList = dbHandler.readAllGoals();
        // passing list to adapter
        goalsAdapter = new GoalsAdapter(goalsArrayList, GoalsActivity.this);
        goalsRecyclerView = findViewById(R.id.goalsRecyclerView);
        // setting layout manager for recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GoalsActivity.this, RecyclerView.VERTICAL, false);
        goalsRecyclerView.setLayoutManager(linearLayoutManager);
        // setting adapter to recycler view
        goalsRecyclerView.setAdapter(goalsAdapter);

        createButton.setOnClickListener(view -> {
            GoalModel test = new GoalModel("example2", 7, 2);
            GoalModel prev = dbHandler.readGoalByHabit(test.getHabit());
            if(prev == null) {
                dbHandler.addGoal(test);
                goalsArrayList.add(test);
                goalsAdapter.notifyDataSetChanged();
            }
        });
    }

    // ############################### ONCLICKS ##########################################
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