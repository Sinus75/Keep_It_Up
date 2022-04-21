package com.example.keepitup;

import static com.example.keepitup.MainActivity.UpdateCompleted;
import static com.example.keepitup.MainActivity.db;

import android.content.Context;
import android.graphics.Color;
import android.media.metrics.Event;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends Fragment {
    private RecyclerView rvStatistic;
    private HabitStatisticAdapter adapter;
    private List<Habit> habits;
    private HabitDao habitDao;
    private HabitCompletedByDateDao habitCompletedByDateDao;
    private Context context;

    public static StatisticsFragment newInstance() {
        StatisticsFragment fragment = new StatisticsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_statistics, container, false);
        initFields(thisView);
        initAdapters(thisView);
        UpdateCompleted();
        return thisView;
    }

    private void initFields(View thisView) {
        habits = new ArrayList<>();
        rvStatistic = thisView.findViewById(R.id.recyclerView_Habits_Statistic);
        adapter = new HabitStatisticAdapter(habits);
        habitCompletedByDateDao = db.habitCompletedByDateDao();
        habitDao = db.habitDao();
        context = thisView.getContext();
    }

    private void initAdapters(View thisView) {
        for (Habit habit: habitDao.getAll()){
            habits.add(habit);
        }

        rvStatistic.setAdapter(adapter);
        rvStatistic.setLayoutManager(new LinearLayoutManager(context));
    }
}