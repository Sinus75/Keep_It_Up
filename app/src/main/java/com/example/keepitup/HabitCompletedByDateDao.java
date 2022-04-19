package com.example.keepitup;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HabitCompletedByDateDao {
    @Query("SELECT * FROM habitcompletedbydate")
    List<HabitCompletedByDate> getAll();

    @Query("SELECT * FROM habitcompletedbydate WHERE habit_id IN (:habitId)")
    List<HabitCompletedByDate> loadAllByIds(long habitId);

    @Insert
    void insertAll(List<HabitCompletedByDate> habitsCompletedByDates);

    @Update
    void update(HabitCompletedByDate habitCompletedByDate);

    @Insert
    long insertDate(HabitCompletedByDate habitCompletedByDate);

    @Delete
    void delete(HabitCompletedByDate habitCompletedByDate);
}
