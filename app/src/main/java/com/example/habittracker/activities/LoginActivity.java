package com.example.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.habittracker.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    // onClick - open new activity
    public void openActivity(View view) {
        Intent i = new Intent();
        switch (view.getId()) {
            case R.id.registerButton:
                i.setClass(this, RegisterActivity.class);
                startActivity(i);
                break;
            case R.id.loginButton:
                i.setClass(this, HabitsActivity.class);
                startActivity(i);
                break;
            default: break;
        }
    }
}