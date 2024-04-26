package com.example.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.habittracker.R;
import com.example.habittracker.adapters.CategoriesAdapter;
import com.example.habittracker.adapters.DatabaseHandler;
import com.example.habittracker.dialogs.CategoryColorsDialog;
import com.example.habittracker.dialogs.CategoryIconsDialog;
import com.example.habittracker.fragments.NewCategoryFragment;
import com.example.habittracker.models.CategoryModel;
import com.example.habittracker.models.GoalModel;
import com.example.habittracker.models.HabitModel;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class CategoriesActivity extends AppCompatActivity implements NewCategoryFragment.ItemClickListener {

    public View hamburgerMenu;
    private ImageView categoriesIW;
    private TextView categoriesTW;
    private ImageButton createButton;
    private DatabaseHandler dbHandler;
    private CategoriesAdapter categoriesAdapter;
    private RecyclerView categoriesRecyclerView;
    private ArrayList<CategoryModel> categoriesArrayList;
    private NewCategoryFragment newCategoryFragment;
    // spinner
    Spinner sortSpinner;
    // array adapter
    ArrayAdapter<String> adapter;
    // sort's position
    int position;
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
        setContentView(R.layout.activity_categories);

        // hamburger menu
        hamburgerMenu = findViewById(R.id.hamburgerMenu);
        categoriesIW = findViewById(R.id.categoryImageView);
        categoriesIW.setColorFilter(ContextCompat.getColor(this, R.color.light_gray));
        categoriesTW = findViewById(R.id.categoriesTextView);
        categoriesTW.setTextColor(ContextCompat.getColor(this, R.color.light_gray));
        // buttons
        createButton = findViewById(R.id.createButton);
        // database handler
        dbHandler = new DatabaseHandler(CategoriesActivity.this);
        // get categories
        categoriesArrayList = setEntries(dbHandler.readAllCategories());
        categoriesArrayList = sortEntriesDecreasing(categoriesArrayList);
        // passing list to adapter
        categoriesAdapter = new CategoriesAdapter(categoriesArrayList, CategoriesActivity.this);
        categoriesRecyclerView = findViewById(R.id.categoryRecyclerView);
        // setting layout manager for recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CategoriesActivity.this, RecyclerView.VERTICAL, false);
        categoriesRecyclerView.setLayoutManager(linearLayoutManager);
        // setting adapter to recycler view
        categoriesRecyclerView.setAdapter(categoriesAdapter);
        // create button onClickListener
        createButton.setOnClickListener(view -> {
            // open new category sheet
            newCategoryFragment = NewCategoryFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString("mode", "new");
            newCategoryFragment.setArguments(bundle);
            newCategoryFragment.show(getSupportFragmentManager(), NewCategoryFragment.TAG);
        });
        // get the spinner from the xml
        sortSpinner = findViewById(R.id.sortSpinner);
        String[] sortOptions = {getResources().getString(R.string.sort_entries_D), getResources().getString(R.string.sort_entries_I), getResources().getString(R.string.sort_AZ), getResources().getString(R.string.sort_ZA)};
        // create an adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sortOptions);
        // default position is 0
        position = 0;
        // set the spinner adapter
        sortSpinner.setAdapter(adapter);
        sortSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position2, long id) {
                        position = sortSpinner.getSelectedItemPosition();
                        switch(position) {
                            case 0:
                                categoriesArrayList = sortEntriesDecreasing(categoriesArrayList);
                                break;
                            case 1:
                                categoriesArrayList = sortEntriesIncreasing(categoriesArrayList);
                                break;
                            case 2:
                                categoriesArrayList = sortNameAZ(categoriesArrayList);
                                break;
                            case 3:
                                categoriesArrayList = sortNameZA(categoriesArrayList);
                                break;
                        }
                        categoriesAdapter.notifyDataSetChanged();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        position = 0;
                    }
                });
    }

    ArrayList<CategoryModel> sortNameAZ(ArrayList<CategoryModel> categoriesArrayList){
        Collections.sort(categoriesArrayList, Comparator.comparing(CategoryModel::getName));
        return categoriesArrayList;
    }

    ArrayList<CategoryModel> sortNameZA(ArrayList<CategoryModel> categoriesArrayList){
        Collections.sort(categoriesArrayList, (first, second) -> second.getName().compareTo(first.getName()));
        return categoriesArrayList;
    }

    ArrayList<CategoryModel> sortEntriesIncreasing(ArrayList<CategoryModel> categoriesArrayList){
        Collections.sort(categoriesArrayList, Comparator.comparing(CategoryModel::getEntries));
        return categoriesArrayList;
    }

    ArrayList<CategoryModel> sortEntriesDecreasing(ArrayList<CategoryModel> categoriesArrayList){
        Collections.reverse(sortEntriesIncreasing(categoriesArrayList));
        return categoriesArrayList;
    }

    ArrayList<CategoryModel> setEntries(ArrayList<CategoryModel> categoriesArrayList) {
        for(int i=0; i<categoriesArrayList.size(); i++) {
            categoriesArrayList.get(i).setEntries(dbHandler.readAllHabitsInCategory(categoriesArrayList.get(i).getName()).size());
        }
        return categoriesArrayList;
    }

    // ############################### ONCLICKS ######################################
    // show the new category fragment
    public void showBottomSheet(View view, String name) {
        newCategoryFragment = NewCategoryFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("mode", "edit");
        bundle.putString("origName", name);
        newCategoryFragment.setArguments(bundle);
        newCategoryFragment.show(getSupportFragmentManager(), NewCategoryFragment.TAG);
    }

    // choose icon
    @Override
    public void onChooseIcon() {
        CategoryIconsDialog iconsDialog = new CategoryIconsDialog(this);
        iconsDialog.show();
    }

    public void setIcon(String icon){
        newCategoryFragment.setIcon(icon);
    }

    // choose color
    @Override
    public void onChooseColor() {
        CategoryColorsDialog colorsDialog = new CategoryColorsDialog(this);
        colorsDialog.show();
    }

    public void setColor(String color){
        newCategoryFragment.setColor(color);
    }

    // create the new category
    @Override
    public void onCreateCategory(CategoryModel cat) {
        CategoryModel prev = dbHandler.readCategoryByName(cat.getName());
        if(prev == null) {
            dbHandler.addCategory(cat);
            categoriesArrayList.add(cat);
            categoriesAdapter.notifyDataSetChanged();
        }
    }

    // create the new category
    @Override
    public void onEditCategory(CategoryModel cat, String origName) {
        for(int i=0; i<categoriesArrayList.size(); i++) {
            if(categoriesArrayList.get(i).getName().equals(origName)) {
                categoriesArrayList.set(i, cat);
                break;
            }
        }
        dbHandler.updateCategory(cat, origName);
        categoriesAdapter.notifyDataSetChanged();
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