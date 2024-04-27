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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.habittracker.R;
import com.example.habittracker.adapters.CommentsAdapter;
import com.example.habittracker.adapters.DatabaseHandler;
import com.example.habittracker.adapters.EntriesAdapter;
import com.example.habittracker.models.DayentryModel;
import com.example.habittracker.models.EntryModel;
import com.example.habittracker.models.HabitModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.listeners.OnMonthChangeListener;
import sun.bob.mcalendarview.vo.DateData;
import sun.bob.mcalendarview.vo.MarkedDates;

public class CalendarActivity extends AppCompatActivity {

    private View hamburgerMenu;
    private ImageView calendarIW;
    private TextView calendarTW;
    // sharedprefs
    private static String PREF_NAME = "optionsSharedPrefs";
    SharedPreferences prefs;
    // spinner
    Spinner habitSpinner;
    // array adapter
    ArrayAdapter<String> adapter;
    // habit's position
    int position;
    // habits arraylist
    ArrayList<HabitModel> habitsArrayList;
    // habitnames array
    String[] habitNames;
    // database handler
    DatabaseHandler dbHandler;
    // calendar
    MCalendarView calendar;
    String s;
    String e;
    // comments adapter
    private CommentsAdapter commentsAdapter;
    private RecyclerView commentsRecyclerView;

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
        setContentView(R.layout.activity_calendar);

        // dbHandler
        dbHandler = new DatabaseHandler(CalendarActivity.this);

        // passing list to adapter
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        // setting layout manager for recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CalendarActivity.this, RecyclerView.VERTICAL, false);
        commentsRecyclerView.setLayoutManager(linearLayoutManager);
        // hamburger menu
        hamburgerMenu = findViewById(R.id.hamburgerMenu);
        // toolbar
        calendarIW = findViewById(R.id.calendarImageView);
        calendarIW.setColorFilter(ContextCompat.getColor(this, R.color.light_gray));
        calendarTW = findViewById(R.id.calendarTextView);
        calendarTW.setTextColor(ContextCompat.getColor(this, R.color.light_gray));
        // calendar
        calendar = findViewById(R.id.calendar);
        calendar.hasTitle(false);
        LocalDate now = LocalDate.now();
        if(now.getMonthValue()<10) {
            s = now.getYear() + "-0" + now.getMonthValue() + "-01";
            e = now.getYear() + "-0" + now.getMonthValue() + "-" + now.lengthOfMonth();
        } else {
            s = now.getYear() + "-" + now.getMonthValue() + "-01";
            e = now.getYear() + "-" + now.getMonthValue() + "-" + now.lengthOfMonth();
        }

        markDays(s, e, "", true);
        calendar.setOnMonthChangeListener(new OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
                LocalDate now;
                if(month<10) {
                    now = LocalDate.parse(year + "-0" + month + "-01");
                    s = year + "-0" + month + "-01";
                    e = year + "-0" +month + "-" + now.lengthOfMonth();
                } else {
                    now = LocalDate.parse(year + "-" + month + "-01");
                    s = year + "-" + month + "-01";
                    e = year + "-" +month + "-" + now.lengthOfMonth();
                }

                if(position == 0) markDays(s, e, "", true);
                else  markDays(s, e, habitNames[position], false);
            }
        });

        calendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                LocalDate target = LocalDate.now();
                int y = date.getYear();
                int m = date.getMonth();
                int d = date.getDay();
                if(y > 0) {
                    if(m<10) {
                        if(d<10) {
                            target = LocalDate.parse(y + "-0" + m + "-0" + d);
                        } else {
                            target = LocalDate.parse(y + "-0" + m + "-" + d);
                        }
                    } else {
                        if(d<10) {
                            target = LocalDate.parse(y + "-" + m + "-0" + d);
                        } else {
                            target = LocalDate.parse(y + "-" + m + "-" + d);
                        }
                    }
                }
                if(!target.isAfter(LocalDate.now())){
                    Intent i = new Intent();
                    i.putExtra("year", target.getYear());
                    i.putExtra("month", target.getMonthValue());
                    i.putExtra("day", target.getDayOfMonth());
                    i.setClass(getBaseContext(), TodayActivity.class);
                    startActivity(i);
                }
            }
        });

        // get the spinner from the xml
        habitSpinner = findViewById(R.id.habitSpinner);
        habitsArrayList = dbHandler.readAllHabits();
        habitNames = new String[habitsArrayList.size()+1];
        habitNames[0] = getResources().getString(R.string.today_mood);
        for(int i=0; i<habitsArrayList.size(); i++) {
            habitNames[i+1] = habitsArrayList.get(i).getName();
        }
        // create an adapter to describe how the items are displayed
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, habitNames);
        // default position is 0
        position = 0;
        //set the spinners adapter to the previously created one.
        habitSpinner.setAdapter(adapter);
        habitSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position2, long id) {
                        position = habitSpinner.getSelectedItemPosition();
                        if(position == 0) markDays(s, e, "", true);
                        else  markDays(s, e, habitNames[position], false);
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                        position = 0;
                    }
                });
    }

    // mark the days
    public void markDays(String startDay, String endDay, String habit, boolean mood){
        LocalDate habitStart = null;
        LocalDate habitEnd = null;
        if(!mood) {
            habitStart = LocalDate.parse(dbHandler.readHabitByName(habit).getStartDate()); // start of habit
            if(!dbHandler.readHabitByName(habit).getEndDate().equals("")) habitEnd = LocalDate.parse(dbHandler.readHabitByName(habit).getEndDate());     // end of habit
            else habitEnd = LocalDate.MAX;
        }
        LocalDate firstDay = LocalDate.parse(startDay);                                          // yyyy-mm-01
        LocalDate lastDay = LocalDate.parse(endDay);                                             // yyyy-mm-31
        for(int i=0; i<lastDay.lengthOfMonth(); i++) {
            calendar.unMarkDate(lastDay.getYear(), lastDay.getMonthValue(), i+1);
        }
        if(mood) {
            ArrayList<DayentryModel> dayentries = dbHandler.readAllDayentriesInRange(startDay, endDay);
            for(int i=0; i<dayentries.size(); i++) {
                LocalDate date = LocalDate.parse(dayentries.get(i).getDate());
                switch (dayentries.get(i).getMood()) {
                    case 1:
                        calendar.markDate(new DateData(date.getYear(), date.getMonthValue(), date.getDayOfMonth()).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getResources().getColor(R.color.red))));
                        break;
                    case 2:
                        calendar.markDate(new DateData(date.getYear(), date.getMonthValue(), date.getDayOfMonth()).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getResources().getColor(R.color.orange))));
                        break;
                    case 3:
                        calendar.markDate(new DateData(date.getYear(), date.getMonthValue(), date.getDayOfMonth()).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getResources().getColor(R.color.yellow))));
                        break;
                    case 4:
                        calendar.markDate(new DateData(date.getYear(), date.getMonthValue(), date.getDayOfMonth()).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getResources().getColor(R.color.light_green))));
                        break;
                    case 5:
                        calendar.markDate(new DateData(date.getYear(), date.getMonthValue(), date.getDayOfMonth()).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getResources().getColor(R.color.dark_green))));
                        break;
                }
            }
            commentsAdapter = new CommentsAdapter(true, dayentries, CalendarActivity.this);
            commentsRecyclerView.setAdapter(commentsAdapter);
            commentsAdapter.notifyDataSetChanged();
        } else if (habitStart.isBefore(lastDay.plusDays(1)) && habitEnd.isAfter(firstDay.minusDays(1))) {
            ArrayList<EntryModel> entries = dbHandler.readAllEntriesByHabitInRange(habit, startDay, endDay);
            Collections.sort(entries, Comparator.comparing(EntryModel::getDate));
            int s, e;
            if(habitStart.isBefore(firstDay)) s = 1;
            else s = habitStart.getDayOfMonth();
            if(habitEnd.isBefore(lastDay)) e = habitEnd.getDayOfMonth();
            else e = lastDay.getDayOfMonth();

            // days
            String[] days = dbHandler.readHabitByName(habit).getRepeatType().split("-");
            int j = 0;
            for(int i=s; i<=e; i++) {
                LocalDate date = null;
                if(entries.size()>0) date = LocalDate.parse(entries.get(j).getDate());
                if(entries.size()>0 && date.isEqual(firstDay.plusDays(i-1))) {
                    if (j < entries.size()-1) j++;
                    if (entries.get(j).getSuccess() == 1)
                        calendar.markDate(new DateData(date.getYear(), date.getMonthValue(), date.getDayOfMonth()).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getResources().getColor(R.color.dark_green))));
                    else if (entries.get(j).getSuccess() == 0) calendar.markDate(new DateData(date.getYear(), date.getMonthValue(), date.getDayOfMonth()).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getResources().getColor(R.color.red))));
                    else calendar.markDate(new DateData(date.getYear(), date.getMonthValue(), date.getDayOfMonth()).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getResources().getColor(R.color.yellow))));
                } else if(firstDay.plusDays(i-1).isBefore(LocalDate.now().plusDays(1)) && (days[0].equals("everyday") || ((days.length>firstDay.plusDays(i-1).getDayOfWeek().getValue()-1) && !days[firstDay.plusDays(i-1).getDayOfWeek().getValue()-1].equals("")))) {
                    calendar.markDate(new DateData(firstDay.getYear(), firstDay.getMonthValue(), i).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getResources().getColor(R.color.yellow))));
                }
            }
            commentsAdapter = new CommentsAdapter(false, entries, CalendarActivity.this);
            commentsRecyclerView.setAdapter(commentsAdapter);
            commentsAdapter.notifyDataSetChanged();
        }
    }

    // ############################################ ONCLICKS ###############################################################

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