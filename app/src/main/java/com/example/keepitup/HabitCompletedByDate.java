package com.example.keepitup;

import static androidx.room.ForeignKey.NO_ACTION;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(foreignKeys = @ForeignKey(entity = Habit.class, parentColumns = "id", childColumns = "habit_id", onDelete = NO_ACTION))
public class HabitCompletedByDate {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "habit_id")
    public long habit_id;

    @ColumnInfo(name = "dateEpochDay")
    public long dateEpochDay;

    @ColumnInfo(name = "percentCompleted")
    public int percentCompleted;

    public HabitCompletedByDate(long habit_id, long dateEpochDay, int percentCompleted) {
        this.habit_id = habit_id;
        this.dateEpochDay = dateEpochDay;
        this.percentCompleted = percentCompleted;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public long getHabit_id() {
        return habit_id;
    }
    public void setHabit_id(long habit_id) {
        this.habit_id = habit_id;
    }

    public long getDate() {
        return dateEpochDay;
    }
    public void setDate(long dateEpochDay) {
        this.dateEpochDay = dateEpochDay;
    }

    public int getPercentCompleted() {
        return percentCompleted;
    }
    public void setPercentCompleted(int percentCompleted) {
        this.percentCompleted = percentCompleted;
    }
}
