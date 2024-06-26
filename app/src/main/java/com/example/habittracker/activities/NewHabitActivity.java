package com.example.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.habittracker.R;
import com.example.habittracker.adapters.DatabaseHandler;
import com.example.habittracker.models.CategoryModel;
import com.example.habittracker.models.GoalModel;
import com.example.habittracker.models.HabitModel;
import com.example.habittracker.services.DailyNotification;
import com.example.habittracker.services.HabitNotification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class NewHabitActivity extends AppCompatActivity {

    // database handler
    DatabaseHandler dbHandler;
    // spinner
    Spinner categorySpinner;
    // imageView
    ImageView iconImageView;
    // edittext
    EditText nameEditText;
    EditText descriptionEditText;
    EditText priorityEditText;
    EditText numberEditText;
    // textviews
    TextView mondayTW;
    TextView tuesdayTW;
    TextView wednesdayTW;
    TextView thursdayTW;
    TextView fridayTW;
    TextView saturdayTW;
    TextView sundayTW;
    // radio
    RadioGroup typeRadioGroup;
    RadioButton yesNoRadioButton;
    RadioButton numberRadioButton;
    RadioButton timeRadioButton;
    // switch
    Switch reminderSwitch;
    Switch endDateSwitch;
    Switch repeatSwitch;
    // button
    Button createButton;
    // numberpickers
    NumberPicker hourNumberPicker;
    NumberPicker minuteNumberPicker;
    NumberPicker secondNumberPicker;
    NumberPicker reminderHourNumberPicker;
    NumberPicker reminderMinuteNumberPicker;
    NumberPicker startYearNumberPicker;
    NumberPicker startMonthNumberPicker;
    NumberPicker startDayNumberPicker;
    NumberPicker endYearNumberPicker;
    NumberPicker endMonthNumberPicker;
    NumberPicker endDayNumberPicker;
    // linearlayouts
    LinearLayout numberLinearLayout;
    LinearLayout timeLinearLayout;
    LinearLayout endDateLinearLayout;
    LinearLayout reminderLinearLayout;
    LinearLayout repeatLinearLayout;
    // extras
    String mode;
    String habitName;
    HabitModel origHabit;
    // bools
    boolean monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    // array adapter
    ArrayAdapter<String> adapter;
    // category's position
    int position;
    // categories arraylist
    ArrayList<CategoryModel> categoriesArrayList;
    // category names array
    String[] categoryNames;
    // time
    int hour, minute;
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
        setContentView(R.layout.activity_new_habit);

        // channel
        createNotificationChannel();
        // alarm manager
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // database handler
        dbHandler = new DatabaseHandler(this);
        LocalDate now = LocalDate.now();
        // time
        hour = 0;
        minute = 0;
        // bools
        monday = false;
        tuesday = false;
        wednesday = false;
        thursday = false;
        friday = false;
        saturday = false;
        sunday = false;
        // get numberpickers
        hourNumberPicker = findViewById(R.id.hourNumberPicker);
        hourNumberPicker.setMinValue(0);
        hourNumberPicker.setMaxValue(99);
        minuteNumberPicker = findViewById(R.id.minuteNumberPicker);
        minuteNumberPicker.setMinValue(0);
        minuteNumberPicker.setMaxValue(59);
        secondNumberPicker = findViewById(R.id.secondNumberPicker);
        secondNumberPicker.setMinValue(0);
        secondNumberPicker.setMaxValue(59);
        reminderHourNumberPicker = findViewById(R.id.reminderHourNumberPicker);
        reminderHourNumberPicker.setMinValue(0);
        reminderHourNumberPicker.setMaxValue(23);
        reminderHourNumberPicker.setValue(20);
        reminderMinuteNumberPicker = findViewById(R.id.reminderMinuteNumberPicker);
        reminderMinuteNumberPicker.setMinValue(0);
        reminderMinuteNumberPicker.setMaxValue(59);
        startYearNumberPicker = findViewById(R.id.startYearNumberPicker);
        startYearNumberPicker.setMinValue(2000);
        startYearNumberPicker.setMaxValue(2100);
        startYearNumberPicker.setValue(now.getYear());
        startMonthNumberPicker = findViewById(R.id.startMonthNumberPicker);
        startMonthNumberPicker.setMinValue(1);
        startMonthNumberPicker.setMaxValue(12);
        startMonthNumberPicker.setValue(now.getMonthValue());
        startDayNumberPicker = findViewById(R.id.startDayNumberPicker);
        startDayNumberPicker.setMinValue(1);
        if(now.getMonthValue() == 2) startDayNumberPicker.setMaxValue(28);
        else if(now.getMonthValue() == 4 || now.getMonthValue() == 6 || now.getMonthValue() == 9 || now.getMonthValue() == 11) startDayNumberPicker.setMaxValue(30);
        else startDayNumberPicker.setMaxValue(31);
        startDayNumberPicker.setValue(LocalDate.now().getDayOfMonth());
        endYearNumberPicker = findViewById(R.id.endYearNumberPicker);
        endYearNumberPicker.setMinValue(2000);
        endYearNumberPicker.setMaxValue(2100);
        endYearNumberPicker.setValue(now.plusYears(1).getYear());
        endMonthNumberPicker = findViewById(R.id.endMonthNumberPicker);
        endMonthNumberPicker.setMinValue(1);
        endMonthNumberPicker.setMaxValue(12);
        endMonthNumberPicker.setValue(now.getMonthValue());
        endDayNumberPicker = findViewById(R.id.endDayNumberPicker);
        endDayNumberPicker.setMinValue(1);
        if(now.getMonthValue() == 2) endDayNumberPicker.setMaxValue(28);
        else if(now.getMonthValue() == 4 || now.getMonthValue() == 6 || now.getMonthValue() == 9 || now.getMonthValue() == 11) endDayNumberPicker.setMaxValue(30);
        else endDayNumberPicker.setMaxValue(31);
        endDayNumberPicker.setValue(LocalDate.now().getDayOfMonth());
        // linear layouts
        numberLinearLayout = findViewById(R.id.numberLinearLayout);
        timeLinearLayout = findViewById(R.id.timeLinearLayout);
        endDateLinearLayout = findViewById(R.id.endDateLinearLayout);
        endDateLinearLayout.setVisibility(View.GONE);
        reminderLinearLayout = findViewById(R.id.reminderLinearLayout);
        reminderLinearLayout.setVisibility(View.GONE);
        repeatLinearLayout = findViewById(R.id.repeatLinearLayout);
        repeatLinearLayout.setVisibility(View.GONE);
        // spinner
        categorySpinner = findViewById(R.id.categorySpinner);
        // imageView
        iconImageView = findViewById(R.id.iconImageView);
        // textviews
        mondayTW = findViewById(R.id.mondayTextView);
        tuesdayTW = findViewById(R.id.tuesdayTextView);
        wednesdayTW = findViewById(R.id.wednesDayTextView);
        thursdayTW = findViewById(R.id.thursdayTextView);
        fridayTW = findViewById(R.id.fridayTextView);
        saturdayTW = findViewById(R.id.saturdayTextView);
        sundayTW = findViewById(R.id.sundayTextView);
        // edittexts
        nameEditText = findViewById(R.id.nameEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        numberEditText = findViewById(R.id.numberEditText);
        priorityEditText = findViewById(R.id.priorityEditText);
        // radio
        typeRadioGroup = findViewById(R.id.typeRadioGroup);
        yesNoRadioButton = findViewById(R.id.yesNoRadioButton);
        numberRadioButton = findViewById(R.id.numberRadioButton);
        timeRadioButton = findViewById(R.id.timeRadioButton);
        // switch
        reminderSwitch = findViewById(R.id.reminderSwitch);
        endDateSwitch = findViewById(R.id.endDateSwitch);
        repeatSwitch = findViewById(R.id.repeatSwitch);
        // button
        createButton = findViewById(R.id.createButton);
        // get the spinner from the xml
        categorySpinner = findViewById(R.id.categorySpinner);
        categoriesArrayList = dbHandler.readAllCategories();
        categoryNames = new String[categoriesArrayList.size()+1];
        categoryNames[0] = "";
        for(int i=0; i<categoriesArrayList.size(); i++) {
            categoryNames[i+1] = categoriesArrayList.get(i).getName();
        }
        // create an adapter to describe how the items are displayed
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoryNames);
        // default position is 0
        position = 0;
        //set the spinners adapter to the previously created one.
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position2, long id) {
                        position = categorySpinner.getSelectedItemPosition();
                        if(position == 0) {
                            iconImageView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                            iconImageView.setImageResource(getResources().getIdentifier("icon_categories", "drawable", getPackageName()));
                        } else {
                            iconImageView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(categoriesArrayList.get(position-1).getColor())));
                            iconImageView.setImageResource(getResources().getIdentifier(categoriesArrayList.get(position-1).getIcon(), "drawable", getPackageName()));
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        position = 0;
                    }
                });

        // setup data if in edit mode
        mode = getIntent().getStringExtra("mode");
        if(mode.equals("edit")) {
            habitName = getIntent().getStringExtra("habit");
            origHabit = dbHandler.readHabitByName(habitName);
            nameEditText.setText(origHabit.getName());
            descriptionEditText.setText(origHabit.getDescription());
            priorityEditText.setText(Integer.toString(origHabit.getPriority()));
            createButton.setText(getResources().getString(R.string.button_edit));
            // set type
            yesNoRadioButton.setClickable(false);
            numberRadioButton.setClickable(false);
            timeRadioButton.setClickable(false);
            if(origHabit.getType().equals("yesno")) {
                numberRadioButton.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.medium_gray)));
                numberRadioButton.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.medium_gray)));
                timeRadioButton.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.medium_gray)));
                timeRadioButton.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.medium_gray)));
            } else if(origHabit.getType().equals("number")) {
                numberRadioButton.setChecked(true);
                numberLinearLayout.setVisibility(View.VISIBLE);
                yesNoRadioButton.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.medium_gray)));
                yesNoRadioButton.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.medium_gray)));
                timeRadioButton.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.medium_gray)));
                timeRadioButton.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.medium_gray)));
                numberEditText.setText(origHabit.getTypeData());
            } else {
                timeRadioButton.setChecked(true);
                timeLinearLayout.setVisibility(View.VISIBLE);
                numberRadioButton.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.medium_gray)));
                numberRadioButton.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.medium_gray)));
                yesNoRadioButton.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.medium_gray)));
                yesNoRadioButton.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.medium_gray)));
                String[] d = origHabit.getTypeData().split(":");
                hourNumberPicker.setValue(Integer.parseInt(d[0]));
                minuteNumberPicker.setValue(Integer.parseInt(d[1]));
                secondNumberPicker.setValue(Integer.parseInt(d[2]));
            }
            // set end date
            if(!origHabit.getEndDate().equals("")) {
                endDateSwitch.setChecked(true);
                endDateLinearLayout.setVisibility(View.VISIBLE);
                String[] e = origHabit.getEndDate().split("-");
                endYearNumberPicker.setValue(Integer.parseInt(e[0]));
                endMonthNumberPicker.setValue(Integer.parseInt(e[1]));
                endDayNumberPicker.setValue(Integer.parseInt(e[2]));
            } else {
                TypedValue typedValue = new TypedValue();
                getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimaryVariant, typedValue, true);
                int c = ContextCompat.getColor(this, typedValue.resourceId);
                endDateSwitch.setThumbTintList(ColorStateList.valueOf(c));
            }
            String[] s = origHabit.getStartDate().split("-");
            startYearNumberPicker.setValue(Integer.parseInt(s[0]));
            startMonthNumberPicker.setValue(Integer.parseInt(s[1]));
            startDayNumberPicker.setValue(Integer.parseInt(s[2]));
            if(origHabit.isReminder()) {
                reminderSwitch.setChecked(true);
                reminderLinearLayout.setVisibility(View.VISIBLE);
                reminderHourNumberPicker.setValue(origHabit.getReminderHour());
                reminderMinuteNumberPicker.setValue(origHabit.getReminderMinute());
            } else {
                reminderSwitch.setChecked(false);
                TypedValue typedValue = new TypedValue();
                getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimaryVariant, typedValue, true);
                int c = ContextCompat.getColor(this, typedValue.resourceId);
                reminderSwitch.setThumbTintList(ColorStateList.valueOf(c));
            }
            repeatSwitch.setEnabled(false);
            mondayTW.setEnabled(false);
            tuesdayTW.setEnabled(false);
            wednesdayTW.setEnabled(false);
            thursdayTW.setEnabled(false);
            fridayTW.setEnabled(false);
            saturdayTW.setEnabled(false);
            sundayTW.setEnabled(false);
            if(origHabit.getRepeatType().equals("everyday")) {
                repeatSwitch.setChecked(true);
                repeatLinearLayout.setVisibility(View.GONE);
                repeatSwitch.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.medium_gray)));
            } else {
                repeatSwitch.setChecked(false);
                repeatLinearLayout.setVisibility(View.VISIBLE);
                repeatSwitch.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.medium_gray)));
                TypedValue typedValue = new TypedValue();
                getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimaryVariant, typedValue, true);
                int c = ContextCompat.getColor(this, typedValue.resourceId);
                repeatSwitch.setThumbTintList(ColorStateList.valueOf(c));
                String[] days = origHabit.getRepeatType().split("-");
                for(int i=0; i<days.length; i++) {
                    if(days[i].equals(getResources().getString(R.string.new_habit_monday))) {
                        mondayTW.setTextColor(c);
                        monday = true;
                    }
                    else if(days[i].equals(getResources().getString(R.string.new_habit_tuesday))) {
                        tuesdayTW.setTextColor(c);
                        tuesday = true;
                    }
                    else if(days[i].equals(getResources().getString(R.string.new_habit_wednesday))) {
                        wednesdayTW.setTextColor(c);
                        wednesday = true;
                    }
                    else if(days[i].equals(getResources().getString(R.string.new_habit_thursday))) {
                        thursdayTW.setTextColor(c);
                        thursday = true;
                    }
                    else if(days[i].equals(getResources().getString(R.string.new_habit_friday))) {
                        fridayTW.setTextColor(c);
                        friday = true;
                    }
                    else if(days[i].equals(getResources().getString(R.string.new_habit_saturday))) {
                        saturdayTW.setTextColor(c);
                        saturday = true;
                    }
                    else if(days[i].equals(getResources().getString(R.string.new_habit_sunday))) {
                        sundayTW.setTextColor(c);
                        sunday = true;
                    }
                }
            }
            reminderHourNumberPicker.setValue(origHabit.getReminderHour());
            reminderMinuteNumberPicker.setValue(origHabit.getReminderMinute());
            for(int i=0; i<categoryNames.length; i++) {
                if(categoryNames[i].equals(origHabit.getCategoryName())){
                    position = i;
                    categorySpinner.setSelection(position);
                    break;
                }
            }
        } else {
            // if it's a new habit we don't have to load data
            TypedValue typedValue = new TypedValue();
            getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimaryVariant, typedValue, true);
            int c = ContextCompat.getColor(this, typedValue.resourceId);
            reminderSwitch.setThumbTintList(ColorStateList.valueOf(c));
            endDateSwitch.setThumbTintList(ColorStateList.valueOf(c));
            repeatSwitch.setChecked(true);
            reminderSwitch.setThumbTintList(ColorStateList.valueOf(c));
        }

        // onValueChanges
        startMonthNumberPicker.setOnValueChangedListener((numberPicker, oldVal, newVal) -> {
            if(newVal == 2) {
                if(startDayNumberPicker.getValue() > 28) startDayNumberPicker.setValue(28);
                startDayNumberPicker.setMaxValue(28);
            }
            else if(newVal == 4 || newVal == 6 || newVal == 9 || newVal == 11) {
                if(startDayNumberPicker.getValue() > 30) startDayNumberPicker.setValue(30);
                startDayNumberPicker.setMaxValue(30);
            }
            else startDayNumberPicker.setMaxValue(31);
        });
        endMonthNumberPicker.setOnValueChangedListener((numberPicker, oldVal, newVal) -> {
            if(newVal == 2) {
                if(startDayNumberPicker.getValue() > 28) startDayNumberPicker.setValue(28);
                endDayNumberPicker.setMaxValue(28);
            }
            else if(newVal == 4 || newVal == 6 || newVal == 9 || newVal == 11) {
                if(startDayNumberPicker.getValue() > 30) startDayNumberPicker.setValue(30);
                endDayNumberPicker.setMaxValue(30);
            }
            else endDayNumberPicker.setMaxValue(31);
        });
        // onClickListener for the radio buttons
        typeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    RadioButton radioButton = group.findViewById(checkedId);
                    if(radioButton.getText().equals(getResources().getString(R.string.new_habit_type_yesno))) {
                        numberLinearLayout.setVisibility(View.GONE);
                        timeLinearLayout.setVisibility(View.GONE);
                    } else if (radioButton.getText().equals(getResources().getString(R.string.new_habit_type_number))) {
                        numberLinearLayout.setVisibility(View.VISIBLE);
                        timeLinearLayout.setVisibility(View.GONE);
                    } else {
                        numberLinearLayout.setVisibility(View.GONE);
                        timeLinearLayout.setVisibility(View.VISIBLE);
                    }
                    System.out.println(radioButton.getText());
                });

        // onCheckedChangeListener for end date
        endDateSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                endDateSwitch.setChecked(true);
                endDateLinearLayout.setVisibility(View.VISIBLE);
                TypedValue typedValue = new TypedValue();
                getTheme().resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true);
                int c = ContextCompat.getColor(this, typedValue.resourceId);
                endDateSwitch.setThumbTintList(ColorStateList.valueOf(c));
            } else {
                endDateSwitch.setChecked(false);
                endDateLinearLayout.setVisibility(View.GONE);
                TypedValue typedValue = new TypedValue();
                getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimaryVariant, typedValue, true);
                int c = ContextCompat.getColor(this, typedValue.resourceId);
                endDateSwitch.setThumbTintList(ColorStateList.valueOf(c));
            }
        });
        // onCheckedChangeListener for reminder
        reminderSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                reminderSwitch.setChecked(true);
                reminderLinearLayout.setVisibility(View.VISIBLE);
                TypedValue typedValue = new TypedValue();
                getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true);
                int c = ContextCompat.getColor(this, typedValue.resourceId);
                reminderSwitch.setThumbTintList(ColorStateList.valueOf(c));
            } else {
                reminderSwitch.setChecked(false);
                reminderLinearLayout.setVisibility(View.GONE);
                TypedValue typedValue = new TypedValue();
                getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimaryVariant, typedValue, true);
                int c = ContextCompat.getColor(this, typedValue.resourceId);
                reminderSwitch.setThumbTintList(ColorStateList.valueOf(c));
            }
        });
        // onCheckedChangeListener for repeat
        repeatSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                repeatSwitch.setChecked(true);
                repeatLinearLayout.setVisibility(View.GONE);
                TypedValue typedValue = new TypedValue();
                getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true);
                int c = ContextCompat.getColor(this, typedValue.resourceId);
                repeatSwitch.setThumbTintList(ColorStateList.valueOf(c));
            } else {
                repeatSwitch.setChecked(false);
                repeatLinearLayout.setVisibility(View.VISIBLE);
                TypedValue typedValue = new TypedValue();
                getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimaryVariant, typedValue, true);
                int c = ContextCompat.getColor(this, typedValue.resourceId);
                reminderSwitch.setThumbTintList(ColorStateList.valueOf(c));
            }
        });
        // onClickListener for the day of the week
        mondayTW.setOnClickListener(view -> monday = setDay((TextView) view, monday));
        tuesdayTW.setOnClickListener(view -> tuesday = setDay((TextView) view, tuesday));
        wednesdayTW.setOnClickListener(view -> wednesday = setDay((TextView) view, wednesday));
        thursdayTW.setOnClickListener(view -> thursday = setDay((TextView) view, thursday));
        fridayTW.setOnClickListener(view -> friday = setDay((TextView) view, friday));
        saturdayTW.setOnClickListener(view -> saturday = setDay((TextView) view, saturday));
        sundayTW.setOnClickListener(view -> sunday = setDay((TextView) view, sunday));

        // create button onClick
        createButton.setOnClickListener(view -> {
            // ERRORS
            // the priority can't be empty
            if(priorityEditText.getText().toString().equals("")) {
                Toasty.error(this, getResources().getString(R.string.toast_empty_priority), Toast.LENGTH_SHORT, true).show();
                return;
            } else if(!repeatSwitch.isChecked() && !monday && !tuesday && !wednesday && !thursday && !friday && !saturday && !sunday){
                Toasty.error(this, getResources().getString(R.string.toast_empty_day), Toast.LENGTH_SHORT, true).show();
                return;
            }
            HabitModel habit;
            if(yesNoRadioButton.isChecked()) {
                habit = new HabitModel(categoryNames[position], nameEditText.getText().toString(), descriptionEditText.getText().toString(), "yesno", "", "everyday", "", "", Integer.parseInt(priorityEditText.getText().toString()), reminderSwitch.isChecked(), 0, 0);
            } else if(numberRadioButton.isChecked()) {
                habit = new HabitModel(categoryNames[position], nameEditText.getText().toString(), descriptionEditText.getText().toString(), "number", numberEditText.getText().toString(), "everyday", "", "", Integer.parseInt(priorityEditText.getText().toString()), reminderSwitch.isChecked(), 0, 0);
            } else {
                habit = new HabitModel(categoryNames[position], nameEditText.getText().toString(), descriptionEditText.getText().toString(), "time", hourNumberPicker.getValue() + ":" + minuteNumberPicker.getValue() + ":" + secondNumberPicker.getValue(), "everyday", "", "", Integer.parseInt(priorityEditText.getText().toString()), reminderSwitch.isChecked(), 0, 0);
            }
            LocalDate start;
            if(startMonthNumberPicker.getValue()<10) {
                if(startDayNumberPicker.getValue()<10) {
                    start = LocalDate.parse(startYearNumberPicker.getValue() + "-0" + startMonthNumberPicker.getValue() + "-0" + startDayNumberPicker.getValue());
                } else {
                    start = LocalDate.parse(startYearNumberPicker.getValue() + "-0" + startMonthNumberPicker.getValue() + "-" + startDayNumberPicker.getValue());
                }
            } else {
                if(startDayNumberPicker.getValue()<10) {
                    start = LocalDate.parse(startYearNumberPicker.getValue() + "-" + startMonthNumberPicker.getValue() + "-0" + startDayNumberPicker.getValue());
                } else {
                    start = LocalDate.parse(startYearNumberPicker.getValue() + "-" + startMonthNumberPicker.getValue() + "-" + startDayNumberPicker.getValue());
                }
            }
            LocalDate end;
            habit.setStartDate(start.toString());
            if(endDateSwitch.isChecked()) {
                if(endMonthNumberPicker.getValue()<10) {
                    if(endDayNumberPicker.getValue()<10) {
                        end = LocalDate.parse(endYearNumberPicker.getValue() + "-0" + endMonthNumberPicker.getValue() + "-0" + endDayNumberPicker.getValue());
                    } else {
                        end = LocalDate.parse(endYearNumberPicker.getValue() + "-0" + endMonthNumberPicker.getValue() + "-" + endDayNumberPicker.getValue());
                    }
                } else {
                    if(endYearNumberPicker.getValue()<10) {
                        end = LocalDate.parse(endYearNumberPicker.getValue() + "-" + endMonthNumberPicker.getValue() + "-0" + endDayNumberPicker.getValue());
                    } else {
                        end = LocalDate.parse(endYearNumberPicker.getValue() + "-" + endMonthNumberPicker.getValue() + "-" + endDayNumberPicker.getValue());
                    }
                }
                habit.setEndDate(end.toString());
            }
            String repeatDays = "";
            if(monday) repeatDays += getResources().getString(R.string.new_habit_monday);
            repeatDays += "-";
            if(tuesday) repeatDays += getResources().getString(R.string.new_habit_tuesday);
            repeatDays += "-";
            if(wednesday) repeatDays += getResources().getString(R.string.new_habit_wednesday);
            repeatDays += "-";
            if(thursday) repeatDays += getResources().getString(R.string.new_habit_thursday);
            repeatDays += "-";
            if(friday) repeatDays += getResources().getString(R.string.new_habit_friday);
            repeatDays += "-";
            if(saturday) repeatDays += getResources().getString(R.string.new_habit_saturday);
            repeatDays += "-";
            if(sunday) repeatDays += getResources().getString(R.string.new_habit_sunday);
            if(!repeatSwitch.isChecked()) habit.setRepeatType(repeatDays);
            else habit.setRepeatType("everyday");
            habit.setReminderHour(reminderHourNumberPicker.getValue());
            habit.setReminderMinute(reminderMinuteNumberPicker.getValue());
            HabitModel prev = dbHandler.readHabitByName(habit.getName());
            if(habit.getName().equals("")) {
                Toasty.warning(NewHabitActivity.this, getResources().getString(R.string.toast_empty_name), Toast.LENGTH_SHORT, true).show();
                return;
            } else if(mode.equals("new")) {
                if(prev == null) {
                    dbHandler.addHabit(habit);
                } else {
                    Toasty.error(NewHabitActivity.this, getResources().getString(R.string.toast_used_name), Toast.LENGTH_SHORT, true).show();
                    return;
                }
            } else if (mode.equals("edit")) {
                if(origHabit.getName().equals(habit.getName()) || prev == null) {
                    dbHandler.updateHabit(habit, habitName);
                } else {
                    Toasty.error(NewHabitActivity.this, getResources().getString(R.string.toast_used_name), Toast.LENGTH_SHORT, true).show();
                    return;
                }
            }
            // alarm manager, intent, pendingintent
            int id = dbHandler.readHabitId(habit.getName());
            Intent intent = new Intent(NewHabitActivity.this, HabitNotification.class);
            if(habit.getCategoryName().equals("")) intent.putExtra("icon", "icon_categories");
            else intent.putExtra("icon", dbHandler.readCategoryByName(habit.getCategoryName()).getIcon());
            intent.putExtra("habit", habit.getName());
            intent.putExtra("id",  id);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(NewHabitActivity.this, id, intent, PendingIntent.FLAG_IMMUTABLE);
            if(habit.isReminder()) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, habit.getReminderHour());
                calendar.set(Calendar.MINUTE, habit.getReminderMinute());
                calendar.set(Calendar.SECOND, 0);
                if(calendar.after(Calendar.getInstance())) calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)-1);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            } else {
                if(alarmManager != null) alarmManager.cancel(pendingIntent);
            }
            Intent i = new Intent();
            i.setClass(this, HabitsActivity.class);
            startActivity(i);
        });
    }

    public boolean setDay(TextView view, boolean day) {
        if(day) {
            view.setTextColor(getResources().getColor(R.color.light_gray));
            day = false;
        } else {
            TypedValue typedValue = new TypedValue();
            getTheme().resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true);
            int c = ContextCompat.getColor(this, typedValue.resourceId);
            view.setTextColor(c);
            day = true;
        }
        return  day;
    }

    // create notification channel
    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "HabitReminderChannel";
            String description = "Channel for the habit reminders";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("habit", name, importance);
            channel.setDescription(description);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}