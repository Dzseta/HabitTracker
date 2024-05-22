package com.example.habittracker.activities;

import static java.lang.Float.parseFloat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habittracker.R;
import com.example.habittracker.models.RatingModel;
import com.example.habittracker.models.UserModel;
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser user;
    private View hamburgerMenu;
    private ImageView profileIW;
    private TextView profileTW;
    private Button changeEmailButton;
    private Button changePasswordButton;
    private Button premiumButton;
    private Button logoutButton;
    private TextView origEmailTextView;
    private TextView premiumTextView;
    private EditText newEmailEditText;
    private EditText passwordEditText;
    private EditText oldPasswordEditText;
    private EditText newPasswordEditText;
    // sharedprefs
    private static String PREF_NAME = "optionsSharedPrefs";
    SharedPreferences prefs;
    // stripe
    PaymentSheet paymentSheet;
    String paymentIntentClientSecret;
    PaymentSheet.CustomerConfiguration customerConfig;
    // fs database
    FirebaseFirestore db;
    DocumentReference docref;
    // user
    UserModel userAccount;

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
        setContentView(R.layout.activity_profile);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        // hamburger menu
        hamburgerMenu = findViewById(R.id.hamburgerMenu);
        profileIW = findViewById(R.id.profileImageView);
        profileIW.setColorFilter(ContextCompat.getColor(this, R.color.light_gray));
        profileTW = findViewById(R.id.profileTextView);
        profileTW.setTextColor(ContextCompat.getColor(this, R.color.light_gray));
        // edittexts
        origEmailTextView = findViewById(R.id.origEmailTextView);
        origEmailTextView.setText(user.getEmail());
        premiumTextView = findViewById(R.id.premiumTextView);
        newEmailEditText = findViewById(R.id.newEmailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        oldPasswordEditText = findViewById(R.id.origPasswordEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        // paymentSheet
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);
        // buttons
        changeEmailButton = findViewById(R.id.changeEmailButton);
        changeEmailButton.setOnClickListener(view -> changeEmail());
        changePasswordButton = findViewById(R.id.changePasswordButton);
        changePasswordButton.setOnClickListener(view -> changePassword());
        premiumButton = findViewById(R.id.premiumButton);
        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(view -> logout());

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
                                premiumTextView.setText(getResources().getString(R.string.profile_premium_version));
                            } else {
                                premiumTextView.setText(getResources().getString(R.string.profile_free_version));
                                premiumButton.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        Toasty.error(ProfileActivity.this, getResources().getString(R.string.toast_error), Toast.LENGTH_SHORT, true).show();
                    }
                });
        // buy premium
        premiumButton.setOnClickListener(view -> {
            getDetails();
        });
    }

    // ########################################### STRIPE #######################################################
    void getDetails(){
        Fuel.INSTANCE.post("https://stripepayment-6ra6cylkra-uc.a.run.app?amt=" + "300000", null).responseString(new Handler<String>() {
            @Override
            public void success(String s) {
                try {
                    JSONObject result = new JSONObject(s);
                    customerConfig = new PaymentSheet.CustomerConfiguration(
                            result.getString("customer"),
                            result.getString("ephemeralKey")
                    );
                    paymentIntentClientSecret = result.getString("paymentIntent");
                    PaymentConfiguration.init(getApplicationContext(), result.getString("publishableKey"));

                    runOnUiThread(() -> showStripePaymentSheet());
                } catch (JSONException e) {
                    Toasty.error(ProfileActivity.this, getResources().getString(R.string.toast_error), Toast.LENGTH_SHORT, true).show();
                }
            }
            @Override
            public void failure(@NonNull FuelError fuelError) {
                Toasty.error(ProfileActivity.this, getResources().getString(R.string.toast_error), Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    void showStripePaymentSheet(){
        final PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder("Dzseta Kft.")
                .customer(customerConfig)
                .allowsDelayedPaymentMethods(true)
                .build();
        paymentSheet.presentWithPaymentIntent(
                paymentIntentClientSecret,
                configuration
        );
    }

    private void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Toasty.info(ProfileActivity.this, getResources().getString(R.string.toast_pay_cancel), Toast.LENGTH_SHORT, true).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Toasty.error(ProfileActivity.this, getResources().getString(R.string.toast_pay_failed), Toast.LENGTH_SHORT, true).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Toasty.success(ProfileActivity.this, getResources().getString(R.string.toast_pay_success), Toast.LENGTH_SHORT, true).show();
            userAccount.setPremium(true);
            docref.update("premium", userAccount.isPremium())
                    .addOnSuccessListener(aVoid -> {
                        premiumTextView.setText(getResources().getString(R.string.profile_premium_version));
                        premiumButton.setVisibility(View.GONE);
                    })
                    .addOnFailureListener(e -> Toasty.error(ProfileActivity.this, getResources().getString(R.string.toast_error), Toast.LENGTH_SHORT, true).show());
        }
    }

    // ########################################## ONCLICKS #####################################################
    // change email
    public void changeEmail(){
        // Get auth credentials from the user for re-authentication
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), passwordEditText.getText().toString());
        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential).addOnCompleteListener(task -> {
            // Now change your email address
            user.updateEmail(user.getEmail()).addOnCompleteListener(task1 -> {
                if (task1.isSuccessful()) {
                    Toasty.success(ProfileActivity.this, getResources().getString(R.string.toast_email_changed), Toast.LENGTH_SHORT, true).show();
                } else {
                    Toasty.error(ProfileActivity.this, getResources().getString(R.string.toast_auth_failed), Toast.LENGTH_SHORT, true).show();
                }
            });
        });
    }

    // change password
    public void changePassword(){
        if(newPasswordEditText.getText().toString().length() < 6) {
            Toasty.warning(ProfileActivity.this, getResources().getString(R.string.toast_password_length), Toast.LENGTH_SHORT, true).show();
            return;
        } else if (!newPasswordEditText.getText().toString().matches(".*\\d.*")) {
            Toasty.warning(ProfileActivity.this, getResources().getString(R.string.toast_password_number), Toast.LENGTH_SHORT, true).show();
            return;
        }
        FirebaseUser user = mAuth.getCurrentUser();
        // Get auth credentials from the user for re-authentication
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPasswordEditText.getText().toString());
        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential).addOnCompleteListener(task -> {
            // Now change your email address
            user.updatePassword(newPasswordEditText.getText().toString()).addOnCompleteListener(task1 -> {
                if (task1.isSuccessful()) {
                    Toasty.success(ProfileActivity.this, getResources().getString(R.string.toast_password_changed), Toast.LENGTH_SHORT, true).show();
                } else {
                    Toasty.error(ProfileActivity.this, getResources().getString(R.string.toast_auth_failed), Toast.LENGTH_SHORT, true).show();
                }
            });
        });
    }

    // logout
    public void logout() {
        FirebaseAuth.getInstance().signOut();
        Toasty.info(ProfileActivity.this, getResources().getString(R.string.toast_logout), Toast.LENGTH_SHORT, true).show();
        Intent i = new Intent();
        i.setClass(this, LoginActivity.class);
        startActivity(i);
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
            case R.id.helpButton:
                i.setClass(this, HelpActivity.class);
                startActivity(i);
                break;
            default: break;
        }
    }
}