package com.example.habittracker.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.habittracker.models.CategoryModel;
import com.example.habittracker.models.DayentryModel;
import com.example.habittracker.models.EntryModel;
import com.example.habittracker.models.GoalModel;
import com.example.habittracker.models.HabitModel;
import com.example.habittracker.models.RatingModel;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class DatabaseHandler extends SQLiteOpenHelper {

    // creating variables
    // database name
    private static final String DB_NAME = "habitsdb";
    // database version
    private static final int DB_VERSION = 22;
    // id column
    private static final String ID_COL = "id";
    // name column
    private static final String NAME_COL = "name";
    // category table
    // table name
    private static final String CATEGORY_TABLE_NAME = "categories";
    // icon column
    private static final String ICON_COL = "icon";
    // color column
    private static final String COLOR_COL = "color";
    // habits table
    // table name
    private static final String HABIT_TABLE_NAME = "habits";
    // category column
    private static final String CAT_COL = "category";
    // description column
    private static final String DESCRIPTION_COL = "description";
    // type column
    private static final String TYPE_COL = "type";
    // type data column
    private static final String TYPEDATA_COL = "typedata";
    // repeat type column
    private static final String REPEATTYPE_COL = "repeattype";
    // start date column
    private static final String STARTDATE_COL = "startdate";
    // end date column
    private static final String ENDDATE_COL = "enddate";
    // priority column
    private static final String PRIORITY_COL = "priority";
    // reminder column
    private static final String REMINDER_COL = "reminder";
    // reminder hour column
    private static final String REMINDERHOUR_COL = "reminderhour";
    // reminder minute column
    private static final String REMINDERMINUTE_COL = "reminderminute";
    // goal table
    // table name
    private static final String GOAL_TABLE_NAME = "goals";
    // needed successes column
    private static final String NEEDED_COL = "needed";
    // successes column
    private static final String FINISHED_COL = "finished";
    // entry table
    // table name
    private static final String ENTRY_TABLE_NAME = "entries";
    // date column
    private static final String DATE_COL = "date";
    // data column
    private static final String DATA_COL = "data";
    // success column
    private static final String SUCCESS_COL = "success";
    // comment column
    private static final String COMMENT_COL = "comment";
    // dayentry table
    // table name
    private static final String DAYENTRY_TABLE_NAME = "dayentries";
    // data column
    private static final String MOOD_COL = "mood";

    // constructor for database handler
    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // creating the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create categories table
        String queryCategories = "CREATE TABLE " + CATEGORY_TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ICON_COL + " TEXT,"
                + NAME_COL + " TEXT UNIQUE,"
                + COLOR_COL + " TEXT)";
        // create habits table
        String queryHabits = "CREATE TABLE " + HABIT_TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CAT_COL + " TEXT,"
                + NAME_COL + " TEXT UNIQUE,"
                + DESCRIPTION_COL + " TEXT,"
                + TYPE_COL + " TEXT,"
                + TYPEDATA_COL + " TEXT,"
                + REPEATTYPE_COL + " TEXT,"
                + STARTDATE_COL + " TEXT,"
                + ENDDATE_COL + " TEXT,"
                + PRIORITY_COL + " INTEGER,"
                + REMINDER_COL + " BOOLEAN,"
                + REMINDERHOUR_COL + " INTEGER,"
                + REMINDERMINUTE_COL + " INTEGER)";
        // create goals table
        String queryGoals = "CREATE TABLE " + GOAL_TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT UNIQUE,"
                + NEEDED_COL + " TEXT,"
                + FINISHED_COL + " BOOLEAN)";
        // create entries table
        String queryEntries = "CREATE TABLE " + ENTRY_TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + DATE_COL + " TEXT,"
                + DATA_COL + " TEXT,"
                + SUCCESS_COL + " INTEGER,"
                + COMMENT_COL + " TEXT)";
        // create dayentries table
        String queryDayentries = "CREATE TABLE " + DAYENTRY_TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DATE_COL + " TEXT,"
                + MOOD_COL + " INTEGER,"
                + COMMENT_COL + " TEXT)";
        // execute sql query
        db.execSQL(queryCategories);
        db.execSQL(queryHabits);
        db.execSQL(queryGoals);
        db.execSQL(queryEntries);
        db.execSQL(queryDayentries);
    }

    // ############################################### CATEGORIES ###########################################################
    // add new category
    public void addCategory(CategoryModel cat) {
        // writing data in the database
        SQLiteDatabase db = this.getWritableDatabase();
        // variable for content values
        ContentValues values = new ContentValues();

        // passing all values
        values.put(ICON_COL, cat.getIcon());
        values.put(NAME_COL, cat.getName());
        values.put(COLOR_COL, cat.getColor());

        // passing content values
        db.insert(CATEGORY_TABLE_NAME, null, values);
    }

    // update category
    public void updateCategory(CategoryModel cat, String origName) {
        // get database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // passing all values
        values.put(ICON_COL, cat.getIcon());
        values.put(NAME_COL, cat.getName());
        values.put(COLOR_COL, cat.getColor());

        // update database
        db.update(CATEGORY_TABLE_NAME, values, "name=?", new String[]{String.valueOf(origName)});

        // update habits
        if(!cat.getName().equals(origName)) {
            ArrayList<HabitModel> habits = readAllHabitsInCategory(origName);
            for(int i=0; i<habits.size(); i++) {
                habits.get(i).setCategoryName(cat.getName());
                updateHabit(habits.get(i), habits.get(i).getName());
            }
        }
    }

    // read a category
    public CategoryModel readCategoryByName(String name) {
        // create database
        SQLiteDatabase db = this.getReadableDatabase();
        // create cursor
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + CATEGORY_TABLE_NAME + " WHERE name=?", new String[]{name});
        // create category
        CategoryModel cat = null;

        // move cursor to first position
        if (cursorCourses.moveToFirst()) {
            cat = new CategoryModel(cursorCourses.getString(1), cursorCourses.getString(2), cursorCourses.getString(3));
        }
        // closing cursor
        cursorCourses.close();
        return cat;
    }

    // read all categories
    public ArrayList<CategoryModel> readAllCategories() {
        // create database
        SQLiteDatabase db = this.getReadableDatabase();
        // create cursor
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + CATEGORY_TABLE_NAME, null);
        // create array list
        ArrayList<CategoryModel> categoryModelArrayList = new ArrayList<>();

        // move cursor to first position
        if (cursorCourses.moveToFirst()) {
            do {
                // add data to the arraylist
                categoryModelArrayList.add(new CategoryModel(cursorCourses.getString(1), cursorCourses.getString(2), cursorCourses.getString(3)));
            } while (cursorCourses.moveToNext());
        }
        // closing cursor
        cursorCourses.close();
        return categoryModelArrayList;
    }

    // delete category
    public void deleteCategory(String name) {
        // get database
        SQLiteDatabase db = this.getWritableDatabase();

        // remove category from habits
        ArrayList<HabitModel> habitModelArrayList = readAllHabitsInCategory(name);
        for(int i=0; i<habitModelArrayList.size(); i++) {
            habitModelArrayList.get(i).setCategoryName("");
            updateHabit(habitModelArrayList.get(i), habitModelArrayList.get(i).getName());
        }

        // delete category
        db.delete(CATEGORY_TABLE_NAME, "name=?", new String[]{name});
    }

    // ############################################### HABITS ###########################################################
    // add new habit
    public void addHabit(HabitModel habit) {
        // writing data in the database
        SQLiteDatabase db = this.getWritableDatabase();
        // variable for content values
        ContentValues values = new ContentValues();

        // passing all values
        values.put(CAT_COL, habit.getCategoryName());
        values.put(NAME_COL, habit.getName());
        values.put(DESCRIPTION_COL, habit.getDescription());
        values.put(TYPE_COL, habit.getType());
        values.put(TYPEDATA_COL, habit.getTypeData());
        values.put(REPEATTYPE_COL, habit.getRepeatType());
        values.put(STARTDATE_COL, habit.getStartDate());
        values.put(ENDDATE_COL, habit.getEndDate());
        values.put(PRIORITY_COL, habit.getPriority());
        values.put(REMINDER_COL, habit.isReminder());
        values.put(REMINDERHOUR_COL, habit.getReminderHour());
        values.put(REMINDERMINUTE_COL, habit.getReminderMinute());

        // passing content values
        db.insert(HABIT_TABLE_NAME, null, values);
    }

    // update habit
    public void updateHabit(HabitModel habit, String origName) {
        // get database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // passing all values
        values.put(CAT_COL, habit.getCategoryName());
        values.put(NAME_COL, habit.getName());
        values.put(DESCRIPTION_COL, habit.getDescription());
        values.put(TYPE_COL, habit.getType());
        values.put(TYPEDATA_COL, habit.getTypeData());
        values.put(REPEATTYPE_COL, habit.getRepeatType());
        values.put(STARTDATE_COL, habit.getStartDate());
        values.put(ENDDATE_COL, habit.getEndDate());
        values.put(PRIORITY_COL, habit.getPriority());
        values.put(REMINDER_COL, habit.isReminder());
        values.put(REMINDERHOUR_COL, habit.getReminderHour());
        values.put(REMINDERMINUTE_COL, habit.getReminderMinute());

        // update database
        db.update(HABIT_TABLE_NAME, values, "name=?", new String[]{String.valueOf(origName)});
        // delete all previous unneeded entries
        if(!readHabitByName(origName).getStartDate().equals(habit.getStartDate())){
            deleteAllEntriesBeforeDate(origName, habit.getStartDate());
        }
        // update entries
        if(!habit.getName().equals(origName)) {
            ArrayList<EntryModel> entries = readAllEntriesByHabit(origName);
            for(int i=0; i<entries.size(); i++) {
                entries.get(i).setHabit(habit.getName());
                updateEntryHabit(entries.get(i), origName);
            }
            GoalModel goal = readGoalByHabit(origName);
            goal.setHabit(habit.getName());
            updateGoalHabit(goal, origName);
        }
    }

    // read a habit
    public HabitModel readHabitByName(String name) {
        // create database
        SQLiteDatabase db = this.getReadableDatabase();
        // create cursor
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + HABIT_TABLE_NAME + " WHERE name=?", new String[]{name});
        // create habit
        HabitModel habit = null;

        // move cursor to first position
        if (cursorCourses.moveToFirst()) {
            habit = new HabitModel(cursorCourses.getString(1), cursorCourses.getString(2), cursorCourses.getString(3), cursorCourses.getString(4), cursorCourses.getString(5), cursorCourses.getString(6), cursorCourses.getString(7), cursorCourses.getString(8), cursorCourses.getInt(9), cursorCourses.getInt(10) > 0, cursorCourses.getInt(11), cursorCourses.getInt(12));
        }
        // closing cursor
        cursorCourses.close();
        return habit;
    }

    // read all habits
    public ArrayList<HabitModel> readAllHabits() {
        // create database
        SQLiteDatabase db = this.getReadableDatabase();
        // create cursor
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + HABIT_TABLE_NAME, null);
        // create array list
        ArrayList<HabitModel> habitModelArrayList = new ArrayList<>();

        // move cursor to first position
        if (cursorCourses.moveToFirst()) {
            do {
                // add data to the arraylist
                habitModelArrayList.add(new HabitModel(cursorCourses.getString(1), cursorCourses.getString(2), cursorCourses.getString(3), cursorCourses.getString(4), cursorCourses.getString(5), cursorCourses.getString(6), cursorCourses.getString(7), cursorCourses.getString(8), cursorCourses.getInt(9), cursorCourses.getInt(10) > 0, cursorCourses.getInt(11), cursorCourses.getInt(12)));
            } while (cursorCourses.moveToNext());
        }
        // closing cursor
        cursorCourses.close();
        return habitModelArrayList;
    }

    // read all habits in a category
    public ArrayList<HabitModel> readAllHabitsInCategory(String categoryName) {
        // create database
        SQLiteDatabase db = this.getReadableDatabase();
        // create cursor
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + HABIT_TABLE_NAME + " WHERE category=?", new String[]{categoryName});
        // create array list
        ArrayList<HabitModel> habitModelArrayList = new ArrayList<>();

        // move cursor to first position
        if (cursorCourses.moveToFirst()) {
            do {
                // add data to the arraylist
                habitModelArrayList.add(new HabitModel(cursorCourses.getString(1), cursorCourses.getString(2), cursorCourses.getString(3), cursorCourses.getString(4), cursorCourses.getString(5), cursorCourses.getString(6), cursorCourses.getString(7), cursorCourses.getString(8), cursorCourses.getInt(9), cursorCourses.getInt(10) > 0, cursorCourses.getInt(11), cursorCourses.getInt(12)));
            } while (cursorCourses.moveToNext());
        }
        // closing cursor
        cursorCourses.close();
        return habitModelArrayList;
    }

    // delete habit
    public void deleteHabit(String name) {
        // get database
        SQLiteDatabase db = this.getWritableDatabase();

        // delete goal
        deleteGoal(name);
        // delete entries
        deleteEntriesByHabit(name);

        // delete customer
        db.delete(HABIT_TABLE_NAME, "name=?", new String[]{name});
    }

    // ############################################### GOALS ###########################################################
    // add new goal
    public void addGoal(GoalModel goal) {
        // writing data in the database
        SQLiteDatabase db = this.getWritableDatabase();
        // variable for content values
        ContentValues values = new ContentValues();

        // passing all values
        values.put(NAME_COL, goal.getHabit());
        values.put(NEEDED_COL, goal.getNeeded());
        values.put(FINISHED_COL, goal.isFinished());

        // passing content values
        db.insert(GOAL_TABLE_NAME, null, values);
    }

    // update goal
    public void updateGoal(GoalModel goal) {
        // get database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // passing all values
        values.put(NAME_COL, goal.getHabit());
        values.put(NEEDED_COL, goal.getNeeded());
        values.put(FINISHED_COL, goal.isFinished());

        // update database
        db.update(GOAL_TABLE_NAME, values, "name=?", new String[]{goal.getHabit()});
    }

    // update goal
    public void updateGoalHabit(GoalModel goal, String habit) {
        // get database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // passing all values
        values.put(NAME_COL, habit);
        values.put(NEEDED_COL, goal.getNeeded());
        values.put(FINISHED_COL, goal.isFinished());

        // update database
        db.update(GOAL_TABLE_NAME, values, "name=?", new String[]{goal.getHabit()});
    }

    // read a goal
    public GoalModel readGoalByHabit(String habit) {
        // create database
        SQLiteDatabase db = this.getReadableDatabase();
        // create cursor
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + GOAL_TABLE_NAME + " WHERE name=?", new String[]{habit});
        // create goal
        GoalModel goal = null;

        // move cursor to first position
        if (cursorCourses.moveToFirst()) {
            goal = new GoalModel(cursorCourses.getString(1), cursorCourses.getInt(2), cursorCourses.getInt(3) > 0);
        }
        // closing cursor
        cursorCourses.close();
        return goal;
    }

    // read all goals
    public ArrayList<GoalModel> readAllGoals() {
        // create database
        SQLiteDatabase db = this.getReadableDatabase();
        // create cursor
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + GOAL_TABLE_NAME, null);
        // create array list
        ArrayList<GoalModel> goalModelArrayList = new ArrayList<>();

        // move cursor to first position
        if (cursorCourses.moveToFirst()) {
            do {
                // add data to the arraylist
                goalModelArrayList.add(new GoalModel(cursorCourses.getString(1), cursorCourses.getInt(2), cursorCourses.getInt(3) > 0));
            } while (cursorCourses.moveToNext());
        }
        // closing cursor
        cursorCourses.close();
        return goalModelArrayList;
    }

    // delete goal
    public void deleteGoal(String name) {
        // get database
        SQLiteDatabase db = this.getWritableDatabase();

        // delete goal
        db.delete(GOAL_TABLE_NAME, "name=?", new String[]{name});
    }

    // ############################################### ENTRIES ###########################################################
    // add new entry
    public void addEntry(EntryModel entry) {
        if(readEntryByHabitAndDate(entry.getHabit(), entry.getDate()) != null) return;

        // writing data in the database
        SQLiteDatabase db = this.getWritableDatabase();
        // variable for content values
        ContentValues values = new ContentValues();

        // passing all values
        values.put(NAME_COL, entry.getHabit());
        values.put(DATE_COL, entry.getDate());
        values.put(DATA_COL, entry.getData());
        values.put(SUCCESS_COL, entry.getSuccess());
        values.put(COMMENT_COL, entry.getComment());

        // passing content values
        db.insert(ENTRY_TABLE_NAME, null, values);
    }

    // update entry
    public void updateEntry(EntryModel entry) {
        // get database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // passing all values
        values.put(NAME_COL, entry.getHabit());
        values.put(DATE_COL, entry.getDate());
        values.put(DATA_COL, entry.getData());
        values.put(SUCCESS_COL, entry.getSuccess());
        values.put(COMMENT_COL, entry.getComment());

        // update database
        db.update(ENTRY_TABLE_NAME, values, "name=? and date=?", new String[]{entry.getHabit(), entry.getDate()});
    }

    // update entry
    public void updateEntryHabit(EntryModel entry, String habit) {
        // get database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // passing all values
        values.put(NAME_COL, habit);
        values.put(DATE_COL, entry.getDate());
        values.put(DATA_COL, entry.getData());
        values.put(SUCCESS_COL, entry.getSuccess());
        values.put(COMMENT_COL, entry.getComment());

        // update database
        db.update(ENTRY_TABLE_NAME, values, "name=? and date=?", new String[]{habit, entry.getDate()});
    }

    // read an entry by habit and date
    public EntryModel readEntryByHabitAndDate(String habit, String date) {
        // create database
        SQLiteDatabase db = this.getReadableDatabase();
        // create cursor
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + ENTRY_TABLE_NAME + " WHERE name=? and date=?", new String[]{habit, date});
        // create entry
        EntryModel entry = null;

        // move cursor to first position
        if (cursorCourses.moveToFirst()) {
            entry = new EntryModel(cursorCourses.getString(1), cursorCourses.getString(2), cursorCourses.getString(3), cursorCourses.getInt(4), cursorCourses.getString(5));
        }
        // closing cursor
        cursorCourses.close();
        return entry;
    }

    // read all entries
    public ArrayList<EntryModel> readAllEntries() {
        // create database
        SQLiteDatabase db = this.getReadableDatabase();
        // create cursor
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + ENTRY_TABLE_NAME, null);
        // create array list
        ArrayList<EntryModel> entryModelArrayList = new ArrayList<>();

        // move cursor to first position
        if (cursorCourses.moveToFirst()) {
            do {
                // add data to the arraylist
                entryModelArrayList.add(new EntryModel(cursorCourses.getString(1), cursorCourses.getString(2), cursorCourses.getString(3), cursorCourses.getInt(4), cursorCourses.getString(5)));
            } while (cursorCourses.moveToNext());
        }
        // closing cursor
        cursorCourses.close();
        return entryModelArrayList;
    }

    // read all entries by habit
    public ArrayList<EntryModel> readAllEntriesByHabit(String name) {
        // create database
        SQLiteDatabase db = this.getReadableDatabase();
        // create cursor
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + ENTRY_TABLE_NAME + " WHERE name=?", new String[]{name});
        // create array list
        ArrayList<EntryModel> entryModelArrayList = new ArrayList<>();

        // move cursor to first position
        if (cursorCourses.moveToFirst()) {
            do {
                // add data to the arraylist
                entryModelArrayList.add(new EntryModel(cursorCourses.getString(1), cursorCourses.getString(2), cursorCourses.getString(3), cursorCourses.getInt(4), cursorCourses.getString(5)));
            } while (cursorCourses.moveToNext());
        }
        // closing cursor
        cursorCourses.close();
        return entryModelArrayList;
    }

    // read all entries by date
    public ArrayList<EntryModel> readAllEntriesByDate(String date) {
        // create database
        SQLiteDatabase db = this.getReadableDatabase();
        // create cursor
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + ENTRY_TABLE_NAME + " WHERE date=?", new String[]{date});
        // create array list
        ArrayList<EntryModel> entryModelArrayList = new ArrayList<>();

        // move cursor to first position
        if (cursorCourses.moveToFirst()) {
            do {
                // add data to the arraylist
                entryModelArrayList.add(new EntryModel(cursorCourses.getString(1), cursorCourses.getString(2), cursorCourses.getString(3), cursorCourses.getInt(4), cursorCourses.getString(5)));
            } while (cursorCourses.moveToNext());
        }
        // closing cursor
        cursorCourses.close();
        return entryModelArrayList;
    }

    // get all entries by habit in a date range
    public ArrayList<EntryModel> readAllEntriesByHabitInRange(String name, String start, String end) {
        // create database
        SQLiteDatabase db = this.getReadableDatabase();
        // create cursor
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + ENTRY_TABLE_NAME + " WHERE name=?", new String[]{name});
        // create array list
        ArrayList<EntryModel> entryModelArrayList = new ArrayList<>();
        // dates
        LocalDate startDate = LocalDate.parse(start).minusDays(1);
        LocalDate endDate = LocalDate.parse(end).plusDays(1);

        // move cursor to first position
        if (cursorCourses.moveToFirst()) {
            do {
                // add data to the arraylist
                LocalDate entryDate = LocalDate.parse(cursorCourses.getString(2));
                if(entryDate.isAfter(startDate) && entryDate.isBefore(endDate)) entryModelArrayList.add(new EntryModel(cursorCourses.getString(1), cursorCourses.getString(2), cursorCourses.getString(3), cursorCourses.getInt(4), cursorCourses.getString(5)));
            } while (cursorCourses.moveToNext());
        }
        // closing cursor
        cursorCourses.close();
        return entryModelArrayList;
    }

    // delete entries before date
    public ArrayList<EntryModel> deleteAllEntriesBeforeDate(String name, String date) {
        // create database
        SQLiteDatabase db = this.getReadableDatabase();
        // create cursor
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + ENTRY_TABLE_NAME + " WHERE name=?", new String[]{name});
        // create array list
        ArrayList<EntryModel> entryModelArrayList = new ArrayList<>();
        // dates
        LocalDate dateBefore = LocalDate.parse(date);

        // move cursor to first position
        if (cursorCourses.moveToFirst()) {
            do {
                // add data to the arraylist
                LocalDate entryDate = LocalDate.parse(cursorCourses.getString(2));
                if(entryDate.isBefore(entryDate)) db.delete(ENTRY_TABLE_NAME, "name=? and date=?", new String[]{name, cursorCourses.getString(2)});;
            } while (cursorCourses.moveToNext());
        }
        // closing cursor
        cursorCourses.close();
        return entryModelArrayList;
    }

    // delete entry
    public void deleteEntry(String name, String date) {
        // get database
        SQLiteDatabase db = this.getWritableDatabase();

        // delete entry
        db.delete(ENTRY_TABLE_NAME, "name=? and date=?", new String[]{name, date});
    }

    // delete entries
    public void deleteEntriesByHabit(String name) {
        // get database
        SQLiteDatabase db = this.getWritableDatabase();

        // delete entry
        db.delete(ENTRY_TABLE_NAME, "name=?", new String[]{name});
    }

    // ############################################### DAYENTRIES ###########################################################
    // add new dayentry
    public void addDayentry(DayentryModel dayentry) {
        if(readDayentryByDate(dayentry.getDate()) != null) return;

        // writing data in the database
        SQLiteDatabase db = this.getWritableDatabase();
        // variable for content values
        ContentValues values = new ContentValues();

        // passing all values
        values.put(DATE_COL, dayentry.getDate());
        values.put(MOOD_COL, dayentry.getMood());
        values.put(COMMENT_COL, dayentry.getComment());

        // passing content values
        db.insert(DAYENTRY_TABLE_NAME, null, values);
    }

    // update dayentry
    public void updateDayentry(DayentryModel dayentry) {
        // get database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // passing all values
        values.put(DATE_COL, dayentry.getDate());
        values.put(MOOD_COL, dayentry.getMood());
        values.put(COMMENT_COL, dayentry.getComment());

        // update database
        db.update(DAYENTRY_TABLE_NAME, values, "date=?", new String[]{dayentry.getDate()});
    }

    // read a dayentry by date
    public DayentryModel readDayentryByDate(String date) {
        // create database
        SQLiteDatabase db = this.getReadableDatabase();
        // create cursor
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + DAYENTRY_TABLE_NAME + " WHERE date=?", new String[]{date});
        // create entry
        DayentryModel dayentry = null;

        // move cursor to first position
        if (cursorCourses.moveToFirst()) {
            dayentry = new DayentryModel(cursorCourses.getString(1), cursorCourses.getInt(2), cursorCourses.getString(3));
        }
        // closing cursor
        cursorCourses.close();
        return dayentry;
    }

    // read all dayentries
    public ArrayList<DayentryModel> readAllDayentries() {
        // create database
        SQLiteDatabase db = this.getReadableDatabase();
        // create cursor
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + DAYENTRY_TABLE_NAME, null);
        // create array list
        ArrayList<DayentryModel> dayentryModelArrayList = new ArrayList<>();

        // move cursor to first position
        if (cursorCourses.moveToFirst()) {
            do {
                // add data to the arraylist
                dayentryModelArrayList.add(new DayentryModel(cursorCourses.getString(1), cursorCourses.getInt(2), cursorCourses.getString(3)));
            } while (cursorCourses.moveToNext());
        }
        // closing cursor
        cursorCourses.close();
        return dayentryModelArrayList;
    }

    // read all dayentries in range
    public ArrayList<DayentryModel> readAllDayentriesInRange(String start, String end) {
        // create database
        SQLiteDatabase db = this.getReadableDatabase();
        // create cursor
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + DAYENTRY_TABLE_NAME, null);
        // create array list
        ArrayList<DayentryModel> dayentryModelArrayList = new ArrayList<>();
        // dates
        LocalDate startDate = LocalDate.parse(start).minusDays(1);
        LocalDate endDate = LocalDate.parse(end).plusDays(1);

        // move cursor to first position
        if (cursorCourses.moveToFirst()) {
            do {
                // add data to the arraylist
                LocalDate dayentryDate = LocalDate.parse(cursorCourses.getString(1));
                if(dayentryDate.isAfter(startDate) && dayentryDate.isBefore(endDate)) dayentryModelArrayList.add(new DayentryModel(cursorCourses.getString(1), cursorCourses.getInt(2), cursorCourses.getString(3)));
             } while (cursorCourses.moveToNext());
        }
        // closing cursor
        cursorCourses.close();
        return dayentryModelArrayList;
    }

    // delete dayentry
    public void deleteDayentry(String date) {
        // get database
        SQLiteDatabase db = this.getWritableDatabase();

        // delete entry
        db.delete(DAYENTRY_TABLE_NAME, "date=?", new String[]{date});
    }

    // ############################################### ONUPGRADE ###########################################################
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // check if the table exists
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + HABIT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + GOAL_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ENTRY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DAYENTRY_TABLE_NAME);
        onCreate(db);
    }
}
