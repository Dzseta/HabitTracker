package com.example.habittracker.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habittracker.R;
import com.example.habittracker.activities.CategoriesActivity;

public class ColorsAdapter extends RecyclerView.Adapter<ColorsAdapter.ViewHolder> {

    // variables
    private String[] colorsArray;
    private Context context;

    // constructor
    public ColorsAdapter(Context context) {
        this.colorsArray = context.getResources().getStringArray(R.array.iconColors);
        this.context = context;
    }

    @NonNull
    @Override
    public ColorsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating layoutfile for recycler view items
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_card, parent, false);
        return new ColorsAdapter.ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ColorsAdapter.ViewHolder holder, int position) {
        // setting data to recycler view item
        holder.colorImageView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorsArray[position])));
        holder.colorImageView.setOnClickListener(v -> {
            if (context instanceof CategoriesActivity) {
                ((CategoriesActivity) context).setColor(colorsArray[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        // number of categories
        return colorsArray.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView colorImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            colorImageView = itemView.findViewById(R.id.colorImageView);
        }
    }
}