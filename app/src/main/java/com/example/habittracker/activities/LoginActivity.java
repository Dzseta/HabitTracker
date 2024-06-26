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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    // EditTexts
    EditText editTextTextEmailAddress;
    EditText editTextTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // EditTexts
        editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword = findViewById(R.id.editTextTextPassword);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent i = new Intent();
            i.setClass(this, TodayActivity.class);
            startActivity(i);
        }
    }

    // login
    public void login(View view) {
        mAuth.signInWithEmailAndPassword(editTextTextEmailAddress.getText().toString(), editTextTextPassword.getText().toString())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // go to app
                        Intent i = new Intent();
                        i.setClass(getApplicationContext(), TodayActivity.class);
                        startActivity(i);
                    } else {
                        // if sign in fails, display a message to the user
                        Toasty.error(LoginActivity.this, getResources().getString(R.string.toast_auth_failed), Toast.LENGTH_SHORT, true).show();
                    }
                });
    }

    // onClick - open new activity
    public void openActivity(View view) {
        Intent i = new Intent();
        switch (view.getId()) {
            case R.id.registerButton:
                i.setClass(this, RegisterActivity.class);
                startActivity(i);
                break;
            case R.id.resetPasswordButton:
                i.setClass(this, ResetPasswordActivity.class);
                startActivity(i);
                break;
            default: break;
        }
    }

}