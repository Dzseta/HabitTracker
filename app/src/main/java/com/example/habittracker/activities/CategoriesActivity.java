package com.example.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.habittracker.R;
import com.example.habittracker.adapters.CategoriesAdapter;
import com.example.habittracker.adapters.DatabaseHandler;
import com.example.habittracker.fragments.NewCategoryFragment;
import com.example.habittracker.models.CategoryModel;
import com.example.habittracker.models.HabitModel;

import java.util.ArrayList;

public class CategoriesActivity extends AppCompatActivity implements NewCategoryFragment.ItemClickListener {

    public View hamburgerMenu;
    private ImageView categoriesIW;
    private TextView categoriesTW;
    private ImageButton createButton;
    private DatabaseHandler dbHandler;
    private CategoriesAdapter categoriesAdapter;
    private RecyclerView categoriesRecyclerView;
    private ArrayList<CategoryModel> categoriesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        categoriesArrayList = dbHandler.readAllCategories();
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
            NewCategoryFragment newCategoryFragment = NewCategoryFragment.newInstance();
            newCategoryFragment.show(getSupportFragmentManager(), NewCategoryFragment.TAG);
        });
    }

    // ############################### ONCLICKS ######################################
    // show the new category fragment
    public void showBottomSheet(View view) {
        NewCategoryFragment addPhotoBottomDialogFragment = NewCategoryFragment.newInstance();
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(), NewCategoryFragment.TAG);
    }

    // choose icon
    @Override
    public String onChooseIcon() {
        return "";
    }

    // choose color
    @Override
    public String onChooseColor() {
        return "";
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