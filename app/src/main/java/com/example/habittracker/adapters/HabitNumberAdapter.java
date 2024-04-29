package com.example.habittracker.adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habittracker.R;
import com.example.habittracker.activities.NewHabitActivity;
import com.example.habittracker.models.CategoryModel;
import com.example.habittracker.models.EntryModel;
import com.example.habittracker.models.HabitModel;

import java.util.ArrayList;

public class HabitNumberAdapter extends RecyclerView.Adapter<HabitNumberAdapter.ViewHolder> {

    // variables
    private ArrayList<HabitModel> habitsArrayList;
    private ArrayList<Integer> habitNumbersArrayList;
    private Context context;
    // databasehandler
    private DatabaseHandler dbHandler;

    // constructor
    public HabitNumberAdapter(ArrayList<HabitModel> habitsArrayList, ArrayList<Integer> habitNumbersArrayList, Context context) {
        this.habitsArrayList = habitsArrayList;
        this.habitNumbersArrayList = habitNumbersArrayList;
        this.context = context;
        // databasehandler
        dbHandler = new DatabaseHandler(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating layoutfile for recycler view items
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.habitnumber_card, parent, false);
        return new HabitNumberAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // setting data to recycler view item
        HabitModel model = habitsArrayList.get(position);
        holder.nameTextView.setText(model.getName());
        holder.numberTextView.setText(Integer.toString(habitNumbersArrayList.get(position)));
    }

    @Override
    public int getItemCount() {
        // number of habits
        return habitsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // TextViews
        private TextView nameTextView;
        private TextView numberTextView;
        // Cardview
        private CardView habitCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            numberTextView = itemView.findViewById(R.id.numberTextView);
            habitCardView = itemView.findViewById(R.id.habitCardView);
        }
    }
}