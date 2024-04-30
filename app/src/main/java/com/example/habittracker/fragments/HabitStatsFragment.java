package com.example.habittracker.fragments;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.habittracker.R;
import com.example.habittracker.activities.CalendarActivity;
import com.example.habittracker.adapters.DatabaseHandler;
import com.example.habittracker.models.DayentryModel;
import com.example.habittracker.models.EntryModel;
import com.example.habittracker.models.HabitModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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
    // year
    int year;
    // colors
    int[] colors;

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
        // year
        year = LocalDate.now().getYear();
        intervalTextView.setText(Integer.toString(year));
        // charts
        yearBarChart = v.findViewById(R.id.yearBarChart);
        Legend legend = yearBarChart.getLegend();
        legend.setEnabled(false);
        Description description = yearBarChart.getDescription();
        description.setEnabled(false);
        yearBarChart.animateY(1000);
        yearBarChart.animateX(1000);
        XAxis xAxis = yearBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        YAxis leftAxis = yearBarChart.getAxisLeft();
        leftAxis.setDrawAxisLine(false);
        YAxis rightAxis = yearBarChart.getAxisRight();
        leftAxis.setTextColor(getResources().getColor(R.color.light_gray));
        rightAxis.setTextColor(getResources().getColor(R.color.light_gray));
        rightAxis.setDrawAxisLine(false);
        xAxis.setTextColor(getResources().getColor(R.color.light_gray));

        monthBarChart = v.findViewById(R.id.monthBarChart);
        Legend legend2 = monthBarChart.getLegend();
        legend2.setEnabled(false);
        Description description2 = monthBarChart.getDescription();
        description2.setEnabled(false);
        monthBarChart.animateY(1000);
        monthBarChart.animateX(1000);
        XAxis xAxis2 = monthBarChart.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setGranularity(1f);
        xAxis2.setDrawAxisLine(false);
        xAxis2.setDrawGridLines(false);
        YAxis leftAxis2 = monthBarChart.getAxisLeft();
        leftAxis2.setDrawAxisLine(false);
        YAxis rightAxis2 = monthBarChart.getAxisRight();
        leftAxis2.setTextColor(getResources().getColor(R.color.light_gray));
        rightAxis2.setTextColor(getResources().getColor(R.color.light_gray));
        rightAxis2.setDrawAxisLine(false);
        xAxis2.setTextColor(getResources().getColor(R.color.light_gray));

        pieChart = v.findViewById(R.id.pieChart);
        colors = new int[]{getResources().getColor(R.color.green), getResources().getColor(R.color.yellow), getResources().getColor(R.color.red)};
        Legend legend3 = pieChart.getLegend();
        legend3.setEnabled(false);
        pieChart.setHoleColor(getResources().getColor(R.color.dark_gray));
        Description description3 = pieChart.getDescription();
        description3.setEnabled(false);
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
        // onClicks
        // year
        leftImageView.setOnClickListener(view -> {
            if(year == LocalDate.now().getYear()) {
                TypedValue typedValue = new TypedValue();
                getContext().getTheme().resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true);
                int c = ContextCompat.getColor(getContext(), typedValue.resourceId);
                rightImageView.setColorFilter(c);
            }
            year--;
            intervalTextView.setText(Integer.toString(year));
            loadData();
        });
        rightImageView.setColorFilter(getResources().getColor(R.color.light_gray));
        rightImageView.setOnClickListener(view -> {
            if(!(year == LocalDate.now().getYear())) {
                year++;
                if(year == LocalDate.now().getYear()) rightImageView.setColorFilter(getResources().getColor(R.color.light_gray));
                intervalTextView.setText(Integer.toString(year));
                loadData();
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
        HashMap<String, Integer> entriesInYear = new HashMap<>();
        HashMap<String, Integer> entriesInMonth = new HashMap<>();
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
                // get the successfull entry numbers by year and by month
                LocalDate date = LocalDate.parse(entriesArrayList.get(i).getDate());
                if(entriesInYear.containsKey(Integer.toString(date.getYear()))) {
                    entriesInYear.put(Integer.toString(date.getYear()), entriesInYear.get(Integer.toString(date.getYear())) + 1);
                } else {
                    entriesInYear.put(Integer.toString(date.getYear()), 1);
                }
                if(year == date.getYear()) {
                    if(entriesInMonth.containsKey(Integer.toString(date.getMonthValue()))) {
                        entriesInMonth.put(Integer.toString(date.getMonthValue()), entriesInMonth.get(Integer.toString(date.getMonthValue())) + 1);
                    } else {
                        entriesInMonth.put(Integer.toString(date.getMonthValue()), 1);
                    }
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

        // yearly entries chart
        ArrayList<BarEntry> entries = new ArrayList<>();
        final ArrayList<String> xAxisLabel = new ArrayList<>();
        float pos = 0f;
        for (String i : entriesInYear.keySet()) {
            entries.add(new BarEntry(pos, entriesInYear.get(i)));
            xAxisLabel.add(i);
            pos++;
        }
        yearBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
        BarDataSet set = new BarDataSet(entries, "yearSet");
        set.setColors(getResources().getColor(R.color.light_green));
        set.setDrawValues(false);
        set.setValueTextColor(getResources().getColor(R.color.light_gray));

        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        yearBarChart.setData(data);
        yearBarChart.setFitBars(true); // make the x-axis fit exactly all bars
        yearBarChart.invalidate(); // refresh

        // monthly entries chart
        ArrayList<BarEntry> entries2 = new ArrayList<>();
        final ArrayList<String> xAxisLabel2 = new ArrayList<>();
        for (String i : entriesInMonth.keySet()) {
            entries2.add(new BarEntry(Float.parseFloat(i)-1, entriesInMonth.get(i)));
        }
        BarDataSet set2 = new BarDataSet(entries2, "monthSet");
        set2.setColors(getResources().getColor(R.color.dark_blue));
        set2.setDrawValues(false);
        set2.setValueTextColor(getResources().getColor(R.color.light_gray));
        xAxisLabel2.add(getResources().getString(R.string.stats_jan));
        xAxisLabel2.add(getResources().getString(R.string.stats_feb));
        xAxisLabel2.add(getResources().getString(R.string.stats_mar));
        xAxisLabel2.add(getResources().getString(R.string.stats_apr));
        xAxisLabel2.add(getResources().getString(R.string.stats_may));
        xAxisLabel2.add(getResources().getString(R.string.stats_jun));
        xAxisLabel2.add(getResources().getString(R.string.stats_jul));
        xAxisLabel2.add(getResources().getString(R.string.stats_aug));
        xAxisLabel2.add(getResources().getString(R.string.stats_sep));
        xAxisLabel2.add(getResources().getString(R.string.stats_oct));
        xAxisLabel2.add(getResources().getString(R.string.stats_nov));
        xAxisLabel2.add(getResources().getString(R.string.stats_dec));
        monthBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabel2));

        BarData data2 = new BarData(set2);
        data2.setBarWidth(0.9f); // set custom bar width
        monthBarChart.setData(data2);
        monthBarChart.setFitBars(true); // make the x-axis fit exactly all bars
        monthBarChart.invalidate(); // refresh

        // pieChart
        ArrayList<PieEntry> entries3 = new ArrayList<>();
        entries3.add(new PieEntry(successful, ""));
        entries3.add(new PieEntry(skipped, ""));
        entries3.add(new PieEntry(failed, ""));
        PieDataSet set3 = new PieDataSet(entries3, "");
        set3.setColors(colors);
        set3.setDrawValues(false);
        PieData data3 = new PieData(set3);
        pieChart.setData(data3);
        pieChart.invalidate(); // refresh
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
                if(streak > longestStreak) longestStreak = streak;
                streak = 0;
            }
        }
        return longestStreak;
    }
}