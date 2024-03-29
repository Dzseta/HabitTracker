package com.example.habittracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habittracker.R;
import com.example.habittracker.models.HabitModel;

import java.util.ArrayList;

public class HabitsAdapter extends RecyclerView.Adapter<HabitsAdapter.ViewHolder> {

    // variables
    private ArrayList<HabitModel> habitsArrayList;
    private Context context;
    // databasehandler
    private DatabaseHandler dbHandler;

    // constructor
    public HabitsAdapter(ArrayList<HabitModel> habitsArrayList, Context context) {
        this.habitsArrayList = habitsArrayList;
        this.context = context;
        // databasehandler
        dbHandler = new DatabaseHandler(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating layoutfile for recycler view items
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.habit_card, parent, false);
        return new HabitsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitsAdapter.ViewHolder holder, int position) {
        // setting data to recycler view item

    }

    @Override
    public int getItemCount() {
        // number of orders
        return habitsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
