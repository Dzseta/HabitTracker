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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.example.habittracker.R;
import com.example.habittracker.activities.CategoriesActivity;
import com.example.habittracker.activities.NewHabitActivity;
import com.example.habittracker.dialogs.CategoryDeleteDialog;
import com.example.habittracker.dialogs.HabitDeleteDialog;
import com.example.habittracker.models.CategoryModel;
import com.example.habittracker.models.HabitModel;

import java.time.LocalDate;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // setting data to recycler view item
        HabitModel model = habitsArrayList.get(position);
        CategoryModel cat = dbHandler.readCategoryByName(model.getCategoryName());
        if(cat == null) cat = new CategoryModel("icon_categories", "No category","#ffffff");
        holder.iconImageView.setImageResource(context.getResources().getIdentifier(cat.getIcon(), "drawable", context.getPackageName()));
        holder.iconImageView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(cat.getColor())));
        holder.nameTextView.setText(model.getName());
        holder.descriptionTextView.setText(model.getDescription());

        if(model.getEndDate().equals("")) holder.dateTextView.setText(model.getStartDate() + " -");
        else holder.dateTextView.setText(model.getStartDate() + " - " + model.getEndDate());
        holder.editButton.setOnClickListener(v -> {
            Intent i = new Intent();
            i.putExtra("mode", "edit");
            i.putExtra("habit", model.getName());
            i.setClass(context, NewHabitActivity.class);
            startActivity(context, i, new Bundle());
        });
        holder.deleteButton.setOnClickListener(v -> {
            DialogFragment deleteDialog = new HabitDeleteDialog(context, model, position);
            deleteDialog.show(((AppCompatActivity)context).getSupportFragmentManager(), "habDeleteDialog");
            holder.habitSwipeRevealLayout.close(true);
        });
    }

    @Override
    public int getItemCount() {
        // number of habits
        return habitsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // SwipeRevealLayout
        private SwipeRevealLayout habitSwipeRevealLayout;
        // TextViews
        private TextView nameTextView;
        private TextView descriptionTextView;
        private TextView dateTextView;
        // Cardview
        private CardView habitCardView;
        // ImageView
        private ImageView iconImageView;
        // ImageButtons
        private ImageButton editButton;
        private ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            habitSwipeRevealLayout = itemView.findViewById(R.id.habitSwipeRevealLayout);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            habitCardView = itemView.findViewById(R.id.habitCardView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
