package com.example.keepitup;

import android.graphics.drawable.Drawable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.security.auth.callback.Callback;

@Entity
public class Habit implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "image")
    public int image;

    @ColumnInfo(name = "color")
    public int color;

    @ColumnInfo(name = "amountPerDay")
    public int amountPerDay;

    @ColumnInfo(name = "daysOfWeek")
    public boolean[] daysOfWeek;

    @ColumnInfo(name = "notification")
    public boolean notification;

    @ColumnInfo(name = "notificationTime")
    public LocalTime notificationTime;

    @ColumnInfo(name = "completed")
    public int completed;

    public Habit(String name, int image, int color, int amountPerDay, boolean[] daysOfWeek,
                 boolean notification, LocalTime notificationTime, int completed) {
        this.name = name;
        this.image = image;
        this.color = color;
        this.amountPerDay = amountPerDay;
        this.daysOfWeek = daysOfWeek;
        this.notification = notification;
        this.notificationTime = notificationTime;
        this.completed = completed;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }
    public void setImage(int image) {
        this.image = image;
    }

    public int getColor() {
        return color;
    }
    public void setColor(int color) {
        this.color = color;
    }

    public int getAmountPerDay() {
        return amountPerDay;
    }
    public void setAmountPerDay(int amountPerDay) {
        this.amountPerDay = amountPerDay;
    }

    public boolean[] getDaysOfWeek() {
        return daysOfWeek;
    }
    public void setDaysOfWeek(boolean[] daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public boolean isNotification() {
        return notification;
    }
    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public LocalTime getNotificationTime() {
        return notificationTime;
    }
    public void setNotificationTime(LocalTime notificationTime) {
        this.notificationTime = notificationTime;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public int getCompleted() {
        return completed;
    }
    public void setCompleted(int completed) {
        this.completed = completed;
    }
}
