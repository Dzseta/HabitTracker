package com.example.habittracker.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habittracker.R;
import com.example.habittracker.activities.CategoriesActivity;
import com.example.habittracker.activities.GoalsActivity;
import com.example.habittracker.models.CategoryModel;
import com.example.habittracker.models.EntryModel;
import com.example.habittracker.models.GoalModel;
import com.example.habittracker.models.HabitModel;
import com.example.habittracker.views.SwipeRevealLayout;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
        CategoryModel cat;
        if(habit == null) cat = dbHandler.readCategoryByName("");
        else cat = dbHandler.readCategoryByName(habit.getCategoryName());
        if(cat == null) cat = new CategoryModel("icon_categories", "No category", "#ffffff");
        holder.iconImageView.setImageResource(context.getResources().getIdentifier(cat.getIcon(), "drawable", context.getPackageName()));
        holder.iconImageView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(cat.getColor())));
        holder.nameTextView.setText(model.getHabit());
        // get successes
        ArrayList<EntryModel> entries = dbHandler.readAllEntriesByHabit(model.getHabit());
        if(model.isFinished()) {
            holder.streakTextView.setText(model.getNeeded() + " / " + model.getNeeded());
            holder.progressBar.setProgress(100);
        } else if (entries.size() == 0) {
            holder.streakTextView.setText(0 + " / " + model.getNeeded());
            holder.progressBar.setProgress(0);
        } else {
            int streak = model.streak(entries);
            if(streak >= model.getNeeded()) {
                holder.streakTextView.setText(model.getNeeded() + " / " + model.getNeeded());
                holder.progressBar.setProgress(100);
                model.setFinished(true);
                dbHandler.updateGoal(model);
            }
            holder.streakTextView.setText(streak + " / " + model.getNeeded());
            holder.progressBar.setProgress((int) Math.floor(1.0 * streak / model.getNeeded() * 100));
        }

        holder.editButton.setOnClickListener(v -> {
            if (context instanceof GoalsActivity) {
                ((GoalsActivity)context).showBottomSheet(v, model.getHabit());
                holder.goalSwipeRevealLayout.close(true);
            }
        });
        holder.deleteButton.setOnClickListener(v -> {
            dbHandler.deleteGoal(model.getHabit());
            goalsArrayList.remove(position);
            holder.goalSwipeRevealLayout.close(true);
            notifyDataSetChanged();
        });
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
        // progressBar
        private ProgressBar progressBar;
        // ImageButtons
        private ImageButton editButton;
        private ImageButton deleteButton;
        // SwipeRevealLayout
        private SwipeRevealLayout goalSwipeRevealLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            streakTextView = itemView.findViewById(R.id.streakTextView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            goalCardView = itemView.findViewById(R.id.goalCardView);
            progressBar = itemView.findViewById(R.id.progressBar);
            goalSwipeRevealLayout = itemView.findViewById(R.id.goalSwipeRevealLayout);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
