package com.example.keepitup;

import static com.example.keepitup.MainActivity.db;
import static com.example.keepitup.MainActivity.settings;

import static java.time.format.SignStyle.NORMAL;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarUtils;
import com.applandeo.materialcalendarview.EventDay;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.datepicker.MaterialCalendar;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
        public com.applandeo.materialcalendarview.CalendarView cvStatistic;

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
        LinearLayout llStatistic                                    = holder.llStatistic;
        ImageView imgIcon                                           = holder.imgIcon;
        ImageView imgOpenClose                                      = holder.imgOpenClose;
        TextView tvName                                             = holder.tvName;
        com.applandeo.materialcalendarview.CalendarView cvStatistic = holder.cvStatistic;

        imgIcon.setImageResource(habit.getImage());
        imgIcon.setColorFilter(habit.getColor());
        tvName.setText(habit.getName());
        llStatistic.setVisibility(View.GONE);

        List<EventDay> eventDays = new ArrayList<>();
        for (HabitCompletedByDate date: habitCompletedByDateDao.loadAllByIds(habit.getId())) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Date.from(LocalDate.ofEpochDay(date.getDate()).atStartOfDay(ZoneId.systemDefault()).toInstant()));

            String result = date.getPercentCompleted() == 0 ? "-" : "+";
            Drawable icon = CalendarUtils.getDrawableText(cvStatistic.getContext(), result,
                    Typeface.defaultFromStyle(Typeface.NORMAL), getColorByPercent(date.getPercentCompleted()), 30);

            EventDay habitDate = new EventDay(calendar, icon);
            eventDays.add(habitDate);
            Log.d("TAG", "onBindViewHolder: " + date.getPercentCompleted() + " " + getColorByPercent(date.getPercentCompleted()));
        }
        cvStatistic.setEvents(eventDays);

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

    }

    public int getColorByPercent(int percent){
        if (percent > 75 ) return R.color.habit_complete;
        if (percent > 50 ) return R.color.habit_good;
        if (percent > 25 ) return R.color.habit_half;
        if (percent > 0 ) return R.color.habit_bad;
        else return R.color.habit_failed;
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }
}
