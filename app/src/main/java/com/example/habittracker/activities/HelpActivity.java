package com.example.habittracker.activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.habittracker.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HelpActivity extends AppCompatActivity {

    private TextView responseTV;
    private TextView questionTV;
    private Button sendButton;
    private TextInputEditText queryEdt;
    public static final MediaType JSON = MediaType.get("application/json");
    OkHttpClient client = new OkHttpClient();

    private static final String url = "https://api.openai.com/v1/chat/completions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // initializing variables on below line.
        responseTV = findViewById(R.id.idTVResponse);
        questionTV = findViewById(R.id.idTVQuestion);
        queryEdt = findViewById(R.id.idEdtQuery);
        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(view -> {
            callAPI(queryEdt.getText().toString());
            queryEdt.setText("");
        });
    }

    void callAPI(String prompt) {
        // okhttp
        JSONObject json = new JSONObject();
        JSONObject response_format = new JSONObject();
        JSONArray messages = new JSONArray();
        JSONObject message = new JSONObject();
        JSONObject message2 = new JSONObject();
        try {
            response_format.put("type", "json_object");

            message.put("role", "system");
            message.put("content", "you are a helping a user with developing healthy habits and designed to output JSON. the user has the following habits: eating, drinking, exercising, sleeping");
            message.put("role", "system");
            message2.put("role", "user");
            message2.put("content", prompt);

            messages.put(message);
            messages.put(message2);

            json.put("model", "gpt-3.5-turbo-0125");
            json.put("response_format", response_format);
            json.put("messages", messages);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        RequestBody body = RequestBody.create(json.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer secretkey")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("FAILED");
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
                        System.out.println(answer);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("NOT SUCCESSFUL" + response.body().string());
                }
            }
        });
    }


}