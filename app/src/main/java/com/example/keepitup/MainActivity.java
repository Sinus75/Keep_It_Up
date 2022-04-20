package com.example.keepitup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    public static AppDatabase db;
    public static final String APP_PREFERENCES = "settings", APP_PREFERENCES_DATE = "date",
            APP_PREFERENCES_NOTIFICATIONS = "allowNotifications";
    public static SharedPreferences settings;
    public static SharedPreferences.Editor editor;
    public static HabitDao habitDao;
    public static HabitCompletedByDateDao habitCompletedByDateDao;
    public static List<Habit> habits;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFields();
        UpdateCompleted();
        initAdapters();
        setNotifications(context);

        //Возвращение назад времени
        //LocalDate date = LocalDate.ofEpochDay(todayLong);
        //date.getDayOfWeek();
    }

    private void initFields() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.page_habits);
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "KeepItUpDataBase").allowMainThreadQueries().build();
        settings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        editor = settings.edit();
        habitDao = db.habitDao();
        habitCompletedByDateDao = db.habitCompletedByDateDao();
        habits = new ArrayList();
        context = getApplicationContext();

        if(!settings.contains(APP_PREFERENCES_NOTIFICATIONS))
            editor.putBoolean(APP_PREFERENCES_NOTIFICATIONS, true);
    }

    private void initAdapters() {
        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.page_checklist:
                            item.setChecked(true);
                            loadFragment(ChecklistFragment.newInstance());
                            break;

                        case R.id.page_habits:
                            item.setChecked(true);
                            loadFragment(HabitsFragment.newInstance());
                            break;
                        case R.id.page_statistics:
                            item.setChecked(true);
                            loadFragment(StatisticsFragment.newInstance());
                            break;
                        case R.id.page_setting:
                            item.setChecked(true);
                            loadFragment(SettingsFragment.newInstance());
                            break;
                    }
                    return false;
                });

    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit();
    }

    public static ArrayList<Integer> getIcons() {
        ArrayList<Integer> icons = new ArrayList<>();

        icons.add(R.drawable.ic_habit_alarm);
        icons.add(R.drawable.ic_habit_book);
        icons.add(R.drawable.ic_habit_exercises);
        icons.add(R.drawable.ic_habit_jogging);
        icons.add(R.drawable.ic_habit_meditation);
        icons.add(R.drawable.ic_habit_money);
        icons.add(R.drawable.ic_habit_no_drinking);
        icons.add(R.drawable.ic_habit_no_smoking);
        icons.add(R.drawable.ic_habit_shower);
        icons.add(R.drawable.ic_habit_sleep);
        icons.add(R.drawable.ic_habit_study);
        icons.add(R.drawable.ic_habit_time);
        icons.add(R.drawable.ic_habit_water);

        return icons;
    }

    public static ArrayList<Integer> getColors(Context context) {
        ArrayList<Integer> colors = new ArrayList<>();

        colors.add(ContextCompat.getColor(context, R.color.habit_color_red));
        colors.add(ContextCompat.getColor(context, R.color.habit_color_orange));
        colors.add(ContextCompat.getColor(context, R.color.habit_color_yellow));
        colors.add(ContextCompat.getColor(context, R.color.habit_color_yellow_green));
        colors.add(ContextCompat.getColor(context, R.color.habit_color_green));
        colors.add(ContextCompat.getColor(context, R.color.habit_color_blue_green));
        colors.add(ContextCompat.getColor(context, R.color.habit_color_cyan));
        colors.add(ContextCompat.getColor(context, R.color.habit_color_blue));
        colors.add(ContextCompat.getColor(context, R.color.habit_color_deep_blue));
        colors.add(ContextCompat.getColor(context, R.color.habit_color_purple));
        colors.add(ContextCompat.getColor(context, R.color.habit_color_pink));
        colors.add(ContextCompat.getColor(context, R.color.habit_color_red_pink));

        return colors;
    }

    //С каждым днём обновляет, выполеные привычки и записывает дату в shared preferences
    public static void UpdateCompleted() {
        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        long todayLong = today.toEpochDay();

        if(settings.contains(APP_PREFERENCES_DATE)) {
            if (settings.getLong(APP_PREFERENCES_DATE, 1) != todayLong) {
                editor.putLong(APP_PREFERENCES_DATE, todayLong);
                for (Habit habit: habitDao.getAll()) {
                    int percentCompleted = (int) Math.round(habit.getCompleted()  * 100 / habit.getAmountPerDay());
                    HabitCompletedByDate date = new HabitCompletedByDate(habit.getId(),todayLong, percentCompleted);

                    habitCompletedByDateDao.insertDate(date);
                    habit.setCompleted(0);
                    habitDao.update(habit);

                }
            }
        }
        else {
            editor.putLong(APP_PREFERENCES_DATE, todayLong);
        }
        editor.apply();
    }

    public static void setNotifications(Context context) {
        for (Habit habit : habitDao.getAll()){
            setNotification(habit, context);
        }
    }

    public static void cancelNotifications(Context context){
        for (Habit habit : habitDao.getAll()){
            cancelNotification(habit, context);
        }
    }

    public static void setNotification(Habit habit, Context context){
        LocalDate todayDate = LocalDate.now(ZoneId.systemDefault());
        boolean[] DaysOfWeek = habit.getDaysOfWeek();
        if (habit.notification && DaysOfWeek[todayDate.getDayOfWeek().getValue()-1] && settings.getBoolean(APP_PREFERENCES_NOTIFICATIONS, false)) {
            int id = Math.toIntExact(habit.getId());
            LocalDateTime time = habit.getNotificationTime().atDate(todayDate);
            Intent alarmIntent = new Intent(context, Receiver.class);

            Bundle bundle = new Bundle();
            bundle.putSerializable(Habit.class.getSimpleName(), habit);
            alarmIntent.putExtra("BUNDLE", bundle);

            alarmIntent.putExtra("ID", habit.getId());
            alarmIntent.putExtra("NAME", habit.getName());
            alarmIntent.putExtra("ICON", habit.getImage());
            alarmIntent.putExtra("COLOR", habit.getColor());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            if(time.isBefore(LocalDateTime.now())) time = time.plusDays(1);
            Calendar calendar = Calendar.getInstance();
            //calendar.setTimeInMillis(System.currentTimeMillis());
            //calendar.set(Calendar.YEAR, time.getYear());
            //calendar.set(Calendar.MONTH, time.getMonth().getValue());
            calendar.set(Calendar.DAY_OF_MONTH, time.getDayOfMonth());
            calendar.set(Calendar.HOUR_OF_DAY, time.getHour());
            calendar.set(Calendar.MINUTE, time.getMinute());

            manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Log.d("TAG", "SET - " + habit.getName());
            Log.d("TAG", "TIME_NOW - " + LocalDateTime.now());
            Log.d("TAG", "TIME - " + time);

        }
        else {
            Log.d("TAG", "CANNOT SET - " + habit.getName());
            cancelNotification(habit, context);
        }
    }

    public static void cancelNotification(Habit habit, Context context){
        int id = Math.toIntExact(habit.getId());
        Intent alarmIntent = new Intent(context, Receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        manager.cancel(pendingIntent);
        Log.d("TAG", "CANCEL - " + habit.getName());
    }
}