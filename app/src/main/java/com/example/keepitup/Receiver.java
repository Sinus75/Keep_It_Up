package com.example.keepitup;

import static com.example.keepitup.MainActivity.APP_PREFERENCES;
import static com.example.keepitup.MainActivity.APP_PREFERENCES_NOTIFICATIONS;
import static com.example.keepitup.MainActivity.habitDao;
import static com.example.keepitup.MainActivity.setNotification;
import static com.example.keepitup.MainActivity.settings;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Receiver extends BroadcastReceiver {
    public Receiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle arguments = intent.getBundleExtra("BUNDLE");
        Habit habit = (Habit) arguments.getSerializable(Habit.class.getSimpleName());
        settings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        long id = habit.getId();
        String name = habit.getName();
        int icon = habit.getImage();
        int color = habit.getColor();

        Log.d("TAG", "RECEIVE - " + name + " " + id);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, String.valueOf(id));
        Intent ii = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, Math.toIntExact(id), ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(context.getResources().getText(R.string.habit_notification_text));
        bigText.setBigContentTitle(name);
        bigText.setSummaryText("Text in detail");

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(icon);
        mBuilder.setColor(color);
        mBuilder.setContentTitle(name);
        mBuilder.setContentText(context.getResources().getText(R.string.habit_notification_text));
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = String.valueOf(id);
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    habit.getName(),
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        mNotificationManager.notify(Math.toIntExact(id), mBuilder.build());
        setNotification(habit, context);
    }
}
