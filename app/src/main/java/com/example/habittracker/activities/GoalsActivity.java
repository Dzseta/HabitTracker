package com.example.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.habittracker.R;
import com.example.habittracker.adapters.DatabaseHandler;
import com.example.habittracker.adapters.GoalsAdapter;
import com.example.habittracker.fragments.NewGoalFragment;
import com.example.habittracker.models.CategoryModel;
import com.example.habittracker.models.GoalModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GoalsActivity extends AppCompatActivity implements NewGoalFragment.ItemClickListener {

    private View hamburgerMenu;
    private ImageView goalsIW;
    private TextView goalsTW;
    private ImageButton createButton;
    private DatabaseHandler dbHandler;
    private GoalsAdapter goalsAdapter;
    private RecyclerView goalsRecyclerView;
    private ArrayList<GoalModel> goalsArrayList;
    private NewGoalFragment newGoalFragment;
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
        goalsArrayList = sortProgressDecreasing(goalsArrayList);
        // passing list to adapter
        goalsAdapter = new GoalsAdapter(goalsArrayList, GoalsActivity.this);
        goalsRecyclerView = findViewById(R.id.goalsRecyclerView);
        // setting layout manager for recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GoalsActivity.this, RecyclerView.VERTICAL, false);
        goalsRecyclerView.setLayoutManager(linearLayoutManager);
        // setting adapter to recycler view
        goalsRecyclerView.setAdapter(goalsAdapter);
        // onCLickListener for add button
        createButton.setOnClickListener(view -> {
            // open new goal sheet
            newGoalFragment = NewGoalFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString("mode", "new");
            newGoalFragment.setArguments(bundle);
            newGoalFragment.show(getSupportFragmentManager(), NewGoalFragment.TAG);
        });
        // get the spinner from the xml
        sortSpinner = findViewById(R.id.sortSpinner);
        String[] sortOptions = {getResources().getString(R.string.sort_progress_d), getResources().getString(R.string.sort_progress_i), getResources().getString(R.string.sort_needed_d), getResources().getString(R.string.sort_needed_i), getResources().getString(R.string.sort_AZ), getResources().getString(R.string.sort_ZA), getResources().getString(R.string.sort_category_AZ), getResources().getString(R.string.sort_category_ZA)};
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
                                goalsArrayList = sortProgressDecreasing(goalsArrayList);
                                break;
                            case 1:
                                goalsArrayList = sortProgressIncreasing(goalsArrayList);
                                break;
                            case 2:
                                goalsArrayList = sortNeededDecreasing(goalsArrayList);
                                break;
                            case 3:
                                goalsArrayList = sortNeededIncreasing(goalsArrayList);
                                break;
                            case 4:
                                goalsArrayList = sortNameAZ(goalsArrayList);
                                break;
                            case 5:
                                goalsArrayList = sortNameZA(goalsArrayList);
                                break;
                            case 6:
                                goalsArrayList = sortCategoryAZ(goalsArrayList);
                                break;
                            case 7:
                                goalsArrayList = sortCategoryZA(goalsArrayList);
                                break;
                        }
                        goalsAdapter.notifyDataSetChanged();
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                        position = 0;
                    }
                });
    }

    ArrayList<GoalModel> sortNameAZ(ArrayList<GoalModel> goalsArrayList){
        Collections.sort(goalsArrayList, Comparator.comparing(GoalModel::getHabit));
        return goalsArrayList;
    }

    ArrayList<GoalModel> sortNameZA(ArrayList<GoalModel> goalsArrayList){
        Collections.sort(goalsArrayList, (first, second) -> second.getHabit().compareTo(first.getHabit()));
        return goalsArrayList;
    }

    ArrayList<GoalModel> sortNeededIncreasing(ArrayList<GoalModel> goalsArrayList){
        Collections.sort(goalsArrayList, Comparator.comparing(GoalModel::getNeeded));
        return goalsArrayList;
    }

    ArrayList<GoalModel> sortNeededDecreasing(ArrayList<GoalModel> goalsArrayList){
        Collections.reverse(sortNeededIncreasing(goalsArrayList));
        return goalsArrayList;
    }

    ArrayList<GoalModel> sortCategoryAZ(ArrayList<GoalModel> goalsArrayList){
        Collections.sort(goalsArrayList, (first, second) -> dbHandler.readHabitByName(first.getHabit()).getCategoryName().compareTo(dbHandler.readHabitByName(second.getHabit()).getCategoryName()));
        return goalsArrayList;
    }

    ArrayList<GoalModel> sortCategoryZA(ArrayList<GoalModel> goalsArrayList){
        Collections.sort(goalsArrayList, (first, second) -> dbHandler.readHabitByName(second.getHabit()).getCategoryName().compareTo(dbHandler.readHabitByName(first.getHabit()).getCategoryName()));
        return goalsArrayList;
    }

    ArrayList<GoalModel> sortProgressIncreasing(ArrayList<GoalModel> goalsArrayList){
        Collections.sort(goalsArrayList, (first, second) -> Double.compare(1.0 * first.streak(dbHandler.readAllEntriesByHabit(first.getHabit())) / first.getNeeded() * 100, 1.0 * second.streak(dbHandler.readAllEntriesByHabit(second.getHabit())) / second.getNeeded() * 100));
        return goalsArrayList;
    }

    ArrayList<GoalModel> sortProgressDecreasing(ArrayList<GoalModel> goalsArrayList){
        Collections.sort(goalsArrayList, (first, second) -> Double.compare(1.0 * second.streak(dbHandler.readAllEntriesByHabit(second.getHabit())) / second.getNeeded() * 100, 1.0 * first.streak(dbHandler.readAllEntriesByHabit(first.getHabit())) / first.getNeeded() * 100));
        return goalsArrayList;
    }

    // ############################### ONCLICKS ##########################################
    // show the new category fragment
    public void showBottomSheet(View view, String habit) {
        NewGoalFragment newGoalFragment = NewGoalFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("mode", "edit");
        bundle.putString("origHabit", habit);
        newGoalFragment.setArguments(bundle);
        newGoalFragment.show(getSupportFragmentManager(), NewGoalFragment.TAG);
    }

    public void notifyChange(GoalModel goal) {
        for(int i=0; i<goalsArrayList.size(); i++) {
            if(goalsArrayList.get(i).getHabit().equals(goal.getHabit())) {
                goalsArrayList.add(i, goal);
                break;
            }
        }
        goalsAdapter.notifyDataSetChanged();
    }

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