package com.example.habittracker.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habittracker.R;
import com.example.habittracker.adapters.IconsAdapter;

public class CategoryIconsDialog extends Dialog {
    private IconsAdapter adapter;
    private Context context;
    public CategoryIconsDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.getWindow().setDimAmount(0.4f);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        View view = LayoutInflater.from(context).inflate(R.layout.category_icon_dialog, null);
        setContentView(view);

        RecyclerView recyclerView = view.findViewById(R.id.iconsRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));

        adapter = new IconsAdapter(context);
        recyclerView.setAdapter(adapter);

        Button button = view.findViewById(R.id.chooseButton);
        button.setOnClickListener(v -> {
            dismiss();
        });
    }
}
