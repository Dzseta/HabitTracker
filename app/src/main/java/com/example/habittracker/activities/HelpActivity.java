package com.example.habittracker.activities;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.habittracker.R;
import com.google.android.material.textfield.TextInputEditText;

public class HelpActivity extends AppCompatActivity {

    private TextView responseTV;
    private TextView questionTV;
    private TextInputEditText queryEdt;

    private static final String url = "https://api.openai.com/v1/chat/completions";
    private static final String key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // initializing variables on below line.
        responseTV = findViewById(R.id.idTVResponse);
        questionTV = findViewById(R.id.idTVQuestion);
        queryEdt = findViewById(R.id.idEdtQuery);
    }


}