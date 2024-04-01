package com.example.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.habittracker.R;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

    public View hamburgerMenu;
    private ImageView settingsIW;
    private TextView settingsTW;
    private LinearLayout reminderTimeLinearLayout;
    private TextView timeTextView;
    private Switch reminderSwitch;
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
        // hamburger menu
        hamburgerMenu = findViewById(R.id.hamburgerMenu);
        settingsIW= findViewById(R.id.settingsImageView);
        settingsIW.setColorFilter(ContextCompat.getColor(this, R.color.light_gray));
        settingsTW = findViewById(R.id.settingsTextView);
        settingsTW.setTextColor(ContextCompat.getColor(this, R.color.light_gray));
        // reminder time selector
        reminderTimeLinearLayout = findViewById(R.id.reminderTimeLinearLayout);
        // reminder switch
        reminderSwitch = findViewById(R.id.reminderSwitch);
        // sharedPrefs
        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = prefs.edit();

        // hour, minute
        hour = prefs.getInt("hour", 0);
        minute = prefs.getInt("minute", 0);
        if(minute<10) timeTextView.setText(hour + ":0" + minute);
        else timeTextView.setText(hour + ":" + minute);

        // reminder
        reminder = prefs.getBoolean("reminder", false);
        if(!reminder) {
            reminderSwitch.setChecked(false);
            reminderTimeLinearLayout.setVisibility(View.INVISIBLE);
        } else {
            reminderSwitch.setChecked(true);
            reminderTimeLinearLayout.setVisibility(View.VISIBLE);
        }

        // turn on or off reminder
        reminderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    reminderSwitch.setChecked(true);
                    reminderTimeLinearLayout.setVisibility(View.VISIBLE);
                    reminder = true;
                } else {
                    reminderSwitch.setChecked(false);
                    reminderTimeLinearLayout.setVisibility(View.INVISIBLE);
                    reminder = false;
                }
                editor.putBoolean("reminder", reminder);
                editor.commit();
            }
        });
    }

    // ############################## ONCLICKS ########################################
    // select time for reminder
    public void selectTime(View v) {
        // initialize Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(SettingsActivity.this, R.style.TimePickerTheme,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour,
                                          int minute) {
                        // set time in textView
                        if(minute<10) timeTextView.setText(hour + ":0" + minute);
                        else timeTextView.setText(hour + ":" + minute);
                        // save time
                        editor.putInt("hour", hour);
                        editor.putInt("minute", minute);
                        editor.commit();
                    }
                }, hour, minute, true);
        timePickerDialog.show();
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