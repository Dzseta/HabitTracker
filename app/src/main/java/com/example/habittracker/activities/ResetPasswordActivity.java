package com.example.habittracker.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.habittracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

public class ResetPasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // init
        emailEditText = findViewById(R.id.emailEditText);
        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(view -> resetPassword());
    }

    public void resetPassword(){
        mAuth.sendPasswordResetEmail(emailEditText.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toasty.info(ResetPasswordActivity.this, getResources().getString(R.string.toast_successful_password_reset), Toast.LENGTH_SHORT, true).show();
                        Intent i = new Intent();
                        i.setClass(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                    } else {
                        Toasty.error(ResetPasswordActivity.this, getResources().getString(R.string.toast_error), Toast.LENGTH_SHORT, true).show();
                    }
                });
    }
}