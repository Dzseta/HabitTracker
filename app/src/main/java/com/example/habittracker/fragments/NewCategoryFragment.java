package com.example.habittracker.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class NewCategoryFragment extends BottomSheetDialogFragment {

    public static final String TAG = "NewCategoryFragment";
    private ItemClickListener mListener;
    TextView iconTextView;
    TextView colorTextView;
    Button createButton;
    EditText nameEditText;
    String icon;
    String color;

    public static NewCategoryFragment newInstance() {
        return new NewCategoryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_category, container, false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // set dim
        dialog.getWindow().setDimAmount(0.4f);
        // set onShowListener
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                // make background transparent (in the corners)
                FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                bottomSheet.setBackgroundResource(android.R.color.transparent);

                icon = "icon_categories";
                color = Integer.toString(Color.parseColor("#FFFFFF"));
                nameEditText = dialog.findViewById(R.id.editTextText);
                iconTextView = dialog.findViewById(R.id.iconTextView);
                iconTextView.setOnClickListener(view -> {
                    icon = mListener.onChooseIcon();
                });
                colorTextView = dialog.findViewById(R.id.colorTextView);
                colorTextView.setOnClickListener(view -> {
                    color = mListener.onChooseColor();
                });
                createButton = dialog.findViewById(R.id.createButton);
                createButton.setOnClickListener(view -> {
                    CategoryModel cat = new CategoryModel(icon, nameEditText.getText().toString(), color);
                    mListener.onCreateCategory(cat);
                    dismiss();

                });
            }
        });
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ItemClickListener) {
            mListener = (ItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ItemClickListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface ItemClickListener {
        void onCreateCategory(CategoryModel cat);
        String onChooseIcon();
        String onChooseColor();
    }
}