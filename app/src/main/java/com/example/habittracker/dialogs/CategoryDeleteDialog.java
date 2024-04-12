package com.example.habittracker.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.example.habittracker.R;
import com.example.habittracker.adapters.CategoriesAdapter;
import com.example.habittracker.adapters.DatabaseHandler;

public class CategoryDeleteDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //using builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.category_delete_dialog)
                .setNegativeButton(R.string.button_cancel, (dialog, id) -> dismiss())
                .setPositiveButton(R.string.button_delete, (dialog, id) -> {
                    // TODO
                });
        //crete alert
        return builder.create();
    }
}
