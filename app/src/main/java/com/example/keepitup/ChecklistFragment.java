package com.example.keepitup;

import static com.example.keepitup.MainActivity.UpdateCompleted;
import static com.example.keepitup.MainActivity.db;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class ChecklistFragment extends Fragment {
    private RecyclerView rvChecklist;
    private HabitCheckAdapter adapter;
    private List<Habit> habits;
    private HabitDao habitDao;
    private Context context;

    public static ChecklistFragment newInstance() {
        ChecklistFragment fragment = new ChecklistFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_checklist, container, false);

        initFields(thisView);
        initAdapters(thisView);
        UpdateCompleted();

        return thisView;
    }

    private void initFields(View thisView) {
        habits = new ArrayList<>();
        rvChecklist = thisView.findViewById(R.id.recyclerView_Checklist);
        adapter = new HabitCheckAdapter(habits);
        habitDao = db.habitDao();
        context = thisView.getContext();
    }

    private void initAdapters(View thisView) {
        LocalDate todayDate = LocalDate.now(ZoneId.systemDefault());
        for (Habit habit: habitDao.getAll()){
            boolean[] DaysOfWeek = habit.getDaysOfWeek();
            if (DaysOfWeek[todayDate.getDayOfWeek().getValue()-1]) habits.add(habit);
        }

        rvChecklist.setAdapter(adapter);
        rvChecklist.setLayoutManager(new LinearLayoutManager(context));
    }
}