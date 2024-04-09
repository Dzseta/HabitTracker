package com.example.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.habittracker.R;
import com.example.habittracker.adapters.DatabaseHandler;
import com.example.habittracker.adapters.EntriesAdapter;
import com.example.habittracker.adapters.HabitsAdapter;
import com.example.habittracker.models.EntryModel;
import com.example.habittracker.models.HabitModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TodayActivity extends AppCompatActivity {

    private View hamburgerMenu;
    private ImageView todayIW;
    private TextView todayTW;
    private DatabaseHandler dbHandler;
    private EntriesAdapter entriesAdapter;
    private RecyclerView entriesRecyclerView;
    private ArrayList<EntryModel> entriesArrayList;
    private ArrayList<HabitModel> habitArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        // hamburger menu
        hamburgerMenu = findViewById(R.id.hamburgerMenu);
        // toolbar
        todayIW = findViewById(R.id.todayImageView);
        todayIW.setColorFilter(ContextCompat.getColor(this, R.color.light_gray));
        todayTW = findViewById(R.id.todayTextView);
        todayTW.setTextColor(ContextCompat.getColor(this, R.color.light_gray));
        // database handler
        dbHandler = new DatabaseHandler(TodayActivity.this);
        // get habits
        habitArrayList = dbHandler.readAllHabits();
        //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // today
        LocalDate now = LocalDate.now();
        // create missing entries
        for(int i=0; i<habitArrayList.size(); i++) {
            LocalDate startDate = LocalDate.parse(habitArrayList.get(i).getStartDate());
            LocalDate endDate = LocalDate.parse(habitArrayList.get(i).getEndDate());
            if(startDate.isBefore(now) && endDate.isAfter(now)) {
                if(dbHandler.readEntryByHabitAndDate(habitArrayList.get(i).getName(), now.toString()) == null) {
                    EntryModel entry = new EntryModel(habitArrayList.get(i).getName(), now.toString(), "");
                    dbHandler.addEntry(entry);
                }
            }
        }
        // get entries
        entriesArrayList = dbHandler.readAllEntriesByDate(now.toString());
        // passing list to adapter
        entriesAdapter = new EntriesAdapter(entriesArrayList, TodayActivity.this);
        entriesRecyclerView = findViewById(R.id.entriesRecyclerView);
        // setting layout manager for recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TodayActivity.this, RecyclerView.VERTICAL, false);
        entriesRecyclerView.setLayoutManager(linearLayoutManager);
        // setting adapter to recycler view
        entriesRecyclerView.setAdapter(entriesAdapter);
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