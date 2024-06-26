package com.example.habittracker.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habittracker.R;
import com.example.habittracker.activities.CategoriesActivity;
import com.example.habittracker.activities.TodayActivity;
import com.example.habittracker.models.CategoryModel;
import com.example.habittracker.models.EntryModel;
import com.example.habittracker.models.HabitModel;

import java.util.ArrayList;

public class EntriesAdapter extends RecyclerView.Adapter<EntriesAdapter.ViewHolder> {

    // variables
    private ArrayList<EntryModel> entriesArrayList;
    private Context context;
    // databasehandler
    private DatabaseHandler dbHandler;

    // constructor
    public EntriesAdapter(ArrayList<EntryModel> entriesArrayList, Context context) {
        this.entriesArrayList = entriesArrayList;
        this.context = context;
        // databasehandler
        dbHandler = new DatabaseHandler(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating layoutfile for recycler view items
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_card, parent, false);
        return new EntriesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // setting data to recycler view item
        EntryModel model = entriesArrayList.get(position);
        HabitModel habit = dbHandler.readHabitByName(model.getHabit());
        CategoryModel cat = dbHandler.readCategoryByName(habit.getCategoryName());
        if(cat == null) cat = new CategoryModel("icon_categories", "No category", "#ffffff");
        holder.iconImageView.setImageResource(context.getResources().getIdentifier(cat.getIcon(), "drawable", context.getPackageName()));
        holder.iconImageView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(cat.getColor())));
        holder.nameTextView.setText(model.getHabit());
        holder.descriptionTextView.setText(habit.getDescription());
        if(model.getSuccess() == 1) holder.checkboxImageView.setImageResource(context.getResources().getIdentifier("icon_success", "drawable", context.getPackageName()));
        else if(model.getSuccess() == 0) holder.checkboxImageView.setImageResource(context.getResources().getIdentifier("icon_fail", "drawable", context.getPackageName()));
        else holder.checkboxImageView.setImageResource(context.getResources().getIdentifier("icon_neutral", "drawable", context.getPackageName()));

        holder.checkboxImageView.setOnClickListener(view -> {
            if (context instanceof TodayActivity) {
                ((TodayActivity)context).showBottomSheet(view, model);
            }

            /*if(holder.checkBox.isChecked()) {
                model.setData("true");
                if(habit.evaluate("true")) model.setSuccess(1);
                else model.setSuccess(0);
            } else {
                model.setData("false");
                if(habit.evaluate("true")) model.setSuccess(1);
                else model.setSuccess(0);
            }
            dbHandler.updateEntry(model);*/
        });
    }

    @Override
    public int getItemCount() {
        // number of habits
        return entriesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // TextViews
        private TextView nameTextView;
        private TextView descriptionTextView;
        // Cardview
        private CardView entryCardView;
        // ImageView
        private ImageView iconImageView;
        // Checkbox
        private ImageView checkboxImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            entryCardView = itemView.findViewById(R.id.entryCardView);
            checkboxImageView = itemView.findViewById(R.id.checkboxImageView);
        }
    }
}
