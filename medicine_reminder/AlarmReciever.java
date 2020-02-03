package com.example.medicine_reminder;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import static androidx.legacy.content.WakefulBroadcastReceiver.startWakefulService;

public class AlarmReciever extends BroadcastReceiver {
    private Ringtone ringtone;

    @Override
    public void onReceive(final Context context, Intent intent) {
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();

        Bundle extras = intent.getExtras();
        Intent i = new Intent(context, AlarmService.class);
        i.putExtra("ringtone", alarmUri);
        i.putExtra("med_name", extras.getString("med_name"));
        i.putExtra("dosage", extras.getString("dosage"));
        i.putExtra("medId", extras.getString("medId"));

        context.startService(i);
        ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }

    public void onDestroy()
    {
        ringtone.stop();
    }
}