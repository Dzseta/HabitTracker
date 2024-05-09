package com.example.habittracker.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habittracker.R;
import com.example.habittracker.activities.CategoriesActivity;
import com.example.habittracker.models.CategoryModel;

import java.util.ArrayList;

public class IconsAdapter extends RecyclerView.Adapter<IconsAdapter.ViewHolder> {

    // variables
    private String[] iconsArray;
    private Context context;
    private String color;

    // constructor
    public IconsAdapter(Context context, String color) {
        this.iconsArray = context.getResources().getStringArray(R.array.icons);
        this.context = context;
        this.color = color;
    }

    @NonNull
    @Override
    public IconsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating layoutfile for recycler view items
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_card, parent, false);
        return new IconsAdapter.ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull IconsAdapter.ViewHolder holder, int position) {
        // setting data to recycler view item
        holder.iconImageView.setImageResource(context.getResources().getIdentifier(iconsArray[position], "drawable", context.getPackageName()));
        holder.iconImageView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
        holder.iconImageView.setOnClickListener(v -> {
            if (context instanceof CategoriesActivity) {
                ((CategoriesActivity) context).setIcon(iconsArray[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        // number of categories
        return iconsArray.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iconImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
        }
    }
}
