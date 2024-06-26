package com.example.habittracker.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.example.habittracker.R;
import com.example.habittracker.activities.CategoriesActivity;
import com.example.habittracker.adapters.CategoriesAdapter;
import com.example.habittracker.adapters.DatabaseHandler;
import com.example.habittracker.models.CategoryModel;

public class CategoryDeleteDialog extends DialogFragment {

    private Context context;
    private CategoryModel model;
    private int position;
    public CategoryDeleteDialog(Context context, CategoryModel model, int position) {
        this.context = context;
        this.model = model;
        this.position = position;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //using builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getResources().getString(R.string.delete_dialog) + " " + model.getName() + getResources().getString(R.string.dialog_questionmark))
                .setNegativeButton(R.string.button_cancel, (dialog, id) -> dismiss())
                .setPositiveButton(R.string.button_delete, (dialog, id) -> {
                    if (context instanceof CategoriesActivity) {
                        ((CategoriesActivity) context).deleteCategory(model, position);
                    }
                });
        //crete alert
        return builder.create();
    }
}
