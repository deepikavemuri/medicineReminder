package com.example.medicine_reminder;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

@TargetApi(Build.VERSION_CODES.O)
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)


public class AlarmService extends IntentService {
    private static final int NOTIFICATION_ID = 3;

    public AlarmService() {
        super("AlarmService");
    }

    NotificationManager alarmNotificationManager = null;
    Bundle extras;
    @Override
    public void onHandleIntent(Intent intent) {
        extras = intent.getExtras();

        sendNotification("Time to take your medicine!", extras.getString("med_name"), extras.getString("dosage"));
    }

    private void sendNotification(String msg, String med_name, String dosage) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        //intent.putExtra("ringtone", extras.get("ringtone"));
        String CHANNEL_ID = "MYCHANNEL";
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "name", NotificationManager.IMPORTANCE_LOW);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, 0);
        Notification notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle("Time to take your medicine!")
                .setContentText("Take " + dosage + " of " + med_name)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.pills, "Go to app", pendingIntent)
                .setChannelId(CHANNEL_ID)
                .setSmallIcon(R.drawable.pills)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notify(1, notification);
    }
}