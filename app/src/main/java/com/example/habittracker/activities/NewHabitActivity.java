package com.example.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.habittracker.R;
import com.example.habittracker.adapters.DatabaseHandler;
import com.example.habittracker.models.HabitModel;

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
    EditText startDateEditText;
    EditText endDateEditText;
    EditText priorityEditText;
    EditText reminderEditText;
    // radio
    RadioGroup typeRadioGroup;
    RadioButton yesNoRadioButton;
    RadioButton numberRadioButton;
    RadioButton timeRadioButton;
    // switch
    Switch reminderSwitch;
    // button
    Button createButton;
    // extras
    String mode;
    String habitName;
    HabitModel origHabit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_habit);

        // database handler
        dbHandler = new DatabaseHandler(this);
        // spinner
        categorySpinner = findViewById(R.id.categorySpinner);
        // imageView
        iconImageView = findViewById(R.id.iconImageView);
        // edittext
        nameEditText = findViewById(R.id.nameEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        startDateEditText = findViewById(R.id.startDateEditText);
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
        // button
        createButton = findViewById(R.id.createButton);
        // set button onClick
        createButton.setOnClickListener(view -> {
            HabitModel habit = new HabitModel("TODO", nameEditText.getText().toString(), descriptionEditText.getText().toString(), "yesno", "", "daily", 1, "2024-04-17", "2099-01-01", Integer.parseInt(priorityEditText.getText().toString()), reminderSwitch.isActivated(), 22, 0);
            HabitModel prev = dbHandler.readHabitByName(habit.getName());
            if(prev == null) {
                dbHandler.addHabit(habit);
                Intent i = new Intent();
                i.setClass(this, HabitsActivity.class);
                startActivity(i);
            }
        });
        mode = getIntent().getStringExtra("mode");
        if(mode.equals("edit")) {
            habitName = getIntent().getStringExtra("habit");
            origHabit = dbHandler.readHabitByName(habitName);
            // TODO
        }
    }
}