package com.example.habittracker.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.habittracker.R;
import com.example.habittracker.adapters.DatabaseHandler;
import com.example.habittracker.models.CategoryModel;
import com.example.habittracker.models.EntryModel;
import com.example.habittracker.models.GoalModel;
import com.example.habittracker.models.HabitModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.vo.DateData;

public class NewEntryFragment extends BottomSheetDialogFragment {

    public static final String TAG = "NewEntryFragment";
    private NewEntryFragment.ItemClickListener listener;
    // buttons
    Button createButton;
    // edittexts
    EditText commentEditText;
    EditText numberEditText;
    // textviews
    TextView goalTextView;
    // numberpickers
    NumberPicker hourNumberPicker;
    NumberPicker minuteNumberPicker;
    NumberPicker secondNumberPicker;
    // linearlayouts
    LinearLayout yesnoLinearLayout;
    LinearLayout numberLinearLayout;
    LinearLayout timeLinearLayout;
    // database handler
    DatabaseHandler dbHandler;
    // imageViews
    ImageView failCheckboxImageView;
    ImageView neutralCheckboxImageView;
    ImageView successCheckboxImageView;
    // strings
    String habit;
    String date;

    public static NewEntryFragment newInstance() {
        return new NewEntryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        habit = getArguments().getString("habit");
        date = getArguments().getString("date");
        return inflater.inflate(R.layout.fragment_new_entry, container, false);
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
            commentEditText = dialog.findViewById(R.id.commentMultiLine);
            numberEditText = dialog.findViewById(R.id.numberEditText);
            // get textviews
            goalTextView = dialog.findViewById(R.id.goalTextView);
            // get imageviews
            failCheckboxImageView = dialog.findViewById(R.id.failCheckboxImageView);
            neutralCheckboxImageView = dialog.findViewById(R.id.neutralCheckboxImageView);
            successCheckboxImageView = dialog.findViewById(R.id.successCheckboxImageView);
            // get numberpickers
            hourNumberPicker = dialog.findViewById(R.id.hourNumberPicker);
            hourNumberPicker.setMinValue(0);
            hourNumberPicker.setMaxValue(99);
            minuteNumberPicker = dialog.findViewById(R.id.minuteNumberPicker);
            minuteNumberPicker.setMinValue(0);
            minuteNumberPicker.setMaxValue(59);
            secondNumberPicker = dialog.findViewById(R.id.secondNumberPicker);
            secondNumberPicker.setMinValue(0);
            secondNumberPicker.setMaxValue(59);
            // get linearlayouts
            yesnoLinearLayout = dialog.findViewById(R.id.yesnoLinearLayout);
            numberLinearLayout = dialog.findViewById(R.id.numberLinearLayout);
            timeLinearLayout = dialog.findViewById(R.id.timeLinearLayout);
            // dbHandler
            dbHandler = new DatabaseHandler(getContext());
            // entry and habit
            EntryModel entryModel = dbHandler.readEntryByHabitAndDate(habit, date);
            HabitModel habitModel = dbHandler.readHabitByName(habit);
            // setup the UI data
            commentEditText.setText(entryModel.getComment());
            if(habitModel.getType().equals("yesno")) {
                yesnoLinearLayout.setVisibility(View.VISIBLE);
                switch (entryModel.getSuccess()) {
                    case -1:
                        failCheckboxImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray));
                        successCheckboxImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray));
                        break;
                    case 0:
                        neutralCheckboxImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray));
                        successCheckboxImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray));
                        break;
                    case 1:
                        failCheckboxImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray));
                        neutralCheckboxImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray));
                        break;
                }
            } else if(habitModel.getType().equals("number")) {
                numberLinearLayout.setVisibility(View.VISIBLE);
                numberEditText.setText(entryModel.getData());
                goalTextView.setText(getResources().getString(R.string.today_per) + " " + habitModel.getTypeData());
            } else {
                timeLinearLayout.setVisibility(View.VISIBLE);
                if(!entryModel.getData().equals("")){
                    String[] time = entryModel.getData().split(":");
                    hourNumberPicker.setValue(Integer.parseInt(time[0]));
                    minuteNumberPicker.setValue(Integer.parseInt(time[1]));
                    secondNumberPicker.setValue(Integer.parseInt(time[2]));
                }
            }
            // onCLickListeners
            failCheckboxImageView.setOnClickListener(view -> {
                neutralCheckboxImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray));
                successCheckboxImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray));
                failCheckboxImageView.clearColorFilter();
                entryModel.setData("false");
                entryModel.setSuccess(0);
            });
            neutralCheckboxImageView.setOnClickListener(view -> {
                failCheckboxImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray));
                successCheckboxImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray));
                neutralCheckboxImageView.clearColorFilter();
                entryModel.setData("");
                entryModel.setSuccess(-1);
            });
            successCheckboxImageView.setOnClickListener(view -> {
                failCheckboxImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray));
                neutralCheckboxImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray));
                successCheckboxImageView.clearColorFilter();
                entryModel.setData("true");
                entryModel.setSuccess(1);
            });
            createButton = dialog.findViewById(R.id.createButton);
            createButton.setOnClickListener(view -> {
                entryModel.setComment(commentEditText.getText().toString());
                if(habitModel.getType().equals("number")) {
                    entryModel.setData(numberEditText.getText().toString());
                    if(!entryModel.getData().equals("")) {
                        if(habitModel.evaluate(entryModel.getData())) entryModel.setSuccess(1);
                        else entryModel.setSuccess(0);
                    } else entryModel.setSuccess(-1);
                } else if(habitModel.getType().equals("time")) {
                    entryModel.setData(hourNumberPicker.getValue() + ":" + minuteNumberPicker.getValue() + ":" + secondNumberPicker.getValue());
                    if(!entryModel.getData().equals("")) {
                        if(habitModel.evaluate(entryModel.getData())) entryModel.setSuccess(1);
                        else entryModel.setSuccess(0);
                    } else entryModel.setSuccess(-1);
                }
                dbHandler.updateEntry(entryModel);
                listener.notifyEntryChange(entryModel);
                dismiss();
            });
        });
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NewEntryFragment.ItemClickListener) {
            listener = (NewEntryFragment.ItemClickListener) context;
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
        void notifyEntryChange(EntryModel entry);
    }
}