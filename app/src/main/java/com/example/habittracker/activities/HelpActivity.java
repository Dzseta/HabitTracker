package com.example.habittracker.activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habittracker.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HelpActivity extends AppCompatActivity {

    private TextView questionTextView;
    private TextView answerTextView;
    private ImageButton sendButton;
    private TextInputEditText questionEditText;
    public static final MediaType JSON = MediaType.get("application/json");
    OkHttpClient client = new OkHttpClient();
    String answer;

    private static final String url = "https://api.openai.com/v1/chat/completions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // initializing variables on below line
        questionTextView = findViewById(R.id.questionTextView);
        answerTextView = findViewById(R.id.answerTextView);
        questionEditText = findViewById(R.id.questionEditText);
        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(view -> {
            callAPI(questionEditText.getText().toString());
            questionTextView.setText(questionEditText.getText().toString());
            questionTextView.setVisibility(View.VISIBLE);
            questionEditText.setText("");
            answerTextView.setText(answer);
        });
    }

    void callAPI(String prompt) {
        // okhttp
        JSONObject json = new JSONObject();
        JSONObject response_format = new JSONObject();
        JSONArray messages = new JSONArray();
        JSONObject system = new JSONObject();
        JSONObject user = new JSONObject();
        JSONObject subject = new JSONObject();
        try {
            response_format.put("type", "text");

            system.put("role", "system");
            system.put("content", "you are a helpful assistant and you are helping a user with developing healthy habits");
            user.put("role", "user");
            user.put("content", prompt);
            subject.put("role", "system");
            String data = "";
            data += "today is " + LocalDate.now().toString();
            subject.put("content", data);

            messages.put(system);
            messages.put(user);
            messages.put(subject);

            json.put("model", "gpt-4o");
            json.put("response_format", response_format);
            json.put("messages", messages);
            json.put("max_tokens", 400);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        RequestBody body = RequestBody.create(json.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer ")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toasty.error(HelpActivity.this, getResources().getString(R.string.toast_error), Toast.LENGTH_SHORT, true).show();
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    JSONObject json = null;
                    try {
                        json = new JSONObject(response.body().string());
                        JSONArray array = json.getJSONArray("choices");
                        JSONObject result = array.getJSONObject(0).getJSONObject("message");
                        String answer = result.getString("content");
                        HelpActivity.this.runOnUiThread(() -> {
                            answerTextView.setText(answer);
                        });
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toasty.error(HelpActivity.this, getResources().getString(R.string.toast_error), Toast.LENGTH_SHORT, true).show();
                }
            }
        });
    }

    // ###################################################### ONCLICK #################################################################
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
            case R.id.helpButton:
                i.setClass(this, HelpActivity.class);
                startActivity(i);
                break;
            default: break;
        }
    }
}