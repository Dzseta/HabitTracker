package com.example.habittracker.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habittracker.R;
import com.example.habittracker.models.CategoryModel;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    // variables
    private ArrayList<CategoryModel> categoriesArrayList;
    private Context context;
    // databasehandler
    private DatabaseHandler dbHandler;

    // constructor
    public CategoriesAdapter(ArrayList<CategoryModel> categoriesArrayList, Context context) {
        this.categoriesArrayList = categoriesArrayList;
        this.context = context;
        // databasehandler
        dbHandler = new DatabaseHandler(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating layoutfile for recycler view items
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // setting data to recycler view item
        CategoryModel model = categoriesArrayList.get(position);
        holder.iconImageView.setImageResource(context.getResources().getIdentifier(model.getIcon(), "drawable", context.getPackageName()));
        holder.iconImageView.setBackgroundTintList(ColorStateList.valueOf(Integer.parseInt(model.getColor())));
        holder.nameTextView.setText(model.getName());
        holder.entriesTextView.setText(dbHandler.readAllHabitsInCategory(model.getName()).size() + " entries");
    }

    @Override
    public int getItemCount() {
        // number of orders
        return categoriesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // TextViews
        private TextView nameTextView;
        private TextView entriesTextView;
        // Cardview
        private CardView categoryCardView;
        // ImageView
        private ImageView iconImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            entriesTextView = itemView.findViewById(R.id.entriesTextView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            categoryCardView = itemView.findViewById(R.id.categoryCardView);
        }
    }
}
