package com.example.habittracker.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.habittracker.R;
import com.example.habittracker.adapters.DatabaseHandler;
import com.example.habittracker.models.CategoryModel;
import com.example.habittracker.models.GoalModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class NewCategoryFragment extends BottomSheetDialogFragment {

    public static final String TAG = "NewCategoryFragment";
    private ItemClickListener listener;
    ImageView iconImageView;
    TextView iconTextView;
    TextView colorTextView;
    Button createButton;
    EditText nameEditText;
    String icon;
    String color;
    // mode and orig name
    String mode;
    String origName;
    // database handler
    DatabaseHandler dbHandler;

    public static NewCategoryFragment newInstance() {
        return new NewCategoryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mode = getArguments().getString("mode");
        if(mode.equals("edit")) origName = getArguments().getString("origName");
        return inflater.inflate(R.layout.fragment_new_category, container, false);
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

            icon = "icon_categories";
            color = "#FFFFFF";
            dbHandler = new DatabaseHandler(getContext());
            iconImageView = dialog.findViewById(R.id.iconImageView);
            nameEditText = dialog.findViewById(R.id.editTextText);
            iconTextView = dialog.findViewById(R.id.iconTextView);
            if(mode.equals("edit")) {
                CategoryModel origCat = dbHandler.readCategoryByName(origName);
                nameEditText.setText(origCat.getName());
                setColor(origCat.getColor());
                setIcon(origCat.getIcon());
            }
            iconTextView.setOnClickListener(view -> {
                listener.onChooseIcon();
            });
            colorTextView = dialog.findViewById(R.id.colorTextView);
            colorTextView.setOnClickListener(view -> {
                listener.onChooseColor();
            });
            createButton = dialog.findViewById(R.id.createButton);
            createButton.setOnClickListener(view -> {
                CategoryModel cat = new CategoryModel(icon, nameEditText.getText().toString(), color);
                if(mode.equals("new")) {
                    listener.onCreateCategory(cat);
                    dismiss();
                } else if (mode.equals("edit")) {
                    listener.onEditCategory(cat, origName);
                    dismiss();
                }
            });
        });
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ItemClickListener) {
            listener = (ItemClickListener) context;
        } else {
            throw new RuntimeException(context + " must implement ItemClickListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void setColor(String color){
        this.color = color;
        iconImageView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
    }

    public void setIcon(String icon){
        this.icon = icon;
        iconImageView.setImageResource(getResources().getIdentifier(icon, "drawable", getContext().getPackageName()));
    }

    public interface ItemClickListener {
        void onCreateCategory(CategoryModel cat);
        void onEditCategory(CategoryModel cat, String origName);
        void onChooseIcon();
        void onChooseColor();
    }
}