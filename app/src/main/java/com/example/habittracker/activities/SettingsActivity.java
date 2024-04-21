package com.example.habittracker.activities;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.os.LocaleListCompat;

import com.example.habittracker.R;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    public View hamburgerMenu;
    private ImageView settingsIW;
    private ImageView hunImageView;
    private ImageView engImageView;
    private ImageView redImageView;
    private TextView settingsTW;
    private LinearLayout reminderTimeLinearLayout;
    private TextView timeTextView;
    private Switch reminderSwitch;
    private TimePicker reminderTimePicker;
    // sharedprefs
    private static String PREF_NAME = "optionsSharedPrefs";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    // hour, minute
    int hour;
    int minute;
    // reminder
    boolean reminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // time TextView
        timeTextView = findViewById(R.id.timeTextView);
        // flag imageviews
        hunImageView = findViewById(R.id.hunImageView);
        engImageView = findViewById(R.id.engImageView);
        // theme imageviews
        redImageView = findViewById(R.id.redImageView);
        // hamburger menu
        hamburgerMenu = findViewById(R.id.hamburgerMenu);
        settingsIW = findViewById(R.id.settingsImageView);
        settingsIW.setColorFilter(ContextCompat.getColor(this, R.color.light_gray));
        settingsTW = findViewById(R.id.settingsTextView);
        settingsTW.setTextColor(ContextCompat.getColor(this, R.color.light_gray));
        // reminder time selector
        reminderTimeLinearLayout = findViewById(R.id.reminderTimeLinearLayout);
        // reminder switch
        reminderSwitch = findViewById(R.id.reminderSwitch);
        // reminder time picker
        reminderTimePicker = findViewById(R.id.reminderTimePicker);
        reminderTimePicker.setIs24HourView(true);
        // sharedPrefs
        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = prefs.edit();

        // hour, minute
        hour = prefs.getInt("hour", 0);
        minute = prefs.getInt("minute", 0);
        reminderTimePicker.setHour(hour);
        reminderTimePicker.setMinute(minute);

        // reminder
        reminder = prefs.getBoolean("reminder", false);
        if(!reminder) {
            reminderSwitch.setChecked(false);
            reminderTimeLinearLayout.setVisibility(View.GONE);
        } else {
            reminderSwitch.setChecked(true);
            reminderTimeLinearLayout.setVisibility(View.VISIBLE);
        }

        // turn on or off reminder
        reminderSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                reminderSwitch.setChecked(true);
                reminderTimeLinearLayout.setVisibility(View.VISIBLE);
                reminder = true;
            } else {
                reminderSwitch.setChecked(false);
                reminderTimeLinearLayout.setVisibility(View.GONE);
                reminder = false;
            }
            editor.putBoolean("reminder", reminder);
            editor.commit();
        });

        reminderTimePicker.setOnTimeChangedListener((timePicker, h, m) -> {
            hour = h;
            minute = m;
            // save time
            editor.putInt("hour", hour);
            editor.putInt("minute", minute);
            editor.commit();
        });

        hunImageView.setOnClickListener(view -> changeLanguage("hu"));
        engImageView.setOnClickListener(view -> changeLanguage("en"));

        redImageView.setOnClickListener(view -> changeTheme());
    }


    // ######################################### ONCLICKS ######################################################################
    // choose theme
    public void changeTheme() {
        // TODO - sharedpreffel
        setTheme(R.style.Theme_HabitTracker_Red);
    }

    // choose language
    public void changeLanguage(String lang) {
        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(lang);
        AppCompatDelegate.setApplicationLocales(appLocale);
    }

    // open and close the hamburger menu
    public void openCloseHamburgerMenu(View view) {
        if (hamburgerMenu.getVisibility() == View.VISIBLE) {
            hamburgerMenu.setVisibility(View.GONE);
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