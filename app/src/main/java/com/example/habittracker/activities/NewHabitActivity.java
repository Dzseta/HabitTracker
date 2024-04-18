package com.example.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    // TExtView
    TextView startDateEditText;
    TextView endDateEditText;
    TextView reminderEditText;
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
    // linear layouts
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_habit);

        // database handler
        dbHandler = new DatabaseHandler(this);
        // time
        hour = 0;
        minute = 0;
        // linear layouts
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
        startDateEditText = findViewById(R.id.startDateEditText);
        LocalDate now = LocalDate.now();
        startDateEditText.setText(now.toString());
        endDateEditText = findViewById(R.id.endDateEditText);
        priorityEditText = findViewById(R.id.priorityEditText);
        reminderEditText = findViewById(R.id.reminderEditText);
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
            if(!origHabit.getEndDate().equals("")) {
                endDateSwitch.setChecked(true);
                endDateLinearLayout.setVisibility(View.VISIBLE);
            }
            startDateEditText.setText(origHabit.getStartDate());
            endDateEditText.setText(origHabit.getEndDate());
            if(!origHabit.isReminder()) {
                reminderSwitch.setChecked(true);
                reminderLinearLayout.setVisibility(View.VISIBLE);
            }
            reminderEditText.setText(origHabit.getReminderHour() + ":" + origHabit.getReminderMinute());
            for(int i=0; i<categoryNames.length; i++) {
                if(categoryNames[i].equals(origHabit.getCategoryName())){
                    position = i;
                    categorySpinner.setSelection(position);
                    break;
                }
            }
        }

        // onClickListener for start date button
        startDateEditText.setOnClickListener(v -> {
            final Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(NewHabitActivity.this, (view, yearOfCalendar, monthOfYear, dayOfMonth) -> {
                String date = Integer.toString(yearOfCalendar);
                if(monthOfYear<10) date += "-0" + (monthOfYear+1);
                else date += "-" + (monthOfYear+1);
                if(dayOfMonth<10) date += "-0" + dayOfMonth;
                else date += "-" + dayOfMonth;
                startDateEditText.setText(date);
                System.out.println(monthOfYear);
            },
                    year, month, day);
            datePickerDialog.show();
        });
        // onCheckedChangeListener for end date
        endDateSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                endDateSwitch.setChecked(true);
                endDateEditText.setText("2099-01-01");
                endDateLinearLayout.setVisibility(View.VISIBLE);
            } else {
                endDateSwitch.setChecked(false);
                endDateEditText.setText("");
                endDateLinearLayout.setVisibility(View.GONE);
            }
        });
        // onClickListener for end date button
        endDateEditText.setOnClickListener(v -> {
            final Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(NewHabitActivity.this, (view, yearOfCalendar, monthOfYear, dayOfMonth) -> {
                String date = Integer.toString(yearOfCalendar);
                if(monthOfYear<10) date += "-0" + (monthOfYear+1);
                else date += "-" + (monthOfYear+1);
                if(dayOfMonth<10) date += "-0" + dayOfMonth;
                else date += "-" + dayOfMonth;
                endDateEditText.setText(date);
            },
                    year, month, day);
            datePickerDialog.show();
        });
        // onCheckedChangeListener for reminder
        reminderSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                reminderSwitch.setChecked(true);
                reminderEditText.setText("00:00");
                reminderLinearLayout.setVisibility(View.VISIBLE);
            } else {
                reminderSwitch.setChecked(false);
                reminderEditText.setText("");
                reminderLinearLayout.setVisibility(View.GONE);
            }
        });
        // on click listener for reminder
        reminderEditText.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.TimePickerTheme,
                    (TimePickerDialog.OnTimeSetListener) (view, hourOfDay, minuteOfHour) -> {
                        // set time in textView
                        if(minuteOfHour<10) reminderEditText.setText(hourOfDay + ":0" + minuteOfHour);
                        else reminderEditText.setText(hourOfDay + ":" + minuteOfHour);
                        // save time
                        hour = hourOfDay;
                        minute = minuteOfHour;
                    }, hour, minute, true);
            timePickerDialog.show();
        });
        // set button onClick
        createButton.setOnClickListener(view -> {
            HabitModel habit = new HabitModel(categoryNames[position], nameEditText.getText().toString(), descriptionEditText.getText().toString(), "yesno", "", "daily", 1, startDateEditText.getText().toString(), endDateEditText.getText().toString(), Integer.parseInt(priorityEditText.getText().toString()), reminderSwitch.isActivated(), hour, minute);
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