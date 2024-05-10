package com.example.habittracker.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.example.habittracker.R;
import com.example.habittracker.activities.CategoriesActivity;
import com.example.habittracker.activities.GoalsActivity;
import com.example.habittracker.models.CategoryModel;
import com.example.habittracker.models.GoalModel;

public class GoalDeleteDialog extends DialogFragment {

    private Context context;
    private GoalModel model;
    private int position;
    public GoalDeleteDialog(Context context, GoalModel model, int position) {
        this.context = context;
        this.model = model;
        this.position = position;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //using builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getResources().getString(R.string.delete_dialog) + " " + model.getHabit() + getResources().getString(R.string.dialog_questionmark))
                .setNegativeButton(R.string.button_cancel, (dialog, id) -> dismiss())
                .setPositiveButton(R.string.button_delete, (dialog, id) -> {
                    if (context instanceof GoalsActivity) {
                        ((GoalsActivity) context).deleteGoal(model, position);
                    }
                });
        //crete alert
        return builder.create();
    }
}