package io.hustler.qtzy.ui.Recievers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Sayi on 26-01-2018.
 */

/*   Copyright [2018] [Sayi Manoj Sugavasi]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.*/
public class SystemBootReciever extends BroadcastReceiver {
    AlarmManager alarmManager;

    Intent alarm_intent, notif_alarm_intent;
    PendingIntent pendingIntent, notif_pending_intent;

    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.d("BOOT RECEIDEVR","RECIEVED");
//        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        if (!sharedPreferences.getBoolean(Shared_prefs_constants.SHARED_PREFS_NOTIFICATION_SERVICES_RUNNING_KEY, false)) {
//            Calendar calendar=Calendar.getInstance();
//            calendar.setTimeInMillis(System.currentTimeMillis());
//            calendar.set(Calendar.HOUR_OF_DAY,10);
//            calendar.set(Calendar.MINUTE,30);
//            notif_alarm_intent = new Intent(context, DailyNotificationAlarmReceiver.class);
//            notif_alarm_intent.putExtra(Constants.ALARM_INTENT__IS_DOWNLOAD_INTENT_FLAG, false);
//            notif_pending_intent = PendingIntent.getBroadcast(context, 1, notif_alarm_intent, 0);
//            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
//                    calendar.getTimeInMillis(),
//                    1 * 24 * 60 * 60 * 1000,
//                    notif_pending_intent);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putBoolean(Shared_prefs_constants.SHARED_PREFS_NOTIFICATION_SERVICES_RUNNING_KEY, true);
//            editor.apply();
//            Log.i("ALARM  NOTIF BOOT R", "SET");
//
//        }
//        if (sharedPreferences.getBoolean(Shared_prefs_constants.SHARED_PREFS_IMAGE_SERVICES_RUNNING_KEY, false)) {
//            alarm_intent = new Intent(context, AlarmReciever.class);
//            alarm_intent.putExtra(Constants.ALARM_INTENT__IS_DOWNLOAD_INTENT_FLAG, true);
//            pendingIntent = PendingIntent.getBroadcast(context, 0, alarm_intent, 0);
//            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                    SystemClock.elapsedRealtime(),
//                    Integer.parseInt(sharedPreferences.getString(context.getString(R.string.DWL_key2), String.valueOf(7200000))),
//                    pendingIntent);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putBoolean(Shared_prefs_constants.SHARED_PREFS_IMAGE_SERVICES_RUNNING_KEY, true);
//            editor.apply();
//            Log.i("ALARM  BOOT RECIEVER", "SET");
//            Log.i("ALARM TIME  RECIEVER", sharedPreferences.getString(context.getString(R.string.DWL_key2), String.valueOf(7200000)));
//        }
    }
//    public void onReceive(Context context, Intent intent) {
////        Toast_Snack_Dialog_Utils.show_ShortToast(QuoteWidgetConfigurationActivity.this, "DEVICE SUCCESSFULLY BOOTED");
//
//        if (sharedPreferences.getBoolean(context.getString(R.string.widget_added_key), false) == true) {
//            Log.d("PREFERNCE LOADED", String.valueOf(sharedPreferences.getBoolean(context.getString(R.string.widget_added_key), false)));
////            startServices();
//        } else {
//
//        }
//    }
}