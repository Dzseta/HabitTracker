package com.example.habittracker.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.habittracker.R;
import com.example.habittracker.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    // EditTexts
    EditText editTextTextEmailAddress;
    EditText editTextTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        // EditTexts
        editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword = findViewById(R.id.editTextTextPassword);
    }

    // register
    public void register(View view) {
        if(editTextTextPassword.getText().toString().length() < 6) {
            Toasty.warning(RegisterActivity.this, getResources().getString(R.string.toast_password_length), Toast.LENGTH_SHORT, true).show();
            return;
        } else if (!editTextTextPassword.getText().toString().matches(".*\\d.*")) {
            Toasty.warning(RegisterActivity.this, getResources().getString(R.string.toast_password_number), Toast.LENGTH_SHORT, true).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(editTextTextEmailAddress.getText().toString(), editTextTextPassword.getText().toString())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // sign in success
                        FirebaseUser user = mAuth.getCurrentUser();
                        // add profile
                        UserModel userModel = new UserModel(user.getUid(), false);
                        db.collection("users")
                                .add(userModel)
                                .addOnSuccessListener(documentReference -> {
                                    Toasty.success(RegisterActivity.this, getResources().getString(R.string.toast_successful_register), Toast.LENGTH_SHORT, true).show();
                                    // go to app
                                    Intent i = new Intent();
                                    i.setClass(getApplicationContext(), TodayActivity.class);
                                    startActivity(i);
                                })
                                .addOnFailureListener(e -> Toasty.error(RegisterActivity.this, getResources().getString(R.string.toast_auth_failed), Toast.LENGTH_SHORT, true).show());
                    } else {
                        // If sign in fails, display a message to the user.
                        Toasty.error(RegisterActivity.this, getResources().getString(R.string.toast_auth_failed), Toast.LENGTH_SHORT, true).show();
                    }
                });
    }

}