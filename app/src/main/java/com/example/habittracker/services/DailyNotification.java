package com.example.habittracker.services;

import android.Manifest;
import android.app.NotificationManager;
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

// BroadcastReceiver for handling notifications
public class DailyNotification extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "daily")
                .setSmallIcon(R.drawable.icon_clock)
                .setContentTitle(context.getResources().getString(R.string.notification_daily_title))
                .setContentText(context.getResources().getString(R.string.notification_daily_desc));

        // Get the NotificationManager service
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);

        // Show the notification using the manager
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Toasty.error(context, context.getResources().getString(R.string.toast_missing_permission), Toast.LENGTH_SHORT, true).show();
            return;
        }
        manager.notify(1, builder.build());
    }
}