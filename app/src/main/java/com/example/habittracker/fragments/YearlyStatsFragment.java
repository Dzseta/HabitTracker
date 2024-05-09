package com.example.habittracker.fragments;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habittracker.R;
import com.example.habittracker.adapters.DatabaseHandler;
import com.example.habittracker.adapters.HabitNumberAdapter;
import com.example.habittracker.models.DayentryModel;
import com.example.habittracker.models.EntryModel;
import com.example.habittracker.models.HabitModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.time.LocalDate;
import java.util.ArrayList;

public class YearlyStatsFragment extends Fragment {

    // TextViews
    TextView dateTextView;
    TextView intervalTextView;
    TextView moodValueTextVIew;
    TextView verySadTextVIew;
    TextView sadTextVIew;
    TextView neutralTextVIew;
    TextView happyTextVIew;
    TextView veryHappyTextVIew;
    TextView commentsTextView;
    TextView firstHabitTextView;
    TextView firstHabitValueTextView;
    TextView secondHabitTextView;
    TextView secondHabitValueTextView;
    TextView thirdHabitTextView;
    TextView thirdHabitValueTextView;
    TextView lastHabitTextView;
    TextView lastHabitValueTextView;
    TextView secondLastHabitTextView;
    TextView secondLastHabitValueTextView;
    TextView thirdLastHabitTextView;
    TextView thirdLastHabitValueTextView;
    // ImageViews
    ImageView leftImageView;
    ImageView rightImageView;
    ImageView intervalLeftImageView;
    ImageView intervalRightImageView;
    // linearlayouts
    LinearLayout mostTextView;
    LinearLayout leastTextView;
    LinearLayout firstLinearLayout;
    LinearLayout secondLinearLayout;
    LinearLayout thirdLinearLayout;
    LinearLayout lastLinearLayout;
    LinearLayout secondLastLinearLayout;
    LinearLayout thirdLastLinearLayout;
    // date
    LocalDate now;
    // charts
    BarChart barChart;
    // recycleView
    RecyclerView habitsRecycleView;
    HabitNumberAdapter habitNumberAdapter;
    // database
    DatabaseHandler dbHandler;
    ArrayList<HabitModel> habitArrayList;
    ArrayList<DayentryModel> dayentryArrayList;
    ArrayList<EntryModel> entryArrayList;
    ArrayList<Integer> numberArrayList;
    int interval;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // initialize
        View v = inflater.inflate(R.layout.fragment_yearly_stats, container, false);
        interval = 0;
        dateTextView = v.findViewById(R.id.dateTextView);
        intervalTextView = v.findViewById(R.id.intervalTextView);
        moodValueTextVIew = v.findViewById(R.id.moodValueTextView);
        verySadTextVIew = v.findViewById(R.id.verySadTextView);
        sadTextVIew = v.findViewById(R.id.sadTextView);
        neutralTextVIew = v.findViewById(R.id.neutralTextView);
        happyTextVIew = v.findViewById(R.id.happyTextView);
        veryHappyTextVIew = v.findViewById(R.id.veryHappyTextView);
        commentsTextView = v.findViewById(R.id.commentsTextView);
        firstHabitTextView = v.findViewById(R.id.firstHabitTextView);
        firstHabitValueTextView = v.findViewById(R.id.firstHabitValueTextView);
        secondHabitTextView = v.findViewById(R.id.secondHabitTextView);
        secondHabitValueTextView = v.findViewById(R.id.secondHabitValueTextView);
        thirdHabitTextView = v.findViewById(R.id.thirdHabitTextView);
        thirdHabitValueTextView = v.findViewById(R.id.thirdHabitValueTextView);
        lastHabitTextView = v.findViewById(R.id.lastHabitTextView);
        lastHabitValueTextView = v.findViewById(R.id.lastHabitValueTextView);
        secondLastHabitTextView = v.findViewById(R.id.secondLastHabitTextView);
        secondLastHabitValueTextView = v.findViewById(R.id.secondLastHabitValueTextView);
        thirdLastHabitTextView = v.findViewById(R.id.thirdLastHabitTextView);
        thirdLastHabitValueTextView = v.findViewById(R.id.thirdLastHabitValueTextView);
        mostTextView = v.findViewById(R.id.mostLinearLayout);
        leastTextView = v.findViewById(R.id.leastLinearLayout);
        // ImageViews
        leftImageView = v.findViewById(R.id.leftImageButton);
        rightImageView = v.findViewById(R.id.rightImageButton);
        intervalLeftImageView = v.findViewById(R.id.intervalLeftImageButton);
        intervalRightImageView = v.findViewById(R.id.intervalRightImageButton);
        // linearLayouts
        firstLinearLayout = v.findViewById(R.id.firstLinearLayout);
        secondLinearLayout = v.findViewById(R.id.secondLinearLayout);
        thirdLinearLayout = v.findViewById(R.id.thirdLinearLayout);
        lastLinearLayout = v.findViewById(R.id.lastLinearLayout);
        secondLastLinearLayout = v.findViewById(R.id.secondLastLinearLayout);
        thirdLastLinearLayout = v.findViewById(R.id.thirdLastLinearLayout);
        // charts
        barChart = v.findViewById(R.id.barChart);
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);
        Description description = barChart.getDescription();
        description.setEnabled(false);
        barChart.animateY(1000);
        barChart.animateX(1000);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawAxisLine(false);
        leftAxis.setTextColor(getResources().getColor(R.color.light_gray));
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(5f);
        leftAxis.setGranularity(1f);
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);
        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add(getResources().getString(R.string.new_habit_monday));
        xAxisLabel.add(getResources().getString(R.string.new_habit_tuesday));
        xAxisLabel.add(getResources().getString(R.string.new_habit_wednesday));
        xAxisLabel.add(getResources().getString(R.string.new_habit_thursday));
        xAxisLabel.add(getResources().getString(R.string.new_habit_friday));
        xAxisLabel.add(getResources().getString(R.string.new_habit_saturday));
        xAxisLabel.add(getResources().getString(R.string.new_habit_sunday));
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
        xAxis.setTextColor(getResources().getColor(R.color.light_gray));

        // recycleView
        habitsRecycleView = v.findViewById(R.id.habitsRecyclerView);
        // date
        now = LocalDate.now();

        // database
        dbHandler = new DatabaseHandler(getContext());
        habitArrayList = dbHandler.readAllHabits();
        numberArrayList = new ArrayList<>();

        // adapter
        habitNumberAdapter = new HabitNumberAdapter(habitArrayList, numberArrayList, getContext());
        // setting layout manager for recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        habitsRecycleView.setLayoutManager(linearLayoutManager);
        // setting adapter to recycler view
        habitsRecycleView.setAdapter(habitNumberAdapter);

        // load data
        loadData();

        // onClicks
        // year
        leftImageView.setOnClickListener(view -> {
            if(interval == 0){
                if(now.getYear() == LocalDate.now().getYear()) {
                    TypedValue typedValue = new TypedValue();
                    getContext().getTheme().resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true);
                    int c = ContextCompat.getColor(getContext(), typedValue.resourceId);
                    rightImageView.setColorFilter(c);
                }
                now = now.minusYears(1);
            } else if(interval == 1) {
                if(now.getYear() == LocalDate.now().getYear() && now.getMonthValue() == LocalDate.now().getMonthValue()) {
                    TypedValue typedValue = new TypedValue();
                    getContext().getTheme().resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true);
                    int c = ContextCompat.getColor(getContext(), typedValue.resourceId);
                    rightImageView.setColorFilter(c);
                }
                now = now.minusMonths(1);
            }
            loadData();
        });
        rightImageView.setColorFilter(getResources().getColor(R.color.light_gray));
        rightImageView.setOnClickListener(view -> {
            if(interval == 0) {
                if(!(now.getYear() == LocalDate.now().getYear())) {
                    now = now.plusYears(1);
                    if(now.getYear() == LocalDate.now().getYear()) rightImageView.setColorFilter(getResources().getColor(R.color.light_gray));
                }
            } else if(interval == 1) {
                if(!((now.getYear() == LocalDate.now().getYear()) && now.getMonthValue() == LocalDate.now().getMonthValue())) {
                    now = now.plusMonths(1);
                    if(now.getYear() == LocalDate.now().getYear() && now.getMonthValue() == LocalDate.now().getMonthValue()) rightImageView.setColorFilter(getResources().getColor(R.color.light_gray));
                }
            }
            loadData();
        });
        // interval
        intervalLeftImageView.setColorFilter(getResources().getColor(R.color.light_gray));
        intervalLeftImageView.setOnClickListener(view -> {
            if(interval == 1) {
                interval--;
                intervalLeftImageView.setColorFilter(getResources().getColor(R.color.light_gray));
                TypedValue typedValue = new TypedValue();
                getContext().getTheme().resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true);
                int c = ContextCompat.getColor(getContext(), typedValue.resourceId);
                intervalRightImageView.setColorFilter(c);
                loadData();
            }
        });
        intervalRightImageView.setOnClickListener(view -> {
            if(interval == 0) {
                interval++;
                intervalRightImageView.setColorFilter(getResources().getColor(R.color.light_gray));
                TypedValue typedValue = new TypedValue();
                getContext().getTheme().resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true);
                int c = ContextCompat.getColor(getContext(), typedValue.resourceId);
                intervalLeftImageView.setColorFilter(c);
                loadData();
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    public void loadData() {
        // set date to text
        String s, e;
        if(interval == 0) {
            dateTextView.setText(Integer.toString(now.getYear()));
            intervalTextView.setText(getResources().getString(R.string.stats_year));
            s = now.getYear() + "-01-01";
            e = now.plusYears(1).getYear() + "-01-01";
        } else {
            dateTextView.setText(Integer.toString(now.getYear()) + "-" + Integer.toString(now.getMonthValue()));
            intervalTextView.setText(getResources().getString(R.string.stats_month));
            if(now.getMonthValue() < 9) {
                s = now.getYear() + "-0" + now.getMonthValue() + "-01";
                e = now.getYear() + "-0" + now.plusMonths(1).getMonthValue() + "-01";
            } else if(now.getMonthValue() == 9){
                s = now.getYear() + "-0" + now.getMonthValue() + "-01";
                e = now.getYear() + "-" + now.plusMonths(1).getMonthValue() + "-01";
            } else {
                s = now.getYear() + "-" + now.getMonthValue() + "-01";
                e = now.plusMonths(1).getYear() + "-" + now.plusMonths(1).getMonthValue() + "-01";
            }
        }
        // load from database
        dayentryArrayList = dbHandler.readAllDayentriesInRange(s, e);
        habitArrayList = dbHandler.readAllHabitsInRange(s, e);
        // mood and comment
        double moodAvg = 0;
        int verySad = 0, sad = 0, neutral = 0, happy = 0, veryHappy = 0, comments = 0;
        for(int i=0; i<dayentryArrayList.size(); i++) {
            moodAvg += dayentryArrayList.get(i).getMood();
            switch (dayentryArrayList.get(i).getMood()){
                case 1:
                    verySad++;
                    break;
                case 2:
                    sad++;
                    break;
                case 3:
                    neutral++;
                    break;
                case 4:
                    happy++;
                    break;
                case 5:
                    veryHappy++;
                    break;
            }
            if(!dayentryArrayList.get(i).getComment().equals("")) comments++;
        }
        moodValueTextVIew.setText(String.valueOf(moodAvg / dayentryArrayList.size()));
        verySadTextVIew.setText(Integer.toString(verySad));
        sadTextVIew.setText(Integer.toString(sad));
        neutralTextVIew.setText(Integer.toString(neutral));
        happyTextVIew.setText(Integer.toString(happy));
        veryHappyTextVIew.setText(Integer.toString(veryHappy));
        commentsTextView.setText(Integer.toString(comments));
        // habits
        numberArrayList = new ArrayList<>();
        for(int i=0; i<habitArrayList.size(); i++) {
            int num = 0;
            entryArrayList = dbHandler.readAllEntriesByHabitInRange(habitArrayList.get(i).getName(), s, e);
            for(int j=0; j<entryArrayList.size(); j++) {
                if(entryArrayList.get(j).getSuccess() == 1) num++;
            }
            numberArrayList.add(num);
        }
        mostTextView.setVisibility(View.GONE);
        leastTextView.setVisibility(View.GONE);
        firstLinearLayout.setVisibility(View.GONE);
        secondLinearLayout.setVisibility(View.GONE);
        thirdLinearLayout.setVisibility(View.GONE);
        lastLinearLayout.setVisibility(View.GONE);
        secondLastLinearLayout.setVisibility(View.GONE);
        thirdLastLinearLayout.setVisibility(View.GONE);
        int first = 0, second = 0, third = 0;
        int fPos = 0, sPos = 0, tPos = 0;
        for(int i=0; i<numberArrayList.size(); i++) {
            if(numberArrayList.get(i) >= first) {
                third = second;
                second = first;
                first = numberArrayList.get(i);
                tPos = sPos;
                sPos = fPos;
                fPos = i;
            } else if(numberArrayList.get(i) >= second) {
                third = second;
                second = numberArrayList.get(i);
                tPos = sPos;
                sPos = i;
            } else if(numberArrayList.get(i) >= third) {
                third = numberArrayList.get(i);
                tPos = i;
            }
        }
        if(numberArrayList.size() > 0) {
            firstHabitTextView.setText(habitArrayList.get(fPos).getName());
            firstHabitValueTextView.setText(Integer.toString(first));
            mostTextView.setVisibility(View.VISIBLE);
            firstLinearLayout.setVisibility(View.VISIBLE);
        }
        if(numberArrayList.size() > 1) {
            secondHabitTextView.setText(habitArrayList.get(sPos).getName());
            secondHabitValueTextView.setText(Integer.toString(second));
            secondLinearLayout.setVisibility(View.VISIBLE);
        }
        if(numberArrayList.size() > 2) {
            thirdHabitTextView.setText(habitArrayList.get(tPos).getName());
            thirdHabitValueTextView.setText(Integer.toString(third));
            thirdLinearLayout.setVisibility(View.VISIBLE);
        }

        int last = Integer.MAX_VALUE, secondLast = Integer.MAX_VALUE, thirdLast = Integer.MAX_VALUE;
        int lPos = 0, slPos = 0, tlPos = 0;
        for(int i=0; i<numberArrayList.size(); i++) {
            if(numberArrayList.get(i) <= last) {
                thirdLast = secondLast;
                secondLast = last;
                last = numberArrayList.get(i);
                tlPos = slPos;
                slPos = lPos;
                lPos = i;
            } else if(numberArrayList.get(i) <= secondLast) {
                thirdLast = secondLast;
                secondLast = numberArrayList.get(i);
                tlPos = slPos;
                slPos = i;
            } else if(numberArrayList.get(i) <= thirdLast) {
                thirdLast = numberArrayList.get(i);
                tlPos = i;
            }
        }
        if(numberArrayList.size() > 0) {
            lastHabitTextView.setText(habitArrayList.get(lPos).getName());
            lastHabitValueTextView.setText(Integer.toString(last));
            leastTextView.setVisibility(View.VISIBLE);
            lastLinearLayout.setVisibility(View.VISIBLE);
        }
        if(numberArrayList.size() > 1) {
            secondLastHabitTextView.setText(habitArrayList.get(slPos).getName());
            secondLastHabitValueTextView.setText(Integer.toString(secondLast));
            secondLastLinearLayout.setVisibility(View.VISIBLE);
        }
        if(numberArrayList.size() > 2) {
            thirdLastHabitTextView.setText(habitArrayList.get(tlPos).getName());
            thirdLastHabitValueTextView.setText(Integer.toString(thirdLast));
            thirdLastLinearLayout.setVisibility(View.VISIBLE);
        }
        habitNumberAdapter = new HabitNumberAdapter(habitArrayList, numberArrayList, getContext());
        habitsRecycleView.setAdapter(habitNumberAdapter);
        habitNumberAdapter.notifyDataSetChanged();

        // charts
        int[] sum = {0, 0, 0, 0, 0, 0, 0};
        int[] count = {0, 0, 0, 0, 0, 0, 0};
        for(int i=0; i<dayentryArrayList.size(); i++) {
            if(dayentryArrayList.get(i).getMood() != 0) {
                int day = LocalDate.parse(dayentryArrayList.get(i).getDate()).getDayOfWeek().getValue()-1;
                sum[day] += dayentryArrayList.get(i).getMood();
                count[day]++;
            }
        }
        for(int i=0; i<7; i++) {
            if(count[i] == 0) count[i]++;
        }
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, (float) sum[0] /count[0]));
        entries.add(new BarEntry(1f, (float) sum[1]/count[1]));
        entries.add(new BarEntry(2f, (float) sum[2]/count[2]));
        entries.add(new BarEntry(3f, (float) sum[3]/count[3]));
        entries.add(new BarEntry(4f, (float) sum[4]/count[4]));
        entries.add(new BarEntry(5f, (float) sum[5]/count[5]));
        entries.add(new BarEntry(6f, (float) sum[6]/count[6]));
        BarDataSet set = new BarDataSet(entries, "BarDataSet");
        set.setColors(getResources().getColor(R.color.green));
        set.setDrawValues(false);
        set.setValueTextColor(getResources().getColor(R.color.light_gray));

        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        barChart.setData(data);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars
        barChart.invalidate(); // refresh
    }
}
