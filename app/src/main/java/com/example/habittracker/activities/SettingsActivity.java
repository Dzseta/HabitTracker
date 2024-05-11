package com.example.habittracker.activities;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
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
import com.example.habittracker.services.DailyNotification;

import org.checkerframework.checker.units.qual.N;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    public View hamburgerMenu;
    private ImageView settingsIW;
    private ImageView hunImageView;
    private ImageView engImageView;
    private ImageView yellowImageView;
    private ImageView redImageView;
    private ImageView greenImageView;
    private ImageView blueImageView;
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
    // reminder variables
    PendingIntent pendingIntent;
    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // sharedPrefs
        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = prefs.edit();
        String color = prefs.getString("color", "y");
        if(color.equals("r")) setTheme(R.style.Theme_HabitTracker_Red);
        else if(color.equals("g")) setTheme(R.style.Theme_HabitTracker_Green);
        else if(color.equals("b")) setTheme(R.style.Theme_HabitTracker_Blue);
        else setTheme(R.style.Theme_HabitTracker);
        setContentView(R.layout.activity_settings);

        // channel
        createNotificationChannel();
        // time TextView
        timeTextView = findViewById(R.id.timeTextView);
        // flag imageviews
        hunImageView = findViewById(R.id.hunImageView);
        engImageView = findViewById(R.id.engImageView);
        // theme imageviews
        yellowImageView = findViewById(R.id.yellowImageView);
        redImageView = findViewById(R.id.redImageView);
        greenImageView = findViewById(R.id.greenImageView);
        blueImageView = findViewById(R.id.blueImageView);
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
        // alarm manager, intent, pendingintent
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(SettingsActivity.this, DailyNotification.class);
        pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

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
                TypedValue typedValue = new TypedValue();
                getTheme().resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true);
                int c = ContextCompat.getColor(this, typedValue.resourceId);
                reminderSwitch.setThumbTintList(ColorStateList.valueOf(c));
                reminderTimeLinearLayout.setVisibility(View.VISIBLE);
                reminder = true;

                // set alarm
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                if(calendar.after(Calendar.getInstance())) calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)-1);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            } else {
                reminderSwitch.setChecked(false);
                TypedValue typedValue = new TypedValue();
                getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimaryVariant, typedValue, true);
                int c = ContextCompat.getColor(this, typedValue.resourceId);
                reminderSwitch.setThumbTintList(ColorStateList.valueOf(c));
                reminderTimeLinearLayout.setVisibility(View.GONE);
                reminder = false;
                if(alarmManager != null) alarmManager.cancel(pendingIntent);
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

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            if(calendar.after(Calendar.getInstance())) calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)-1);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        });

        hunImageView.setOnClickListener(view -> changeLanguage("hu"));
        engImageView.setOnClickListener(view -> changeLanguage("en"));

        yellowImageView.setOnClickListener(view -> changeTheme("y"));
        redImageView.setOnClickListener(view -> changeTheme("r"));
        greenImageView.setOnClickListener(view -> changeTheme("g"));
        blueImageView.setOnClickListener(view -> changeTheme("b"));
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "DailyReminderChannel";
            String description = "Channel for the daily reminders";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("daily", name, importance);
            channel.setDescription(description);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    // ######################################### ONCLICKS ######################################################################
    // choose theme
    public void changeTheme(String color) {
        editor.putString("color", color);
        editor.commit();
        this.recreate();
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