package com.example.habittracker.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habittracker.R;
import com.example.habittracker.adapters.ColorsAdapter;

public class CategoryColorsDialog extends Dialog {
    private ColorsAdapter adapter;
    private Context context;
    public CategoryColorsDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.getWindow().setDimAmount(0.4f);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        View view = LayoutInflater.from(context).inflate(R.layout.category_color_dialog, null);
        setContentView(view);

        RecyclerView recyclerView = view.findViewById(R.id.colorsRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));

        adapter = new ColorsAdapter(context);
        recyclerView.setAdapter(adapter);

        Button button = view.findViewById(R.id.chooseButton);
        button.setOnClickListener(v -> {
            dismiss();
        });
    }
}

