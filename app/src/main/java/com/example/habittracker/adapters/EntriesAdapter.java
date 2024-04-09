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
        if(cat == null) cat = new CategoryModel("icon_categories", "No category", Integer.toString(Color.parseColor("#ffffff")));
        holder.iconImageView.setImageResource(context.getResources().getIdentifier(cat.getIcon(), "drawable", context.getPackageName()));
        holder.iconImageView.setBackgroundTintList(ColorStateList.valueOf(Integer.parseInt(cat.getColor())));
        holder.nameTextView.setText(model.getHabit());
        holder.descriptionTextView.setText(habit.getDescription());
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
        private CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            entryCardView = itemView.findViewById(R.id.entryCardView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
