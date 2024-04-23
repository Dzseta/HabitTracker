package com.example.habittracker.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.habittracker.R;
import com.example.habittracker.adapters.DatabaseHandler;
import com.example.habittracker.adapters.HabitsAdapter;
import com.example.habittracker.models.EntryModel;
import com.example.habittracker.models.HabitModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class HabitsActivity extends AppCompatActivity {

    public View hamburgerMenu;
    private ImageView habitsIW;
    private TextView habitsTW;
    private ImageView createButton;
    private DatabaseHandler dbHandler;
    private HabitsAdapter habitsAdapter;
    private RecyclerView habitsRecyclerView;
    private ArrayList<HabitModel> habitsArrayList;
    // spinner
    Spinner sortSpinner;
    // array adapter
    ArrayAdapter<String> adapter;
    // sort's position
    int position;
    // sharedprefs
    private static String PREF_NAME = "optionsSharedPrefs";
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // sharedPrefs
        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String color = prefs.getString("color", "y");
        if(color.equals("r")) setTheme(R.style.Theme_HabitTracker_Red);
        else if(color.equals("g")) setTheme(R.style.Theme_HabitTracker_Green);
        else if(color.equals("b")) setTheme(R.style.Theme_HabitTracker_Blue);
        else setTheme(R.style.Theme_HabitTracker);
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
        // get habits
        habitsArrayList = dbHandler.readAllHabits();
        habitsArrayList = sortPriorityDecreasing(habitsArrayList);
        habitsArrayList = sortCategoryAZ(habitsArrayList);
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

        // get the spinner from the xml
        sortSpinner = findViewById(R.id.sortSpinner);
        String[] sortOptions = {getResources().getString(R.string.sort_category_AZ), getResources().getString(R.string.sort_category_ZA), getResources().getString(R.string.sort_priority_D), getResources().getString(R.string.sort_priority_I), getResources().getString(R.string.sort_AZ), getResources().getString(R.string.sort_ZA)};
        // create an adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sortOptions);
        // default position is 0
        position = 0;
        // set the spinner adapter
        sortSpinner.setAdapter(adapter);
        sortSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position2, long id) {
                        position = sortSpinner.getSelectedItemPosition();
                        switch(position) {
                            case 0:
                                habitsArrayList = sortPriorityDecreasing(habitsArrayList);
                                habitsArrayList = sortCategoryAZ(habitsArrayList);
                                break;
                            case 1:
                                habitsArrayList = sortPriorityDecreasing(habitsArrayList);
                                habitsArrayList = sortCategoryZA(habitsArrayList);
                                break;
                            case 2:
                                habitsArrayList = sortPriorityDecreasing(habitsArrayList);
                                break;
                            case 3:
                                habitsArrayList = sortPriorityIncreasing(habitsArrayList);
                                break;
                            case 4:
                                habitsArrayList = sortNameAZ(habitsArrayList);
                                break;
                            case 5:
                                habitsArrayList = sortNameZA(habitsArrayList);
                                break;
                        }
                        habitsAdapter.notifyDataSetChanged();
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                        position = 0;
                    }
                });
    }

    ArrayList<HabitModel> sortCategoryAZ(ArrayList<HabitModel> habitsArrayList){
        Collections.sort(habitsArrayList, Comparator.comparing(HabitModel::getCategoryName));
        return habitsArrayList;
    }

    ArrayList<HabitModel> sortCategoryZA(ArrayList<HabitModel> habitsArrayList){
        Collections.sort(habitsArrayList, (first, second) -> second.getCategoryName().compareTo(first.getCategoryName()));
        return habitsArrayList;
    }

    ArrayList<HabitModel> sortPriorityDecreasing(ArrayList<HabitModel> habitsArrayList){
        Collections.sort(habitsArrayList, Comparator.comparingInt(HabitModel::getPriority));
        return habitsArrayList;
    }

    ArrayList<HabitModel> sortPriorityIncreasing(ArrayList<HabitModel> habitsArrayList){
        Collections.sort(habitsArrayList, (first, second) -> Integer.compare(second.getPriority(), first.getPriority()));
        return habitsArrayList;
    }

    ArrayList<HabitModel> sortNameAZ(ArrayList<HabitModel> habitsArrayList){
        Collections.sort(habitsArrayList, Comparator.comparing(HabitModel::getName));
        return habitsArrayList;
    }

    ArrayList<HabitModel> sortNameZA(ArrayList<HabitModel> habitsArrayList){
        Collections.sort(habitsArrayList, (first, second) -> second.getName().compareTo(first.getName()));
        return habitsArrayList;
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