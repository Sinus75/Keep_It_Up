package com.example.keepitup;

import static com.example.keepitup.MainActivity.UpdateCompleted;
import static com.example.keepitup.MainActivity.db;
import static com.example.keepitup.MainActivity.setNotification;

import android.content.Context;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HabitsFragment extends Fragment {
    private RecyclerView recyclerView;
    private HabitAdapter adapter;
    private List<Habit> habits;
    private FloatingActionButton btnAdd;
    private HabitDao habitDao;
    private Context context;

    public static HabitsFragment newInstance() {
        HabitsFragment fragment = new HabitsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_habits, container, false);

        initFields(thisView);
        initAdapters(thisView);
        UpdateCompleted();

        return thisView;
    }

    private void initFields(View thisView) {
        habits = new ArrayList<>();
        recyclerView = thisView.findViewById(R.id.recyclerView_Habits);
        btnAdd = thisView.findViewById(R.id.button_Add);
        adapter = new HabitAdapter(habits);
        habitDao = db.habitDao();
        context = thisView.getContext();
    }

    private void initAdapters(View thisView) {
        boolean[] daysOfWeek= {true, false, true, false, true, false, true};
        LocalTime time = LocalTime.of(12,30);

        habits.addAll(habitDao.getAll());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        btnAdd.setOnClickListener(view -> {
            Habit newHabit = new Habit("Habit",R.drawable.ic_setting,
                    ContextCompat.getColor(context, R.color.habit_color_cyan),
                    1, daysOfWeek,false, time, 0, 0);
            habitDao.insertHabit(newHabit);
            habits.clear();
            habits.addAll(habitDao.getAll());
            adapter.notifyItemInserted(habits.size());
        });
    }
}