package com.example.keepitup;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class HabitStatisticAdapterTest {
    @Test
    public void getColorByPercentTest_GetFailedColor_Correct(){
        int percentCompleted = 0;
        int color = HabitStatisticAdapter.getColorByPercent(percentCompleted);
        assertEquals(R.color.habit_failed, color);
    }

    @Test
    public void getColorByPercentTest_GetFailedColor_Incorrect(){
        int percentCompleted = 1;
        int color = HabitStatisticAdapter.getColorByPercent(percentCompleted);
        assertNotEquals(R.color.habit_failed, color);
    }

    @Test
    public void getColorByPercentTest_GetBadColor_Correct(){
        int percentCompleted = 25;
        int color = HabitStatisticAdapter.getColorByPercent(percentCompleted);
        assertEquals(R.color.habit_bad, color);
    }

    @Test
    public void getColorByPercentTest_GetBadColor_Incorrect(){
        int percentCompleted = 0;
        int color = HabitStatisticAdapter.getColorByPercent(percentCompleted);
        assertNotEquals(R.color.habit_bad, color);
    }

    @Test
    public void getColorByPercentTest_GetHalfColor_Correct(){
        int percentCompleted = 50;
        int color = HabitStatisticAdapter.getColorByPercent(percentCompleted);
        assertEquals(R.color.habit_half, color);
    }

    @Test
    public void getColorByPercentTest_GetHalfColor_Incorrect(){
        int percentCompleted = 25;
        int color = HabitStatisticAdapter.getColorByPercent(percentCompleted);
        assertNotEquals(R.color.habit_half, color);
    }

    @Test
    public void getColorByPercentTest_GetGoodColor_Correct(){
        int percentCompleted = 75;
        int color = HabitStatisticAdapter.getColorByPercent(percentCompleted);
        assertEquals(R.color.habit_good, color);
    }

    @Test
    public void getColorByPercentTest_GetGoodColor_Incorrect(){
        int percentCompleted = 50;
        int color = HabitStatisticAdapter.getColorByPercent(percentCompleted);
        assertNotEquals(R.color.habit_good, color);
    }

    @Test
    public void getColorByPercentTest_GetCompletedColor_Correct(){
        int percentCompleted = 100;
        int color = HabitStatisticAdapter.getColorByPercent(percentCompleted);
        assertEquals(R.color.habit_complete, color);
    }

    @Test
    public void getColorByPercentTest_GetCompletedColor_Incorrect(){
        int percentCompleted = 75;
        int color = HabitStatisticAdapter.getColorByPercent(percentCompleted);
        assertNotEquals(R.color.habit_complete, color);
    }
}