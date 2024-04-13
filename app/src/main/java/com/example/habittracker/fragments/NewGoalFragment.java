package com.example.habittracker.fragments;

import android.app.Dialog;
import android.content.Context;
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
import com.example.habittracker.adapters.DatabaseHandler;
import com.example.habittracker.models.CategoryModel;
import com.example.habittracker.models.GoalModel;
import com.example.habittracker.models.HabitModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class NewGoalFragment extends BottomSheetDialogFragment {

    public static final String TAG = "NewGoalFragment";
    private NewGoalFragment.ItemClickListener listener;
    ImageView iconImageView;
    Button createButton;
    EditText daysEditText;
    // spinner
    Spinner habitSpinner;
    // array adapter
    ArrayAdapter<String> adapter;
    // habit's position
    int position;
    // habits arraylist
    ArrayList<HabitModel> habitsArrayList;
    // habitnames array
    String[] habitNames;
    // database handler
    DatabaseHandler dbHandler;

    public static NewGoalFragment newInstance() {
        return new NewGoalFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_goal, container, false);
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

            // get iconImageView
            iconImageView = dialog.findViewById(R.id.iconImageView);
            // get editText
            daysEditText = dialog.findViewById(R.id.daysEditText);

            // get the spinner from the xml
            habitSpinner = dialog.findViewById(R.id.habitSpinner);
            dbHandler = new DatabaseHandler(getContext());
            habitsArrayList = dbHandler.readAllHabits();
            habitNames = new String[habitsArrayList.size()];
            for(int i=0; i<habitsArrayList.size(); i++) {
                habitNames[i] = habitsArrayList.get(i).getName();
            }
            // create an adapter to describe how the items are displayed
            adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, habitNames);
            // default position is 0
            position = 0;
            //set the spinners adapter to the previously created one.
            habitSpinner.setAdapter(adapter);
            habitSpinner.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(
                                AdapterView<?> parent, View view, int position2, long id) {
                            position = habitSpinner.getSelectedItemPosition();
                        }

                        public void onNothingSelected(AdapterView<?> parent) {
                            position = 0;
                        }
                    });

            createButton = dialog.findViewById(R.id.createButton);
            createButton.setOnClickListener(view -> {
                GoalModel prev = dbHandler.readGoalByHabit(habitNames[position]);
                if(daysEditText.getText().length()>0 && prev == null) {
                    GoalModel goal = new GoalModel(habitNames[position], Integer.parseInt(daysEditText.getText().toString()), false);
                    dbHandler.addGoal(goal);
                    listener.notifyChange(goal);
                    dismiss();
                }
            });
        });
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NewGoalFragment.ItemClickListener) {
            listener = (NewGoalFragment.ItemClickListener) context;
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
        void notifyChange(GoalModel goal);
    }
}