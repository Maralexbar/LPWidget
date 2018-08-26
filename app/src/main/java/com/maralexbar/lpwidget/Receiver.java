package com.maralexbar.lpwidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;



public class Receiver extends BroadcastReceiver {




    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            AlarmManager am =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(context, Updater.class);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 1, pi);

        }


        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        // Put here YOUR code.

        wl.release();

        //Intent myIntent = new Intent(context, Updater.class);
        //context.startService(myIntent);


        throw new UnsupportedOperationException("Not yet implemented");
    }





}
