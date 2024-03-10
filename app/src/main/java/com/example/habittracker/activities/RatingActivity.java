package com.example.habittracker.activities;

import static java.lang.Float.parseFloat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
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

public class RatingActivity extends AppCompatActivity {

    public View hamburgerMenu;
    RatingBar ratingbar;
    EditText opinionText;
    RatingModel rating;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        // hamburger menu
        hamburgerMenu = findViewById(R.id.hamburgerMenu);
        // rating bar
        ratingbar = (RatingBar)findViewById(R.id.ratingBar);
        // edittext
        opinionText = findViewById(R.id.editTextTextMultiLine);
        // rating
        rating = null;

        // get previous rating
        db.collection("ratings")
                .whereEqualTo("email", FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                rating = new RatingModel(document.getData().get("email").toString(), parseFloat(document.getData().get("stars").toString()), document.getData().get("opinion").toString());
                                docref = document.getReference();
                                ratingbar.setRating(rating.getStars());
                                opinionText.setText(rating.getOpinion());
                                System.out.println(task.getResult().size());
                            }
                        } else {
                            Toast.makeText(RatingActivity.this, "Nem sikerült elérni a szervert", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void sendRating(View view) {
        // Create a new rating
        rating = new RatingModel(FirebaseAuth.getInstance().getCurrentUser().getEmail(), ratingbar.getRating(), opinionText.getText().toString());

        if (docref == null) {
            // Add a new document with a generated ID
            db.collection("ratings")
                    .add(rating)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Intent i = new Intent();
                            i.setClass(RatingActivity.this, TodayActivity.class);
                            startActivity(i);
                            Toast.makeText(RatingActivity.this, "Siker", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RatingActivity.this, "Nem sikerült", Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            // Refresh rating
            docref
                    .update("stars", rating.getStars(), "opinion", rating.getOpinion())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent i = new Intent();
                            i.setClass(RatingActivity.this, TodayActivity.class);
                            startActivity(i);
                            Toast.makeText(RatingActivity.this, "Siker", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RatingActivity.this, "Nem sikerült", Toast.LENGTH_LONG).show();
                        }
                    });
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