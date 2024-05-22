package com.example.habittracker.activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habittracker.R;
import com.example.habittracker.adapters.DatabaseHandler;
import com.example.habittracker.models.CategoryModel;
import com.example.habittracker.models.DayentryModel;
import com.example.habittracker.models.EntryModel;
import com.example.habittracker.models.GoalModel;
import com.example.habittracker.models.HabitModel;
import com.example.habittracker.models.UserModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HelpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;
    UserModel userAccount;
    DocumentReference docref;
    private View hamburgerMenu;
    private ImageView helpIW;
    private TextView helpTW;
    private TextView questionTextView;
    private TextView answerTextView;
    private TextView premiumNeededTextView;
    private ImageButton sendButton;
    private TextInputEditText questionEditText;
    private ScrollView chatScrollView;
    private LinearLayout questionLinearLayout;
    DatabaseHandler dbHandler;
    public static final MediaType JSON = MediaType.get("application/json");
    OkHttpClient client;
    String topic;
    // spinner
    Spinner subjectSpinner;
    // array adapter
    ArrayAdapter<String> adapter;
    // sort's position
    int position;
    private static final String url = "https://api.openai.com/v1/chat/completions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // hamburger menu
        hamburgerMenu = findViewById(R.id.hamburgerMenu);
        helpIW = findViewById(R.id.helpImageView);
        helpIW.setColorFilter(ContextCompat.getColor(this, R.color.light_gray));
        helpTW = findViewById(R.id.helpTextView);
        helpTW.setTextColor(ContextCompat.getColor(this, R.color.light_gray));

        premiumNeededTextView = findViewById(R.id.premiumFunctionTextView);
        dbHandler = new DatabaseHandler(HelpActivity.this);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        // firebase
        db = FirebaseFirestore.getInstance();
        // get user status
        db.collection("users")
                .whereEqualTo("uid", FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            userAccount = new UserModel(document.getData().get("uid").toString(), document.getData().get("premium").toString());
                            System.out.println(document.getData().get("premium").toString());
                            docref = document.getReference();
                            if(userAccount.isPremium()) {
                                premiumNeededTextView.setVisibility(View.GONE);
                                chatScrollView.setVisibility(View.VISIBLE);
                                questionLinearLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        Toasty.error(HelpActivity.this, getResources().getString(R.string.toast_error), Toast.LENGTH_SHORT, true).show();
                    }
                });

        // spinner
        // get the spinner from the xml
        subjectSpinner = findViewById(R.id.subjectSpinner);
        String[] subjectOptions = {"", getResources().getString(R.string.title_categories), getResources().getString(R.string.title_habits), getResources().getString(R.string.title_goals), getResources().getString(R.string.help_motivation), getResources().getString(R.string.title_settings), getResources().getString(R.string.title_statistics)};
        // create an adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, subjectOptions);
        // set the spinner adapter
        subjectSpinner.setAdapter(adapter);
        subjectSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position2, long id) {
                        position = subjectSpinner.getSelectedItemPosition();
                        switch(position) {
                            case 0:
                                topic = "";
                                break;
                            case 1:
                                topic = "category";
                                break;
                            case 2:
                                topic = "habit";
                                break;
                            case 3:
                                topic = "goal";
                                break;
                            case 4:
                                topic = "motivation";
                                break;
                            case 5:
                                topic = "settings";
                                break;
                            case 6:
                                topic = "statistics";
                                break;
                        }
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                        position = 0;
                    }
                });

        client = new OkHttpClient();
        // initializing variables
        questionTextView = findViewById(R.id.questionTextView);
        answerTextView = findViewById(R.id.answerTextView);
        questionEditText = findViewById(R.id.questionEditText);
        chatScrollView = findViewById(R.id.chatScrollView);
        questionLinearLayout = findViewById(R.id.questionLinearLayout);
        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(view -> {
            questionTextView.setText(questionEditText.getText().toString());
            questionTextView.setVisibility(View.VISIBLE);
            questionEditText.setText("");
            answerTextView.setText(". . .");
            sendButton.setEnabled(false);
            callAPI(questionEditText.getText().toString());
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
            system.put("content", "you are an assistant and you are helping a user with developing healthy habits");
            user.put("role", "user");
            user.put("content", prompt);
            subject.put("role", "system");
            String data = "";
            data += "today is " + LocalDate.now().toString() + ", ";
            if(topic.equals("category")) {
                data += "existing habit categories: ";
                ArrayList<CategoryModel> categories = dbHandler.readAllCategories();
                for(int i=0; i<categories.size(); i++) {
                    data += categories.get(i).getName() + " with " + dbHandler.readAllHabitsInCategory(categories.get(i).getName()).size() + " habits in it, ";
                }
            } else if(topic.equals("habit")){
                data += "existing habits: ";
                ArrayList<HabitModel> habits = dbHandler.readAllHabits();
                for(int i=0; i<habits.size(); i++) {
                    data += habits.get(i).getName() + " with " + dbHandler.readAllEntriesByHabit(habits.get(i).getName()).size() + " entries, ";
                }
            } else if(topic.equals("goal")){
                data += "existing goals: ";
                ArrayList<GoalModel> goals = dbHandler.readAllGoals();
                for(int i=0; i<goals.size(); i++) {
                    data += "do " + goals.get(i).getHabit() + " " + goals.get(i).getNeeded() + " days in a row, ";
                }
            } else if(topic.equals("settings")){
                data += " tha application has the following options: hungarian and english language, a theme color chooser with yellow, red, green and blue colors, and the users can set daily reminder to get a notification at a given time";
            } else if(topic.equals("motivation")){
                data += " and you need to help the user with their motivation";
            } else if(topic.equals("statistics")){
                ArrayList<EntryModel> entries = dbHandler.readAllEntries();
                int successful = 0, failed = 0, skipped = 0;
                for(int i=0; i<entries.size(); i++) {
                    if(entries.get(i).getSuccess() == 1) successful++;
                    else if(entries.get(i).getSuccess() == 1) failed++;
                    else skipped++;
                }
                data += "the user had successfully done " + successful + " daily tasks, failed " + failed + " tasks and skipped " + skipped + " tasks";
                data += " and if the user wants detailed statistics then guide them to the Statistics menu";
            } else if(topic.equals("mood")){
                data += "previous moods: ";
                ArrayList<DayentryModel> dayentries = dbHandler.readAllDayentries();
                for(int i=0; i<dayentries.size(); i++) {
                    data += "on " + dayentries.get(i).getDate() + " the user had a mood of " + dayentries.get(i).getMood() + " out of 5, ";
                }
            }
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
                .header("Authorization", "Bearer " + getResources().getString(R.string.openai_secret))
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
                            sendButton.setEnabled(true);
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
            case R.id.helpButton:
                i.setClass(this, HelpActivity.class);
                startActivity(i);
                break;
            default: break;
        }
    }
}