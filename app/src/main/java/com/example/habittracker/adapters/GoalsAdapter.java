package com.example.habittracker.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habittracker.R;
import com.example.habittracker.models.CategoryModel;
import com.example.habittracker.models.GoalModel;
import com.example.habittracker.models.HabitModel;

import java.util.ArrayList;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.ViewHolder> {

    // variables
    private ArrayList<GoalModel> goalsArrayList;
    private Context context;
    // databasehandler
    private DatabaseHandler dbHandler;

    // constructor
    public GoalsAdapter(ArrayList<GoalModel> goalsArrayList, Context context) {
        this.goalsArrayList = goalsArrayList;
        this.context = context;
        // databasehandler
        dbHandler = new DatabaseHandler(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating layoutfile for recycler view items
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goal_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // setting data to recycler view item
        GoalModel model = goalsArrayList.get(position);
        HabitModel habit = dbHandler.readHabitByName(model.getHabit());
        CategoryModel cat = null;
        if(habit == null) cat = dbHandler.readCategoryByName("");
        else cat = dbHandler.readCategoryByName(habit.getCategoryName());
        if(cat == null) cat = new CategoryModel("icon_categories", "No category", Integer.toString(Color.parseColor("#ffffff")));
        holder.iconImageView.setImageResource(context.getResources().getIdentifier(cat.getIcon(), "drawable", context.getPackageName()));
        holder.iconImageView.setBackgroundTintList(ColorStateList.valueOf(Integer.parseInt(cat.getColor())));
        holder.nameTextView.setText(model.getHabit());
        holder.streakTextView.setText(model.getSuccesses() + " / " + model.getNeeded());
    }

    @Override
    public int getItemCount() {
        // number of goals
        return goalsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // TextViews
        private TextView nameTextView;
        private TextView streakTextView;
        // Cardview
        private CardView goalCardView;
        // ImageView
        private ImageView iconImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            streakTextView = itemView.findViewById(R.id.streakTextView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            goalCardView = itemView.findViewById(R.id.goalCardView);
        }
    }
}
