package com.example.keepitup;

import static com.example.keepitup.MainActivity.db;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HabitStatisticAdapter extends
        RecyclerView.Adapter<HabitStatisticAdapter.ViewHolder>{
    private List<Habit> habits;
    private HabitCompletedByDateDao habitCompletedByDateDao = db.habitCompletedByDateDao();

    public HabitStatisticAdapter(List<Habit> habitList){habits = habitList;}

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout llStatistic;
        public ImageView imgIcon, imgOpenClose;
        public TextView tvName;
        public CalendarView cvStatistic;

        public ViewHolder(View itemView) {
            super(itemView);
            llStatistic         = itemView.findViewById(R.id.linearLayout_Statistic);
            imgIcon             = itemView.findViewById(R.id.imageView_Icon_Statistic);
            imgOpenClose        = itemView.findViewById(R.id.imageView_OpenClose_Statistic);
            tvName              = itemView.findViewById(R.id.textView_Name_Statistic);
            cvStatistic         = itemView.findViewById(R.id.calendarView_Statistic);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View thisView = inflater.inflate(R.layout.recycleritem_habit_statistic, parent, false);
        ViewHolder viewHolder = new ViewHolder(thisView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Habit habit = habits.get(position);
        LinearLayout llStatistic = holder.llStatistic;
        ImageView imgIcon        = holder.imgIcon;
        ImageView imgOpenClose   = holder.imgOpenClose;
        TextView tvName          = holder.tvName;
        CalendarView cvStatistic = holder.cvStatistic;

        imgIcon.setImageResource(habit.getImage());
        imgIcon.setColorFilter(habit.getColor());
        tvName.setText(habit.getName());
        llStatistic.setVisibility(View.GONE);

        imgOpenClose.setOnClickListener(view -> {
            if(llStatistic.getVisibility() == View.VISIBLE) {
                llStatistic.setVisibility(View.GONE);
                imgOpenClose.setImageResource(R.drawable.ic_down);
            }
            else {
                llStatistic.setVisibility(View.VISIBLE);
                imgOpenClose.setImageResource(R.drawable.ic_up);
            }
        });

        //for (HabitCompletedByDate date: habitCompletedByDateDao.loadAllByIds(habit.getId())) {
        //    Log.d("TAG", "onBindViewHolder: " + habit.getId() + " " + date.getDate());
        //}
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }
}
