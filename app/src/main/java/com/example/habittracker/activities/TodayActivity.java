package com.example.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.habittracker.R;
import com.example.habittracker.adapters.DatabaseHandler;
import com.example.habittracker.adapters.EntriesAdapter;
import com.example.habittracker.fragments.DayEntryFragment;
import com.example.habittracker.fragments.NewCategoryFragment;
import com.example.habittracker.fragments.NewEntryFragment;
import com.example.habittracker.fragments.NewGoalFragment;
import com.example.habittracker.models.CategoryModel;
import com.example.habittracker.models.EntryModel;
import com.example.habittracker.models.GoalModel;
import com.example.habittracker.models.HabitModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TodayActivity extends AppCompatActivity implements DayEntryFragment.ItemClickListener, NewEntryFragment.ItemClickListener{

    private View hamburgerMenu;
    private ImageView todayIW;
    private ImageView leftImageButton;
    private ImageView rightImageButton;
    private TextView todayTW;
    private TextView dateTextView;
    private ImageButton createButton;
    private DatabaseHandler dbHandler;
    private EntriesAdapter entriesAdapter;
    private RecyclerView entriesRecyclerView;
    private ArrayList<EntryModel> entriesArrayList;
    private ArrayList<HabitModel> habitArrayList;
    private DayEntryFragment dayEntryFragment;
    private NewEntryFragment newEntryFragment;
    LocalDate now;
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
        setContentView(R.layout.activity_today);

        // hamburger menu
        hamburgerMenu = findViewById(R.id.hamburgerMenu);
        // toolbar
        todayIW = findViewById(R.id.todayImageView);
        todayIW.setColorFilter(ContextCompat.getColor(this, R.color.light_gray));
        todayTW = findViewById(R.id.todayTextView);
        todayTW.setTextColor(ContextCompat.getColor(this, R.color.light_gray));
        // button
        createButton = findViewById(R.id.createButton);
        // date
        dateTextView = findViewById(R.id.dateTextView);
        rightImageButton = findViewById(R.id.rightImageButton);
        leftImageButton = findViewById(R.id.leftImageButton);
        // database handler
        dbHandler = new DatabaseHandler(TodayActivity.this);
        // get habits
        habitArrayList = dbHandler.readAllHabits();
        // default position is 0
        position = 0;
        // today
        now = LocalDate.now();
        Intent i = getIntent();
        int y = i.getIntExtra("year", -1);
        int m = i.getIntExtra("month", -1);
        int d = i.getIntExtra("day", -1);
        if(y >= 0) {
            if(m<10) {
                if(d<10) {
                    now = LocalDate.parse(y + "-0" + m + "-0" + d);
                } else {
                    now = LocalDate.parse(y + "-0" + m + "-" + d);
                }
            } else {
                if(d<10) {
                    now = LocalDate.parse(y + "-" + m + "-0" + d);
                } else {
                    now = LocalDate.parse(y + "-" + m + "-" + d);
                }
            }
        }
        if(now.isEqual(LocalDate.now())) rightImageButton.setColorFilter(getResources().getColor(R.color.light_gray));
        // passing list to adapter
        entriesRecyclerView = findViewById(R.id.entriesRecyclerView);
        // setting layout manager for recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TodayActivity.this, RecyclerView.VERTICAL, false);
        entriesRecyclerView.setLayoutManager(linearLayoutManager);
        loadEntries();
        if(dbHandler.readDayentryByDate(now.toString()) != null) notifyChange(dbHandler.readDayentryByDate(now.toString()).getMood());

        leftImageButton.setOnClickListener(view -> {
            if(now.isEqual(LocalDate.now())) {
                TypedValue typedValue = new TypedValue();
                getTheme().resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true);
                int c = ContextCompat.getColor(this, typedValue.resourceId);
                rightImageButton.setColorFilter(c);
            }
            now = now.minusDays(1);
            loadEntries();
            if(dbHandler.readDayentryByDate(now.toString()) != null) notifyChange(dbHandler.readDayentryByDate(now.toString()).getMood());
            else notifyChange(0);
        });

        rightImageButton.setOnClickListener(view -> {
            if(!now.isEqual(LocalDate.now())) {
                now = now.plusDays(1);
                loadEntries();
                if(dbHandler.readDayentryByDate(now.toString()) != null) notifyChange(dbHandler.readDayentryByDate(now.toString()).getMood());
                else notifyChange(0);
                if(now.isEqual(LocalDate.now())) rightImageButton.setColorFilter(getResources().getColor(R.color.light_gray));
            }
        });

        // onCLickListener for add button
        createButton.setOnClickListener(view -> {
            // open new goal sheet
            dayEntryFragment = DayEntryFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString("date", now.toString());
            dayEntryFragment.setArguments(bundle);
            dayEntryFragment.show(getSupportFragmentManager(), DayEntryFragment.TAG);
        });

        // get the spinner from the xml
        sortSpinner = findViewById(R.id.sortSpinner);
        String[] sortOptions = {getResources().getString(R.string.sort_category_AZ), getResources().getString(R.string.sort_category_ZA), getResources().getString(R.string.sort_priority_D), getResources().getString(R.string.sort_priority_I), getResources().getString(R.string.sort_AZ), getResources().getString(R.string.sort_ZA)};
        // create an adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sortOptions);
        // set the spinner adapter
        sortSpinner.setAdapter(adapter);
        sortSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position2, long id) {
                        position = sortSpinner.getSelectedItemPosition();
                        switch(position) {
                            case 0:
                                entriesArrayList = sortPriorityDecreasing(entriesArrayList);
                                entriesArrayList = sortCategoryAZ(entriesArrayList);
                                break;
                            case 1:
                                entriesArrayList = sortPriorityDecreasing(entriesArrayList);
                                entriesArrayList = sortCategoryZA(entriesArrayList);
                                break;
                            case 2:
                                entriesArrayList = sortPriorityDecreasing(entriesArrayList);
                                break;
                            case 3:
                                entriesArrayList = sortPriorityIncreasing(entriesArrayList);
                                break;
                            case 4:
                                entriesArrayList = sortNameAZ(entriesArrayList);
                                break;
                            case 5:
                                entriesArrayList = sortNameZA(entriesArrayList);
                                break;
                        }
                        entriesAdapter.notifyDataSetChanged();
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                        position = 0;
                    }
                });
    }

    void loadEntries() {
        dateTextView.setText(now.toString());
        // create missing entries
        for(int i=0; i<habitArrayList.size(); i++) {
            LocalDate startDate = LocalDate.parse(habitArrayList.get(i).getStartDate());
            LocalDate endDate = null;
            String[] days = habitArrayList.get(i).getRepeatType().split("-");
            if(!habitArrayList.get(i).getEndDate().equals("")) endDate = LocalDate.parse(habitArrayList.get(i).getEndDate()).plusDays(1);
            if(startDate.isBefore(now.plusDays(1)) && (endDate == null || endDate.isAfter(now.plusDays(1)))) {
                if(dbHandler.readEntryByHabitAndDate(habitArrayList.get(i).getName(), now.toString()) == null && (days[0].equals("everyday") || ((days.length>now.getDayOfWeek().getValue()-1) && !days[now.getDayOfWeek().getValue()-1].equals("")))) {
                    EntryModel entry = new EntryModel(habitArrayList.get(i).getName(), now.toString(), "", -1);
                    dbHandler.addEntry(entry);
                }
            }
        }
        entriesArrayList = dbHandler.readAllEntriesByDate(now.toString());
        switch(position) {
            case 0:
                entriesArrayList = sortPriorityDecreasing(entriesArrayList);
                entriesArrayList = sortCategoryAZ(entriesArrayList);
                break;
            case 1:
                entriesArrayList = sortPriorityDecreasing(entriesArrayList);
                entriesArrayList = sortCategoryZA(entriesArrayList);
                break;
            case 2:
                entriesArrayList = sortPriorityDecreasing(entriesArrayList);
                break;
            case 3:
                entriesArrayList = sortPriorityIncreasing(entriesArrayList);
                break;
            case 4:
                entriesArrayList = sortNameAZ(entriesArrayList);
                break;
            case 5:
                entriesArrayList = sortNameZA(entriesArrayList);
                break;
        }
        entriesAdapter = new EntriesAdapter(entriesArrayList, TodayActivity.this);
        entriesRecyclerView.setAdapter(entriesAdapter);
        entriesAdapter.notifyDataSetChanged();
    }

    // ############################################ SORT #####################################################
    ArrayList<EntryModel> sortCategoryAZ(ArrayList<EntryModel> entriesArrayList){
        Collections.sort(entriesArrayList, (first, second) -> dbHandler.readHabitByName(first.getHabit()).getCategoryName().compareTo(dbHandler.readHabitByName(second.getHabit()).getCategoryName()));
        return entriesArrayList;
    }

    ArrayList<EntryModel> sortCategoryZA(ArrayList<EntryModel> entriesArrayList){
        Collections.sort(entriesArrayList, (first, second) -> dbHandler.readHabitByName(second.getHabit()).getCategoryName().compareTo(dbHandler.readHabitByName(first.getHabit()).getCategoryName()));
        return entriesArrayList;
    }

    ArrayList<EntryModel> sortPriorityDecreasing(ArrayList<EntryModel> entriesArrayList){
        Collections.sort(entriesArrayList, (first, second) -> Integer.compare(dbHandler.readHabitByName(first.getHabit()).getPriority(), dbHandler.readHabitByName(second.getHabit()).getPriority()));
        return entriesArrayList;
    }

    ArrayList<EntryModel> sortPriorityIncreasing(ArrayList<EntryModel> entriesArrayList){
        Collections.sort(entriesArrayList, (first, second) -> Integer.compare(dbHandler.readHabitByName(second.getHabit()).getPriority(), dbHandler.readHabitByName(first.getHabit()).getPriority()));
        return entriesArrayList;
    }

    ArrayList<EntryModel> sortNameAZ(ArrayList<EntryModel> entriesArrayList){
        Collections.sort(entriesArrayList, Comparator.comparing(EntryModel::getHabit));
        return entriesArrayList;
    }

    ArrayList<EntryModel> sortNameZA(ArrayList<EntryModel> entriesArrayList){
        Collections.sort(entriesArrayList, (first, second) -> second.getHabit().compareTo(first.getHabit()));
        return entriesArrayList;
    }

    // ############################################ ONCLICKS #####################################################
    // show the new category fragment
    public void showBottomSheet(View view, EntryModel entry) {
        newEntryFragment = NewEntryFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("habit", entry.getHabit());
        bundle.putString("date", entry.getDate());
        newEntryFragment.setArguments(bundle);
        newEntryFragment.show(getSupportFragmentManager(), NewCategoryFragment.TAG);
    }

    // change entry
    public void notifyEntryChange(EntryModel entry) {
        for(int i=0; i<entriesArrayList.size(); i++) {
            if(entriesArrayList.get(i).getHabit().equals(entry.getHabit())) {
                entriesArrayList.set(i, entry);
                break;
            }
        }
        dbHandler.updateEntry(entry);
        entriesAdapter.notifyDataSetChanged();
    }

    // change mood
    public void notifyChange(int mood) {
        switch (mood) {
            case 1:
                createButton.setImageDrawable(getResources().getDrawable(R.drawable.icon_mood_very_sad));
                createButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                break;
            case 2:
                createButton.setImageDrawable(getResources().getDrawable(R.drawable.icon_mood_sad));
                createButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange)));
                break;
            case 3:
                createButton.setImageDrawable(getResources().getDrawable(R.drawable.icon_mood_neutral));
                createButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow)));
                break;
            case 4:
                createButton.setImageDrawable(getResources().getDrawable(R.drawable.icon_mood_happy));
                createButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_green)));
                break;
            case 5:
                createButton.setImageDrawable(getResources().getDrawable(R.drawable.icon_mood_very_happy));
                createButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark_green)));
                break;
            default:
                createButton.setImageDrawable(getResources().getDrawable(R.drawable.icon_question));
                TypedValue typedValue = new TypedValue();
                getTheme().resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true);
                int color = ContextCompat.getColor(this, typedValue.resourceId);
                createButton.setBackgroundTintList(ColorStateList.valueOf(color));
        }
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