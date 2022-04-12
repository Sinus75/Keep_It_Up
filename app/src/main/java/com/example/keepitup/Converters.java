package com.example.keepitup;

import android.util.Log;

import androidx.room.TypeConverter;

import java.sql.Time;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Date;

public class Converters {
    @TypeConverter
    public static String daysOfWeekToString(boolean[] daysOfWeek){
        String daysOfWeekString = "";

        for (boolean bool: daysOfWeek) {
            daysOfWeekString+= bool ? "1":"0";
        }

        return daysOfWeekString;
    }

    @TypeConverter
    public static boolean[] daysOfWeekToBoolean(String daysOfWeek){
        boolean[] daysOfWeekBoolean = new boolean[7];
        char[] daysOfWeekChar = daysOfWeek.toCharArray();

        for (int i = 0; i < daysOfWeekBoolean.length; i++) {
            if (daysOfWeekChar[i] == '1') daysOfWeekBoolean[i] = true;
            else daysOfWeekBoolean[i] = false;
        }

        return daysOfWeekBoolean;
    }

    @TypeConverter
    public static long daysOfWeekToBoolean(LocalTime time){
        return time.getLong(ChronoField.SECOND_OF_DAY);
    }

    @TypeConverter
    public static LocalTime daysOfWeekToBoolean(long timeLong){
        return Instant.ofEpochSecond(timeLong)
                .atZone(ZoneId.systemDefault()).toLocalTime();
    }
}
