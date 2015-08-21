package barqsoft.footballscores.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

   @Override
   public void onReceive(Context context, Intent intent)
   {
       Log.v("AlarmReceiver","Alarm received.");
       Intent service_start = new Intent(context, myFetchService.class);
       context.startService(service_start);
   }
}
