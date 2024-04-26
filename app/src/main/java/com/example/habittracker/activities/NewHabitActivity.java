package com.example.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
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

import com.example.habittracker.R;
import com.example.habittracker.adapters.DatabaseHandler;
import com.example.habittracker.models.CategoryModel;
import com.example.habittracker.models.GoalModel;
import com.example.habittracker.models.HabitModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

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
    // radio
    RadioGroup typeRadioGroup;
    RadioButton yesNoRadioButton;
    RadioButton numberRadioButton;
    RadioButton timeRadioButton;
    // switch
    Switch reminderSwitch;
    Switch endDateSwitch;
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
    // extras
    String mode;
    String habitName;
    HabitModel origHabit;
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

        // database handler
        dbHandler = new DatabaseHandler(this);
        LocalDate now = LocalDate.now();
        // time
        hour = 0;
        minute = 0;
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
        // spinner
        categorySpinner = findViewById(R.id.categorySpinner);
        // imageView
        iconImageView = findViewById(R.id.iconImageView);
        // edittext
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
            TypedValue typedValue = new TypedValue();
            getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimaryVariant, typedValue, true);
            int c = ContextCompat.getColor(this, typedValue.resourceId);
            reminderSwitch.setThumbTintList(ColorStateList.valueOf(c));
            endDateSwitch.setThumbTintList(ColorStateList.valueOf(c));
        }

        // onValueChange
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
        // set button onClick
        createButton.setOnClickListener(view -> {
            HabitModel habit;
            if(yesNoRadioButton.isChecked()) {
                habit = new HabitModel(categoryNames[position], nameEditText.getText().toString(), descriptionEditText.getText().toString(), "yesno", "", "daily", 1, "", "", Integer.parseInt(priorityEditText.getText().toString()), reminderSwitch.isActivated(), 0, 0);
            } else if(numberRadioButton.isChecked()) {
                habit = new HabitModel(categoryNames[position], nameEditText.getText().toString(), descriptionEditText.getText().toString(), "number", numberEditText.getText().toString(), "daily", 1, "", "", Integer.parseInt(priorityEditText.getText().toString()), reminderSwitch.isActivated(), 0, 0);
            } else {
                habit = new HabitModel(categoryNames[position], nameEditText.getText().toString(), descriptionEditText.getText().toString(), "time", hourNumberPicker.getValue() + ":" + minuteNumberPicker.getValue() + ":" + secondNumberPicker.getValue(), "daily", 1, "", "", Integer.parseInt(priorityEditText.getText().toString()), reminderSwitch.isActivated(), 0, 0);
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
            habit.setReminderHour(reminderHourNumberPicker.getValue());
            habit.setReminderMinute(reminderMinuteNumberPicker.getValue());
            HabitModel prev = dbHandler.readHabitByName(habit.getName());
            if(mode.equals("new") && prev == null) {
                dbHandler.addHabit(habit);
            } else if (mode.equals("edit")) {
                dbHandler.updateHabit(habit, habitName);
            }
            Intent i = new Intent();
            i.setClass(this, HabitsActivity.class);
            startActivity(i);
        });
    }
}