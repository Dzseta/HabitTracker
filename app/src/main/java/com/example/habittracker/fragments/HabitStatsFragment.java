package com.example.habittracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.habittracker.R;
import com.example.habittracker.activities.CalendarActivity;
import com.example.habittracker.adapters.DatabaseHandler;
import com.example.habittracker.models.DayentryModel;
import com.example.habittracker.models.EntryModel;
import com.example.habittracker.models.HabitModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

public class HabitStatsFragment extends Fragment {

    // textView
    TextView currentStreakValueTextView;
    TextView bestStreakValueTextView;
    TextView completedStreakValueTextView;
    TextView effectStreakValueTextView;
    TextView averageNumberStreakValueTextView;
    TextView averageTimeValueTextView;
    TextView firstHabitTextView;
    TextView firstHabitValueTextView;
    TextView secondHabitTextView;
    TextView secondHabitValueTextView;
    TextView thirdHabitTextView;
    TextView thirdHabitValueTextView;
    TextView intervalTextView;
    TextView successfulTextView;
    TextView skippedTextView;
    TextView failedTextView;
    TextView pairedWithTextView;
    // ImageViews
    ImageView leftImageView;
    ImageView rightImageView;
    // linearLayout
    LinearLayout averageNumberLinearLayout;
    LinearLayout averageTimeLinearLayout;
    LinearLayout firstLinearLayout;
    LinearLayout secondLinearLayout;
    LinearLayout thirdLinearLayout;
    // charts
    BarChart yearBarChart;
    BarChart monthBarChart;
    PieChart pieChart;
    // spinner
    Spinner habitSpinner;
    // array adapter
    ArrayAdapter<String> adapter;
    // habit's position
    int position;
    // habits arraylist
    ArrayList<HabitModel> habitsArrayList;
    ArrayList<EntryModel> entriesArrayList;
    // habitnames array
    String[] habitNames;
    // database handler
    DatabaseHandler dbHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // initialize
        View v = inflater.inflate(R.layout.fragment_habit_stats, container, false);
        currentStreakValueTextView = v.findViewById(R.id.currentStreakValueTextView);
        bestStreakValueTextView = v.findViewById(R.id.bestStreakValueTextView);
        completedStreakValueTextView = v.findViewById(R.id.completedStreakValueTextView);
        effectStreakValueTextView = v.findViewById(R.id.effectStreakValueTextView);
        averageNumberStreakValueTextView = v.findViewById(R.id.averageNumberStreakValueTextView);
        averageTimeValueTextView = v.findViewById(R.id.averageTimeValueTextView);
        firstHabitTextView = v.findViewById(R.id.firstHabitTextView);
        firstHabitValueTextView = v.findViewById(R.id.firstHabitValueTextView);
        secondHabitTextView = v.findViewById(R.id.secondHabitTextView);
        secondHabitValueTextView = v.findViewById(R.id.secondHabitValueTextView);
        thirdHabitTextView = v.findViewById(R.id.thirdHabitTextView);
        thirdHabitValueTextView = v.findViewById(R.id.thirdHabitValueTextView);
        intervalTextView = v.findViewById(R.id.intervalTextView);
        successfulTextView = v.findViewById(R.id.successfulTextView);
        skippedTextView = v.findViewById(R.id.skippedTextView);
        failedTextView = v.findViewById(R.id.failedTextView);
        pairedWithTextView = v.findViewById(R.id.pairedWithTextView);
        // ImageViews
        leftImageView = v.findViewById(R.id.intervalLeftImageButton);
        rightImageView = v.findViewById(R.id.intervalRightImageButton);
        // linearLayout
        averageNumberLinearLayout = v.findViewById(R.id.averageNumberLinearLayout);
        averageTimeLinearLayout = v.findViewById(R.id.averageTimeLinearLayout);
        firstLinearLayout = v.findViewById(R.id.firstLinearLayout);
        secondLinearLayout = v.findViewById(R.id.secondLinearLayout);
        thirdLinearLayout = v.findViewById(R.id.thirdLinearLayout);
        // charts
        yearBarChart = v.findViewById(R.id.yearBarChart);
        monthBarChart = v.findViewById(R.id.monthBarChart);
        pieChart = v.findViewById(R.id.pieChart);
        // dbHandler
        dbHandler = new DatabaseHandler(getContext());
        // spinner
        habitSpinner = v.findViewById(R.id.habitSpinner);
        // habits
        habitsArrayList = dbHandler.readAllHabits();
        habitNames = new String[habitsArrayList.size()];
        for(int i=0; i<habitsArrayList.size(); i++) {
            habitNames[i] = habitsArrayList.get(i).getName();
        }
        // array adapter
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, habitNames);
        // habit's position
        position = 0;
        //set the spinners adapter to the previously created one.
        habitSpinner.setAdapter(adapter);
        habitSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position2, long id) {
                        position = habitSpinner.getSelectedItemPosition();
                        loadData();
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                        position = 0;
                    }
                });

        loadData();
        // Inflate the layout for this fragment
        return v;
    }

    public void loadData(){
        // get all entries
        entriesArrayList = dbHandler.readAllEntriesByHabit(habitNames[position]);
        // set texts
        int successful = 0, skipped = 0, failed = 0;
        int successMood = 0, successMoodCount = 0, failedMood = 0, failedMoodCount = 0;
        if(entriesArrayList.size() == 0) {
            bestStreakValueTextView.setText("0");
            currentStreakValueTextView.setText("0");
        } else {
            ArrayList<EntryModel> tempArray = (ArrayList<EntryModel>) entriesArrayList.clone();
            bestStreakValueTextView.setText(Integer.toString(longestStreak(tempArray)));
            tempArray = (ArrayList<EntryModel>) entriesArrayList.clone();
            currentStreakValueTextView.setText(Integer.toString(streak(tempArray)));
            for(int i=0; i<entriesArrayList.size(); i++) {
                if(entriesArrayList.get(i).getSuccess() == 1) {
                    successful++;
                    //get effect on mood
                    DayentryModel temp = dbHandler.readDayentryByDate(entriesArrayList.get(i).getDate());
                    if(temp != null) {
                        successMood += temp.getMood();
                        successMoodCount++;
                    }
                }
                else if(entriesArrayList.get(i).getSuccess() == 0) {
                    failed++;
                    //get effect on mood
                    DayentryModel temp = dbHandler.readDayentryByDate(entriesArrayList.get(i).getDate());
                    if(temp != null) {
                        failedMood += temp.getMood();
                        failedMoodCount++;
                    }
                }
                if(entriesArrayList.get(i).getSuccess() == -1) {
                    skipped++;
                }

            }
        }
        completedStreakValueTextView.setText(Integer.toString(successful));
        successfulTextView.setText(Integer.toString(successful));
        skippedTextView.setText(Integer.toString(skipped));
        failedTextView.setText(Integer.toString(failed));
        if(successMoodCount == 0) successMoodCount++;
        if(failedMoodCount == 0) failedMoodCount++;
        effectStreakValueTextView.setText(Double.toString((successMood/successMoodCount) - (failedMood/failedMoodCount)));

        // often paired with
        int[] paired = new int[habitNames.length];
        for(int i=0;i<paired.length;i++) {
            paired[i] = 0;
        }
        for(int i=0; i<entriesArrayList.size(); i++) {
            if(entriesArrayList.get(i).getSuccess() == 1) {
                ArrayList<EntryModel> entriesOnDay = dbHandler.readAllEntriesByDate(entriesArrayList.get(i).getDate());
                for(int j=0; j<entriesOnDay.size(); j++) {
                    if(entriesOnDay.get(j).getSuccess() == 1){
                        for(int k=0; k<paired.length; k++) {
                            if(habitNames[k].equals(entriesOnDay.get(j).getHabit())) paired[k]++;
                        }
                    };
                }
            }
        }
        int first = 0, second = 0, third = 0;
        int fPos = 0, sPos = 0, tPos = 0;
        for(int i=0; i<paired.length; i++) {
            if(i != position && paired[i] >= first) {
                third = second;
                second = first;
                first = paired[i];
                tPos = sPos;
                sPos = fPos;
                fPos = i;
            } else if(i != position && paired[i] >= second) {
                third = second;
                second = paired[i];
                tPos = sPos;
                sPos = i;
            } else if(i != position && paired[i] >= third) {
                third = paired[i];
                tPos = i;
            }
        }
        pairedWithTextView.setVisibility(View.GONE);
        firstLinearLayout.setVisibility(View.GONE);
        secondLinearLayout.setVisibility(View.GONE);
        thirdLinearLayout.setVisibility(View.GONE);
        if(paired.length > 1) {
            firstHabitTextView.setText(habitNames[fPos]);
            firstHabitValueTextView.setText(Integer.toString(first));
            pairedWithTextView.setVisibility(View.VISIBLE);
            firstLinearLayout.setVisibility(View.VISIBLE);
        }
        if(paired.length > 2) {
            secondHabitTextView.setText(habitNames[sPos]);
            secondHabitValueTextView.setText(Integer.toString(second));
            secondLinearLayout.setVisibility(View.VISIBLE);
        }
        if(paired.length > 3) {
            thirdHabitTextView.setText(habitNames[tPos]);
            thirdHabitValueTextView.setText(Integer.toString(third));
            thirdLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    public int streak(ArrayList<EntryModel> entries){
        LocalDate now = LocalDate.now();
        Collections.sort(entries, (first, second) -> second.getDate().compareTo(first.getDate()));

        if(!(entries.get(0).getDate().equals(now.toString()))) {
            now = now.minusDays(1);
        } else if (entries.get(0).getSuccess() != 1) {
            now = now.minusDays(1);
            entries.remove(0);
        }

        int streak = 0;
        for(int i=0; i<entries.size(); i++) {
            LocalDate entryDate = LocalDate.parse(entries.get(i).getDate());
            if(entryDate.isEqual(now) && entries.get(i).getSuccess() == 1) {
                now = now.minusDays(1);
                streak++;
            } else {
                break;
            }
        }
        return streak;
    }

    public int longestStreak(ArrayList<EntryModel> entries){
        LocalDate now = LocalDate.now();
        Collections.sort(entries, (first, second) -> second.getDate().compareTo(first.getDate()));

        if(!(entries.get(0).getDate().equals(now.toString()))) {
            now = now.minusDays(1);
        } else if (entries.get(0).getSuccess() != 1) {
            now = now.minusDays(1);
            entries.remove(0);
        }

        int longestStreak = 0;
        int streak = 0;
        for(int i=0; i<entries.size(); i++) {
            LocalDate entryDate = LocalDate.parse(entries.get(i).getDate());
            if(entryDate.isEqual(now) && entries.get(i).getSuccess() == 1) {
                now = now.minusDays(1);
                streak++;
            } else {
                longestStreak = streak;
                streak = 0;
            }
        }
        return longestStreak;
    }
}