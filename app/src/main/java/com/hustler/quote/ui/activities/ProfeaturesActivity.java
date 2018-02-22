package com.hustler.quote.ui.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.hustler.quote.R;
import com.hustler.quote.ui.Recievers.AlarmReciever;
import com.hustler.quote.ui.Recievers.NotifAlarmReciever;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.apiRequestLauncher.Shared_prefs_constants;

/**
 * Created by anvaya5 on 06/02/2018.
 */

public class ProfeaturesActivity extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public SharedPreferences sharedPreferences;
    Intent alarm_intent, notif_alarm_intent;
    PendingIntent pendingIntent, notif_pending_intent;
    AlarmManager alarmManager;
    JobScheduler mJobScheduler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pro_features_layout);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        mJobScheduler = (JobScheduler) getApplicationContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.DWL_key1))) {
            boolean val = sharedPreferences.getBoolean(key, false);
            if (val) {
                startAlarm(sharedPreferences);

            } else {
                stopAlarm(sharedPreferences);

            }
        } else if (key.equals(getString(R.string.DWL_key3))) {
            if (sharedPreferences.getBoolean(getString(R.string.DWL_key1), false)) {
                stopAlarm(sharedPreferences);
                startAlarm(sharedPreferences);
            } else {

            }
        } else if (key.equals(getString(R.string.DNT_key1))) {
            // TODO: 06/02/2018 needs implementation of this
            if (sharedPreferences.getBoolean(getString(R.string.DNT_key1), false)) {
                startAlarmForNotifications();
                Log.i("ALARM NOTIF SET", "SET");

//                JobInfo.Builder builder = new JobInfo.Builder(1,
//                        new ComponentName(getPackageName(), DailyNotificationService.class.getName()));
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
////                    builder.setPeriodic(15 * 60 * 1000, 5 * 60 *1000);
//                    builder.setPeriodic(5000);
//
//                }else {
//                    builder.setPeriodic(5000);
//                }
//                int val=mJobScheduler.schedule(builder.build());
//                if (val <= JobScheduler.RESULT_FAILURE) {
//                    //If something goes wrong
//                    Toast_Snack_Dialog_Utils.show_ShortToast(ProfeaturesActivity.this,"FAILED");
//                }else if(val==JobScheduler.RESULT_SUCCESS){
//                    Toast_Snack_Dialog_Utils.show_ShortToast(ProfeaturesActivity.this,"SUCCESS");
//
//                }
            } else {
                stopAlarmForNotifications();
                Log.i("ALARM NOTIF CANCEL", "CANCEL");

            }
        }
    }


    private void startAlarm(SharedPreferences sharedPreferences) {
        alarm_intent = new Intent(getApplicationContext(), AlarmReciever.class);
        alarm_intent.putExtra(Constants.ALARM_INTENT__IS_DOWNLOAD_INTENT_FLAG, true);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarm_intent, 0);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                Integer.parseInt(sharedPreferences.getString(getString(R.string.DWL_key2), String.valueOf(7200000))),
                pendingIntent);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Shared_prefs_constants.SHARED_PREFS_IMAGE_SERVICES_RUNNING_KEY, true);
        editor.apply();
        Log.i("ALARM SET", "SET");
        Log.i("ALARM TIME", sharedPreferences.getString(getString(R.string.DWL_key2), String.valueOf(7200000)));
    }

    private void stopAlarm(SharedPreferences sharedPreferences) {
        alarm_intent = new Intent(getApplicationContext(), AlarmReciever.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarm_intent, 0);
        alarmManager.cancel(pendingIntent);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.Shared_prefs_loaded_images_for_service_key, null);
        editor.putInt(Constants.Shared_prefs_current_service_image_key, 0);
        editor.putInt(Constants.Shared_prefs_current_service_image_Size_key, 0);
        editor.putBoolean(Shared_prefs_constants.SHARED_PREFS_IMAGE_SERVICES_RUNNING_KEY, false);
        editor.apply();
        Log.i("ALARM Canceled", "Cancel");
    }


    private void startAlarmForNotifications() {
        notif_alarm_intent = new Intent(getApplicationContext(), NotifAlarmReciever.class);
        notif_alarm_intent.putExtra(Constants.ALARM_INTENT__IS_DOWNLOAD_INTENT_FLAG, false);
        notif_pending_intent = PendingIntent.getBroadcast(getApplicationContext(), 1, notif_alarm_intent, 0);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                (60 * 1000),
                notif_pending_intent);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Shared_prefs_constants.SHARED_PREFS_NOTIFICATION_SERVICES_RUNNING_KEY, true);
        editor.apply();
    }

    private void stopAlarmForNotifications() {
        notif_alarm_intent = new Intent(getApplicationContext(), AlarmReciever.class);
        notif_pending_intent = PendingIntent.getBroadcast(getApplicationContext(), 1, notif_alarm_intent, 0);
        alarmManager.cancel(notif_pending_intent);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Shared_prefs_constants.SHARED_PREFS_NOTIFICATION_SERVICES_RUNNING_KEY, false);
        editor.apply();
    }
}

