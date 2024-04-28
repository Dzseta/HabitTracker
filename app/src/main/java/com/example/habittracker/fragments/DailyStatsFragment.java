package com.example.habittracker.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.habittracker.R;
import com.example.habittracker.activities.TodayActivity;
import com.example.habittracker.adapters.DatabaseHandler;
import com.example.habittracker.models.DayentryModel;
import com.example.habittracker.models.EntryModel;
import com.example.habittracker.models.HabitModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.time.LocalDate;
import java.util.ArrayList;

public class DailyStatsFragment extends Fragment {

    // imageviews
    ImageView leftImageView;
    ImageView rightImageView;
    // textviews
    TextView dateTextView;
    TextView moodValueTextView;
    TextView commentTextView;
    TextView successfulTextView;
    TextView skippedTextView;
    TextView failedTextView;
    TextView scoreTextView;
    // charts
    PieChart pieChart;
    // date
    LocalDate now;
    // database
    DatabaseHandler dbHandler;
    // data
    DayentryModel dayEntry;
    ArrayList<EntryModel> entryArrayList;
    ArrayList<HabitModel> habitArrayList;
    int[] colors;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // initialize
        View v = inflater.inflate(R.layout.fragment_daily_stats, container, false);
        // imageviews
        leftImageView = v.findViewById(R.id.leftImageButton);
        rightImageView = v.findViewById(R.id.rightImageButton);
        // textviews
        dateTextView = v.findViewById(R.id.dateTextView);
        moodValueTextView = v.findViewById(R.id.moodValueTextView);
        commentTextView = v.findViewById(R.id.commentTextView);
        successfulTextView = v.findViewById(R.id.successfulTextView);
        skippedTextView = v.findViewById(R.id.skippedTextView);
        failedTextView = v.findViewById(R.id.failedTextView);
        scoreTextView = v.findViewById(R.id.scoreTextView);
        // charts
        pieChart = (PieChart) v.findViewById(R.id.chart);
        colors = new int[]{getResources().getColor(R.color.green), getResources().getColor(R.color.yellow), getResources().getColor(R.color.red)};
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
        pieChart.setHoleColor(getResources().getColor(R.color.dark_gray));
        Description description = pieChart.getDescription();
        description.setEnabled(false);
        // date
        now = LocalDate.now();
        // datebasehandler
        dbHandler = new DatabaseHandler(getContext());

        // load data
        loadData();

        // onClicks
        leftImageView.setOnClickListener(view -> {
            if(now.isEqual(LocalDate.now())) {
                TypedValue typedValue = new TypedValue();
                getContext().getTheme().resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true);
                int c = ContextCompat.getColor(getContext(), typedValue.resourceId);
                rightImageView.setColorFilter(c);
            }
            now = now.minusDays(1);
            loadData();
        });

        if(now.isEqual(LocalDate.now())) rightImageView.setColorFilter(getResources().getColor(R.color.light_gray));
        rightImageView.setOnClickListener(view -> {
            if(!now.isEqual(LocalDate.now())) {
                now = now.plusDays(1);
                loadData();
                if(now.isEqual(LocalDate.now())) rightImageView.setColorFilter(getResources().getColor(R.color.light_gray));
            }
        });

        // return
        return v;
    }

    public void loadData(){
        // set date to text
        dateTextView.setText(now.toString());
        // load from database
        dayEntry = dbHandler.readDayentryByDate(now.toString());
        if(dayEntry == null) dayEntry = new DayentryModel(now.toString(), 3, "");
        habitArrayList = dbHandler.readAllHabits();
        // create missing entries
        for(int i=0; i<habitArrayList.size(); i++) {
            LocalDate startDate = LocalDate.parse(habitArrayList.get(i).getStartDate());
            LocalDate endDate = null;
            String[] days = habitArrayList.get(i).getRepeatType().split("-");
            if(!habitArrayList.get(i).getEndDate().equals("")) endDate = LocalDate.parse(habitArrayList.get(i).getEndDate()).plusDays(1);
            if(startDate.isBefore(now.plusDays(1)) && (endDate == null || endDate.isAfter(now.plusDays(1)))) {
                if(dbHandler.readEntryByHabitAndDate(habitArrayList.get(i).getName(), now.toString()) == null && (days[0].equals("everyday") || ((days.length>now.getDayOfWeek().getValue()-1) && !days[now.getDayOfWeek().getValue()-1].equals("")))) {
                    EntryModel entry = new EntryModel(habitArrayList.get(i).getName(), now.toString(), "", -1);
                    dbHandler.addEntry(entry);
                }
            }
        }
        entryArrayList = dbHandler.readAllEntriesByDate(now.toString());

        // setup the texview
        moodValueTextView.setText(Integer.toString(dayEntry.getMood()));
        commentTextView.setText(dayEntry.getComment());
        int successful = 0, skipped = 0, failed = 0;
        for(int i=0; i<entryArrayList.size(); i++) {
            if(entryArrayList.get(i).getSuccess() == 1) successful++;
            else if(entryArrayList.get(i).getSuccess() == 0) failed++;
            if(entryArrayList.get(i).getSuccess() == -1) skipped++;
        }
        successfulTextView.setText(Integer.toString(successful));
        skippedTextView.setText(Integer.toString(skipped));
        failedTextView.setText(Integer.toString(failed));
        Double score;
        if(successful+failed > 0) score = (double) (1.0 * successful / (successful + failed) * dayEntry.getMood() * 20);
        else score = 0.0;
        scoreTextView.setText(Double.toString(Math.round(score)));

        // pieChart
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(successful, ""));
        entries.add(new PieEntry(skipped, ""));
        entries.add(new PieEntry(failed, ""));
        PieDataSet set = new PieDataSet(entries, "Election Results");
        set.setColors(colors);
        set.setDrawValues(false);
        PieData data = new PieData(set);
        pieChart.setData(data);
        pieChart.invalidate(); // refresh
    }
}
