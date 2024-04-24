package com.example.habittracker.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.habittracker.fragments.DailyStatsFragment;
import com.example.habittracker.fragments.HabitStatsFragment;
import com.example.habittracker.fragments.YearlyStatsFragment;

public class StatsViewPagerAdapter extends FragmentStateAdapter {

    public StatsViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1: return new YearlyStatsFragment();
            case 2: return new HabitStatsFragment();
            default: return new DailyStatsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
