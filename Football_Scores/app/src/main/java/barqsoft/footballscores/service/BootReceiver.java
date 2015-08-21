package barqsoft.footballscores.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by htan on 21/08/2015.
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            // Set the alarm here.
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0 , alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            am.cancel(pi);
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, AlarmManager.INTERVAL_HOUR, pi);
        }
    }
}