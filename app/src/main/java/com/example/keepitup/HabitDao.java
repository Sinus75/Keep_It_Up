package com.example.keepitup;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HabitDao {
    @Query("SELECT * FROM habit")
    List<Habit> getAll();

    @Query("SELECT * FROM habit WHERE id IN (:habitsId)")
    List<Habit> loadAllByIds(int[] habitsId);

    @Query("SELECT * FROM habit WHERE id IN (:habitsId)")
    Habit getHabitById(int habitsId);

    @Insert
    void insertAll(List<Habit> habits);

    @Update
    void update(Habit habit);

    @Insert
    long insertHabit(Habit habit);

    @Delete
    void delete(Habit Habit);
}
