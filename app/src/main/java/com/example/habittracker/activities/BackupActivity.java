package com.example.habittracker.activities;

import static java.lang.Float.parseFloat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habittracker.R;
import com.example.habittracker.adapters.DatabaseHandler;
import com.example.habittracker.models.BackupModel;
import com.example.habittracker.models.CategoryModel;
import com.example.habittracker.models.DayentryModel;
import com.example.habittracker.models.EntryModel;
import com.example.habittracker.models.GoalModel;
import com.example.habittracker.models.HabitModel;
import com.example.habittracker.models.RatingModel;
import com.example.habittracker.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class BackupActivity extends AppCompatActivity {

    public View hamburgerMenu;
    private ImageView backupIW;
    private TextView backupTW;
    // dbHandler
    private DatabaseHandler dbHandler;
    // buttons
    Button backupButton;
    Button exportButton;
    Button importButton;
    // linearLayout
    LinearLayout premiumFunctions;
    // sharedprefs
    private static String PREF_NAME = "optionsSharedPrefs";
    SharedPreferences prefs;
    // database
    FirebaseFirestore db;
    DocumentReference docref;
    UserModel userAccount;
    // backup
    BackupModel backup;

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
        setContentView(R.layout.activity_backup);

        // db
        db = FirebaseFirestore.getInstance();
        loadBackup();
        // linear layout
        premiumFunctions = findViewById(R.id.premiumFunctionsLinearLayout);
        // get user status
        db.collection("users")
                .whereEqualTo("uid", FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            userAccount = new UserModel(document.getData().get("uid").toString(), document.getData().get("premium").toString());
                            System.out.println(document.getData().get("premium").toString());
                            docref = document.getReference();
                            if(userAccount.isPremium()) {
                                premiumFunctions.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        Toasty.error(BackupActivity.this, getResources().getString(R.string.toast_error), Toast.LENGTH_SHORT, true).show();
                    }
                });
        // hamburger menu
        hamburgerMenu = findViewById(R.id.hamburgerMenu);
        backupIW= findViewById(R.id.backupImageView);
        backupIW.setColorFilter(ContextCompat.getColor(this, R.color.light_gray));
        backupTW = findViewById(R.id.backupTextView);
        backupTW.setTextColor(ContextCompat.getColor(this, R.color.light_gray));
        // backup button
        backupButton = findViewById(R.id.backupButton);
        backupButton.setOnClickListener(view -> {
            // new backup model
            backup = new BackupModel(FirebaseAuth.getInstance().getUid(), serialiseCategories(), serialiseHabits(), serialiseGoals(), serialiseDayentries(), serialiseEntries());
            // if backup doesn't exist yet
            if (docref == null) {
                // Add a new document with a generated ID
                db.collection("backups")
                        .add(backup)
                        .addOnSuccessListener(documentReference -> {
                            Toasty.success(BackupActivity.this, getResources().getString(R.string.toast_successful_backup), Toast.LENGTH_SHORT, true).show();
                        })
                        .addOnFailureListener(e -> Toasty.error(BackupActivity.this, getResources().getString(R.string.toast_error), Toast.LENGTH_SHORT, true).show());
            } else {
                // if a backup already exists refresh the data
                docref
                        .update("categories", backup.getCategories(), "habits", backup.getHabits(), "goals", backup.getGoals(), "dayentries", backup.getDayentries(), "entries", backup.getEntries())
                        .addOnSuccessListener(aVoid -> {
                            Toasty.success(BackupActivity.this, getResources().getString(R.string.toast_successful_backup), Toast.LENGTH_SHORT, true).show();
                        })
                        .addOnFailureListener(e -> Toasty.error(BackupActivity.this, getResources().getString(R.string.toast_error), Toast.LENGTH_SHORT, true).show());
            }
        });
        // import data from Firestore
        importButton = findViewById(R.id.importButton);
        importButton.setOnClickListener(view -> {
            // load the data
            loadBackup();
            // create the categories, habits, goals, dayentries and entries
            String[] categories = backup.getCategories().split(" --- ");
            if(!categories[0].equals("")) {
                for(int i=0; i<categories.length; i++) {
                    String[] cat = categories[i].split(" ;; ", -1);
                    CategoryModel categoryModel = new CategoryModel(cat[0], cat[1], cat[2]);
                    if(dbHandler.readCategoryByName(categoryModel.getName()) == null) dbHandler.addCategory(categoryModel);
                }
            }
            String[] habits = backup.getHabits().split(" --- ");
            if(!habits[0].equals("")) {
                for (int i = 0; i < habits.length; i++) {
                    String[] habit = habits[i].split(" ;; ", -1);
                    HabitModel habitModel = new HabitModel(habit[0], habit[1], habit[2], habit[3], habit[4], habit[5], habit[6], habit[7], Integer.parseInt(habit[8]), habit[9], Integer.parseInt(habit[10]), Integer.parseInt(habit[11]));
                    if(dbHandler.readHabitByName(habitModel.getName()) == null) dbHandler.addHabit(habitModel);
                }
            }
            String[] goals = backup.getGoals().split(" --- ");
            if(!goals[0].equals("")) {
                for (int i = 0; i < goals.length; i++) {
                    String[] goal = goals[i].split(" ;; ", -1);
                    GoalModel goalModel = new GoalModel(goal[0], Integer.parseInt(goal[1]), goal[2]);
                    if(dbHandler.readGoalByHabit(goalModel.getHabit()) == null) dbHandler.addGoal(goalModel);
                }
            }
            String[] dayentries = backup.getDayentries().split(" --- ");
            if(!dayentries[0].equals("")) {
                for (int i = 0; i < dayentries.length; i++) {
                    String[] dayentry = dayentries[i].split(" ;; ", -1);
                    DayentryModel dayentryModel = new DayentryModel(dayentry[0], Integer.parseInt(dayentry[1]), dayentry[2]);
                    if(dbHandler.readDayentryByDate(dayentryModel.getDate()) == null) dbHandler.addDayentry(dayentryModel);
                }
            }
            String[] entries = backup.getEntries().split(" --- ");
            if(!entries[0].equals("")) {
                for (int i = 0; i < entries.length; i++) {
                    String[] entry = entries[i].split(" ;; ", -1);
                    EntryModel entryModel = new EntryModel(entry[0], entry[1], entry[2], Integer.parseInt(entry[3]));
                    if(dbHandler.readEntryByHabitAndDate(entryModel.getHabit(), entryModel.getDate()) == null) dbHandler.addEntry(entryModel);
                }
            }
            Toasty.success(BackupActivity.this, getResources().getString(R.string.toast_pay_success), Toast.LENGTH_SHORT, true).show();
        });
        // export data to a file
        exportButton = findViewById(R.id.exportButton);
        exportButton.setOnClickListener(view -> {
            try {
                // create the new file
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "habittracker_data.txt");
                // write the data into the file
                FileWriter writer = new FileWriter(file);
                writer.append("CATEGORIES\n");
                writer.append(serialiseCategories());
                writer.append("\n\n");
                writer.append("HABITS\n");
                writer.append(serialiseHabits());
                writer.append("\n\n");
                writer.append("GOALS\n");
                writer.append(serialiseGoals());
                writer.append("\n\n");
                writer.append("DAYENTRIES\n");
                writer.append(serialiseDayentries());
                writer.append("\n\n");
                writer.append("ENTRIES\n");
                writer.append(serialiseEntries());
                writer.flush();
                writer.close();
                Toasty.success(BackupActivity.this, getResources().getString(R.string.toast_file_saved), Toast.LENGTH_SHORT, true).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // dbHandler
        dbHandler = new DatabaseHandler(BackupActivity.this);
    }

    // load the data into a BackupModel
    private void loadBackup() {
        db.collection("backups")
                .whereEqualTo("uid", FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            backup = new BackupModel(document.getData().get("uid").toString(), document.getData().get("categories").toString(), document.getData().get("habits").toString(), document.getData().get("goals").toString(), document.getData().get("dayentries").toString(), document.getData().get("entries").toString());
                            docref = document.getReference();
                        }
                    } else {
                        Toasty.error(BackupActivity.this, getResources().getString(R.string.toast_error), Toast.LENGTH_SHORT, true).show();
                    }
                });
    }

    // ################################################### SERIALISE ######################################################
    // convert the datamodels into Strings
    private String serialiseCategories() {
        ArrayList<CategoryModel> categories = dbHandler.readAllCategories();
        String save = "";
        for(int i=0; i<categories.size(); i++){
            save += categories.get(i).serialise();
            save += " --- ";
        }
        return save;
    }
    private String serialiseHabits() {
        ArrayList<HabitModel> habits = dbHandler.readAllHabits();
        String save = "";
        for(int i=0; i<habits.size(); i++){
            save += habits.get(i).serialise();
            save += " --- ";
        }
        return save;
    }
    private String serialiseGoals() {
        ArrayList<GoalModel> goals = dbHandler.readAllGoals();
        String save = "";
        for(int i=0; i<goals.size(); i++){
            save += goals.get(i).serialise();
            save += " --- ";
        }
        return save;
    }
    private String serialiseDayentries() {
        ArrayList<DayentryModel> dayentries = dbHandler.readAllDayentries();
        String save = "";
        for(int i=0; i<dayentries.size(); i++){
            save += dayentries.get(i).serialise();
            save += " --- ";
        }
        return save;
    }
    private String serialiseEntries() {
        ArrayList<EntryModel> entries = dbHandler.readAllEntries();
        String save = "";
        for(int i=0; i<entries.size(); i++){
            save += entries.get(i).serialise();
            save += " --- ";
        }
        return save;
    }

    // ################################################### ONCLICKS ######################################################
    // open and close the hamburger menu
    public void openCloseHamburgerMenu(View view) {
        if (hamburgerMenu.getVisibility() == View.VISIBLE) {
            hamburgerMenu.setVisibility(View.INVISIBLE);
        } else {
            hamburgerMenu.setVisibility(View.VISIBLE);
        }
    }

    // open new activity
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
            case R.id.helpButton:
                i.setClass(this, HelpActivity.class);
                startActivity(i);
                break;
            default: break;
        }
    }
}