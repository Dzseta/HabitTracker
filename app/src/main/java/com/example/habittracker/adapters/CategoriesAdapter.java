package com.example.habittracker.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.example.habittracker.R;
import com.example.habittracker.activities.CategoriesActivity;
import com.example.habittracker.dialogs.CategoryDeleteDialog;
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
        holder.iconImageView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(model.getColor())));
        holder.nameTextView.setText(model.getName());
        holder.entriesTextView.setText(dbHandler.readAllHabitsInCategory(model.getName()).size() + " " + context.getResources().getString(R.string.category_entries));
        holder.editButton.setOnClickListener(v -> {
            if (context instanceof CategoriesActivity) {
                ((CategoriesActivity)context).showBottomSheet(v, model.getName());
                holder.categorySwipeRevealLayout.close(true);
            }
        });
        holder.deleteButton.setOnClickListener(v -> {
            //DialogFragment deleteDialog = new CategoryDeleteDialog();
            //deleteDialog.show(((AppCompatActivity)context).getSupportFragmentManager(), "catDeleteDialog");
            dbHandler.deleteCategory(model.getName());
            categoriesArrayList.remove(position);
            holder.categorySwipeRevealLayout.close(true);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        // number of categories
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
        // ImageButtons
        private ImageButton editButton;
        private ImageButton deleteButton;
        // SwipeRevealLayout
        private SwipeRevealLayout categorySwipeRevealLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categorySwipeRevealLayout = itemView.findViewById(R.id.categorySwipeRevealLayout);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            entriesTextView = itemView.findViewById(R.id.entriesTextView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            categoryCardView = itemView.findViewById(R.id.categoryCardView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
