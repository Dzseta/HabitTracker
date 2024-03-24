package com.example.habittracker.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.habittracker.models.CategoryModel;
import com.example.habittracker.models.RatingModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DatabaseHandler extends SQLiteOpenHelper {

    // creating variables
    // database name
    private static final String DB_NAME = "habitsdb";
    // database version
    private static final int DB_VERSION = 1;
    // id column
    private static final String ID_COL = "id";
    // ratings table
    // table name
    private static final String CATEGORY_TABLE_NAME = "ratings";
    // user column
    private static final String ICON_COL = "icon";
    // starts column
    private static final String NAME_COL = "name";
    // rating column
    private static final String COLOR_COL = "color";

    // constructor for database handler
    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // creating the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create customers table
        String queryRatings = "CREATE TABLE " + CATEGORY_TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ICON_COL + " TEXT,"
                + NAME_COL + " TEXT UNIQUE,"
                + COLOR_COL + " TEXT)";
        // execute sql query
        db.execSQL(queryRatings);
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
        // closing the database
        db.close();
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

        // update and close database
        db.update(CATEGORY_TABLE_NAME, values, "name=?", new String[]{String.valueOf(origName)});
        db.close();
    }

    // read a category
    public CategoryModel readCategoryByName(String name) {
        // create database
        SQLiteDatabase db = this.getReadableDatabase();
        // create cursor
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + CATEGORY_TABLE_NAME + " WHERE name=?", new String[]{name});
        // create order
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

    // delete rating
    public void deleteCategory(String name) {
        // get database
        SQLiteDatabase db = this.getWritableDatabase();

        // delete customer and close database
        db.delete(CATEGORY_TABLE_NAME, "name=?", new String[]{name});
        db.close();
    }

    // ############################################### ONUPGRADE ###########################################################
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // check if the table exists
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE_NAME);
        onCreate(db);
    }
}
