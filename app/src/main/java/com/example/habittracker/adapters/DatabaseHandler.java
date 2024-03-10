package com.example.habittracker.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    private static final String RATINGS_TABLE_NAME = "ratings";
    // user column
    private static final String EMAIL_COL = "email";
    // starts column
    private static final String STARS = "stars";
    // rating column
    private static final String OPINION_COL = "opinion";
    // date column
    private static final String DATE_COL = "date";

    // constructor for database handler
    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // creating the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create customers table
        String queryRatings = "CREATE TABLE " + RATINGS_TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EMAIL_COL + " TEXT,"
                + STARS + " NUMBER,"
                + OPINION_COL + " TEXT,"
                + DATE_COL + " TEXT)";
        // execute sql query
        db.execSQL(queryRatings);
    }

    // ############################################### RATINGS ###########################################################
    // add new rating
    public void addRating(String email, float stars, String opinion) {
        // writing data in the database
        SQLiteDatabase db = this.getWritableDatabase();
        // variable for content values
        ContentValues values = new ContentValues();

        // passing all values
        values.put(EMAIL_COL, email);
        values.put(STARS, stars);
        // date
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format1.format(cal.getTime());
        values.put(DATE_COL, formatted);

        // passing content values
        db.insert(RATINGS_TABLE_NAME, null, values);
        // closing the database
        db.close();
    }

    // update rating
    public void updateRating(String email, float stars, String opinion) {
        // get database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // pass values
        values.put(EMAIL_COL, email);
        values.put(STARS, stars);
        // date
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format1.format(cal.getTime());
        values.put(DATE_COL, formatted);

        // update and close database
        db.update(RATINGS_TABLE_NAME, values, "email=?", new String[]{email});
        db.close();
    }

    // read a rating
    public RatingModel readRatingByUser(String email) {
        // create database
        SQLiteDatabase db = this.getReadableDatabase();
        // create cursor
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + RATINGS_TABLE_NAME + " WHERE name=?", new String[]{email});
        // create order
        RatingModel rating = null;

        // move cursor to first position
        if (cursorCourses.moveToFirst()) {
            rating = new RatingModel(cursorCourses.getString(1), cursorCourses.getFloat(2), cursorCourses.getString(3));
        }
        // closing cursor
        cursorCourses.close();
        return rating;
    }

    // delete rating
    public void deleteRating(String email) {
        // get database
        SQLiteDatabase db = this.getWritableDatabase();

        // delete customer and close database
        db.delete(RATINGS_TABLE_NAME, "email=?", new String[]{email});
        db.close();
    }

    // ############################################### ONUPGRADE ###########################################################
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // check if the table exists
        db.execSQL("DROP TABLE IF EXISTS " + RATINGS_TABLE_NAME);
        onCreate(db);
    }
}
