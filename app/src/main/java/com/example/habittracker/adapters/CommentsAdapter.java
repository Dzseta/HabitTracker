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
import com.example.habittracker.models.DayentryModel;
import com.example.habittracker.models.EntryModel;
import com.example.habittracker.models.HabitModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    // variables
    private ArrayList<?> entriesArrayList;
    private boolean mood;
    private Context context;

    // constructor
    public CommentsAdapter(boolean mood, ArrayList<?> entriesArrayList, Context context) {
        this.entriesArrayList = entriesArrayList;
        this.mood = mood;
        if(mood) {
            for(int i=entriesArrayList.size()-1; i>=0; i--) {
                if(((DayentryModel) entriesArrayList.get(i)).getComment().equals("")) {
                     this.entriesArrayList.remove(i);
                }
            }
            Collections.sort((ArrayList<DayentryModel>)entriesArrayList, Comparator.comparing(DayentryModel::getDate));
        } else {
            for(int i=entriesArrayList.size()-1; i>=0; i--) {
                if(((EntryModel) entriesArrayList.get(i)).getComment().equals("")) {
                    this.entriesArrayList.remove(i);
                }
            }
            Collections.sort((ArrayList<EntryModel>)entriesArrayList, Comparator.comparing(EntryModel::getDate));
        }
        this.context = context;
    }

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating layoutfile for recycler view items
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_card, parent, false);
        return new CommentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder holder, int position) {
        // setting data to recycler view item
        DayentryModel dmodel;
        EntryModel emodel;
        if(mood) {
            dmodel = (DayentryModel) entriesArrayList.get(position);
            holder.dateTextView.setText(dmodel.getDate());
            holder.commentTextView.setText(dmodel.getComment());
        } else {
            emodel = (EntryModel) entriesArrayList.get(position);
            holder.dateTextView.setText(emodel.getDate());
            holder.commentTextView.setText(emodel.getComment());
        }
    }

    @Override
    public int getItemCount() {
        // number of habits
        return entriesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // TextViews
        private TextView dateTextView;
        private TextView commentTextView;
        // Cardview
        private CardView commentCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            commentCardView = itemView.findViewById(R.id.commentCardView);
        }
    }
}