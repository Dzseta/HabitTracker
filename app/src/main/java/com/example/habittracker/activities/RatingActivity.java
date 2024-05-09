package com.example.habittracker.activities;

import static java.lang.Float.parseFloat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habittracker.R;
import com.example.habittracker.models.RatingModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Console;

import es.dmoral.toasty.Toasty;

public class RatingActivity extends AppCompatActivity {

    public View hamburgerMenu;
    private ImageView ratingIW;
    private TextView ratingTW;
    Button sendButton;
    RatingBar ratingbar;
    EditText opinionText;
    RatingModel rating;
    FirebaseFirestore db;
    DocumentReference docref;
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
        setContentView(R.layout.activity_rating);

        db = FirebaseFirestore.getInstance();
        // hamburger menu
        hamburgerMenu = findViewById(R.id.hamburgerMenu);
        ratingIW= findViewById(R.id.ratingImageView);
        ratingIW.setColorFilter(ContextCompat.getColor(this, R.color.light_gray));
        ratingTW = findViewById(R.id.ratingTextView);
        ratingTW.setTextColor(ContextCompat.getColor(this, R.color.light_gray));

        // rating bar
        ratingbar = (RatingBar)findViewById(R.id.ratingBar);
        // edittext
        opinionText = findViewById(R.id.opinionMultiLine);
        // rating
        rating = null;
        // send button
        sendButton = findViewById(R.id.sendRatingButton);

        // get previous rating
        db.collection("ratings")
                .whereEqualTo("uid", FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            rating = new RatingModel(document.getData().get("uid").toString(), parseFloat(document.getData().get("stars").toString()), document.getData().get("opinion").toString());
                            docref = document.getReference();
                            ratingbar.setRating(rating.getStars());
                            opinionText.setText(rating.getOpinion());
                            sendButton.setClickable(true);
                        }
                    } else {
                        Toast.makeText(RatingActivity.this, "Nem sikerült elérni a szervert", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void sendRating(View view) {
        // Create a new rating
        rating = new RatingModel(FirebaseAuth.getInstance().getCurrentUser().getUid(), ratingbar.getRating(), opinionText.getText().toString());

        if (docref == null) {
            // Add a new document with a generated ID
            db.collection("ratings")
                    .add(rating)
                    .addOnSuccessListener(documentReference -> {
                        Toasty.success(RatingActivity.this, getResources().getString(R.string.toast_successful_rating), Toast.LENGTH_SHORT, true).show();
                        Intent i = new Intent();
                        i.setClass(RatingActivity.this, TodayActivity.class);
                        startActivity(i);
                    })
                    .addOnFailureListener(e -> Toasty.error(RatingActivity.this, getResources().getString(R.string.toast_error), Toast.LENGTH_SHORT, true).show());
        } else {
            // Refresh rating
            docref
                    .update("stars", rating.getStars(), "opinion", rating.getOpinion())
                    .addOnSuccessListener(aVoid -> {
                        Toasty.success(RatingActivity.this, getResources().getString(R.string.toast_successful_rating), Toast.LENGTH_SHORT, true).show();
                        Intent i = new Intent();
                        i.setClass(RatingActivity.this, TodayActivity.class);
                        startActivity(i);
                    })
                    .addOnFailureListener(e -> Toasty.error(RatingActivity.this, getResources().getString(R.string.toast_error), Toast.LENGTH_SHORT, true).show());
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