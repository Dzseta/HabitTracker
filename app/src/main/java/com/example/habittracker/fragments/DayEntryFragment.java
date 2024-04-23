package com.example.habittracker.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.habittracker.R;
import com.example.habittracker.activities.TodayActivity;
import com.example.habittracker.adapters.DatabaseHandler;
import com.example.habittracker.models.DayentryModel;
import com.example.habittracker.models.GoalModel;
import com.example.habittracker.models.HabitModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.LocalDate;
import java.util.ArrayList;

public class DayEntryFragment extends BottomSheetDialogFragment {

    public static final String TAG = "DayEntryFragment";
    private DayEntryFragment.ItemClickListener listener;
    // date and mood
    String date;
    int mood;
    boolean edit;
    // buttons
    Button createButton;
    // edittexts
    EditText commentMultiLine;
    // imageViews
    ImageView verySadImageView;
    ImageView sadImageView;
    ImageView neutralImageView;
    ImageView happyImageView;
    ImageView veryHappyImageView;
    // database handler
    DatabaseHandler dbHandler;

    public static DayEntryFragment newInstance() {
        return new DayEntryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        date = getArguments().getString("date");
        mood = 0;
        return inflater.inflate(R.layout.fragment_day_entry, container, false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // set dim
        dialog.getWindow().setDimAmount(0.4f);
        // set onShowListener
        dialog.setOnShowListener(dialogInterface -> {
            // make background transparent (in the corners)
            FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            bottomSheet.setBackgroundResource(android.R.color.transparent);

            // get editText
            commentMultiLine = dialog.findViewById(R.id.commentMultiLine);
            // dbHandler
            dbHandler = new DatabaseHandler(getContext());
            // imageViews
            verySadImageView = dialog.findViewById(R.id.verySadImageView);
            sadImageView = dialog.findViewById(R.id.sadImageView);
            neutralImageView = dialog.findViewById(R.id.neutralImageView);
            happyImageView = dialog.findViewById(R.id.happyImageView);
            veryHappyImageView = dialog.findViewById(R.id.veryHappyImageView);
            verySadImageView.setOnClickListener(view -> changeMood(1));
            sadImageView.setOnClickListener(view -> changeMood(2));
            neutralImageView.setOnClickListener(view -> changeMood(3));
            happyImageView.setOnClickListener(view -> changeMood(4));
            veryHappyImageView.setOnClickListener(view -> changeMood(5));
            // set data
            DayentryModel dayentry = dbHandler.readDayentryByDate(date);

            if(dbHandler.readDayentryByDate(date) != null) {
                commentMultiLine.setText(dayentry.getComment());
                changeMood(dayentry.getMood());
                edit = true;
            } else edit = false;

            createButton = dialog.findViewById(R.id.createButton);
            createButton.setOnClickListener(view -> {
                DayentryModel de = new DayentryModel(date, mood, commentMultiLine.getText().toString());
                if(edit) {
                    dbHandler.updateDayentry(de);
                } else {
                    dbHandler.addDayentry(de);
                }
                listener.notifyChange(mood);
                dismiss();
            });
        });
        return dialog;
    }

    public void changeMood(int mood) {
        verySadImageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.medium_gray)));
        sadImageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.medium_gray)));
        neutralImageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.medium_gray)));
        happyImageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.medium_gray)));
        veryHappyImageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.medium_gray)));
        switch (mood) {
            case 1:
                verySadImageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                break;
            case 2:
                sadImageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                break;
            case 3:
                neutralImageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                break;
            case 4:
                happyImageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                break;
            case 5:
                veryHappyImageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                break;
        }
        this.mood = mood;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DayEntryFragment.ItemClickListener) {
            listener = (DayEntryFragment.ItemClickListener) context;
        } else {
            throw new RuntimeException(context + " must implement ItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface ItemClickListener {
        void notifyChange(int mood);
    }
}