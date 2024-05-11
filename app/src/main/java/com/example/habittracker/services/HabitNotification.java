package com.example.habittracker.services;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.habittracker.R;

import es.dmoral.toasty.Toasty;

public class HabitNotification extends BroadcastReceiver {

    String icon;
    String habit;
    int id;

    @Override
    public void onReceive(Context context, Intent intent) {
        icon = intent.getStringExtra("icon");
        habit = intent.getStringExtra("habit");
        id = intent.getIntExtra("id", 1);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "habit")
                .setSmallIcon(context.getResources().getIdentifier(icon, "drawable", context.getPackageName()))
                .setContentTitle(context.getResources().getString(R.string.notification_habit_title))
                .setContentText(context.getResources().getString(R.string.notification_habit_desc) + " " + habit);

        // Get the NotificationManager service
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);

        // Show the notification using the manager
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Toasty.error(context, context.getResources().getString(R.string.toast_missing_permission), Toast.LENGTH_SHORT, true).show();
            return;
        }
        manager.notify(id, builder.build());
    }
}
