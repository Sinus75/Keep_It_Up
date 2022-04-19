package com.example.keepitup;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Habit.class, HabitCompletedByDate.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract HabitDao habitDao();
    public abstract HabitCompletedByDateDao habitCompletedByDateDao();
}
