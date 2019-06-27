package io.hustler.qtzy.ui.activities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.util.Calendar;

import io.hustler.qtzy.R;
import io.hustler.qtzy.ui.Recievers.DailyNotificationAlarmReceiver;
import io.hustler.qtzy.ui.Services.JobServices.WallaperFirebaseJobService;
import io.hustler.qtzy.ui.apiRequestLauncher.Constants;
import io.hustler.qtzy.ui.apiRequestLauncher.Shared_prefs_constants;
import io.hustler.qtzy.ui.utils.Toast_Snack_Dialog_Utils;

/**
 * Created by anvaya5 on 06/02/2018.
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
public class SettingsActivity extends SecondActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public SharedPreferences sharedPreferences;
    Intent alarm_intent, notif_alarm_intent;
    PendingIntent pendingIntent, notif_pending_intent;
    @Nullable
    AlarmManager alarmManager;
    @Nullable
    JobScheduler mJobScheduler;
    FirebaseJobDispatcher firebaseJobDispatcher;
    Driver driver;
    NotificationManager mNotificationManager;

    private static int BUNDLENOTIFICATIONID = 5005;
    private static int SINGLENOTIFICATIONID = 5005;
    private String CHANNEL_ID = "DAILY_WALL_CHANNEL";
    private String BUNDLECHANNEL_ID = "BUNDLE_DAILY_WALL_CHANNEL";
    private String GROUP_ID = "DAILY_WALL_GROUP";
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pro_features_layout);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        mJobScheduler = (JobScheduler) getApplicationContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slideup, R.anim.slidedown);

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
    public void onSharedPreferenceChanged(@NonNull SharedPreferences sharedPreferences, @NonNull String key) {
        if (key.equals(getString(R.string.DAILY_WALLS_ACTIVATED))) {
            scheduleWallJob(sharedPreferences, sharedPreferences.getBoolean(key, false));
        } else if (key.equals(getString(R.string.DWL_NETWORK_TYPE)) || (key.equals(getString(R.string.DWL_SERVICE_FREQUENCY))) || (key.equals(getString(R.string.DWL_WALL_CATEGORY)))) {
            if (sharedPreferences.getBoolean(getString(R.string.DAILY_WALLS_ACTIVATED), false)) {
                scheduleWallJob(sharedPreferences, false);
                scheduleWallJob(sharedPreferences, true);
            }
        } else if (key.equals(getString(R.string.DNT_key1))) {
            if (sharedPreferences.getBoolean(getString(R.string.DNT_key1), false)) {
                startAlarmForNotifications();
                Log.i("ALARM NOTIF SET", "SET");
            } else {
                stopAlarmForNotifications();
                Log.i("ALARM NOTIF CANCEL", "CANCEL");

            }
        }
    }


    private void scheduleWallJob(SharedPreferences sharedPreferences, boolean isActivated) {
        driver = new GooglePlayDriver(this);
        firebaseJobDispatcher = new FirebaseJobDispatcher(driver);

        if (isActivated) {
            Toast_Snack_Dialog_Utils.show_ShortToast(this, "Service Deactivated");
            editor.putBoolean(Constants.DAILY_WALLS_ACTIVATED, false).apply();
            firebaseJobDispatcher.cancel(getString(R.string.WALLPAPER_JOB_TAG));

        } else {
            Toast_Snack_Dialog_Utils.show_ShortToast(this, "Service Activated");
            editor.putBoolean(Constants.DAILY_WALLS_ACTIVATED, true).apply();
            Job wallJob = firebaseJobDispatcher.newJobBuilder().setService(WallaperFirebaseJobService.class)
                    .setLifetime(Lifetime.FOREVER)
                    .setRecurring(true)
                    .setTrigger(Trigger.executionWindow(0, (sharedPreferences.getInt(getString(R.string.DWL_SERVICE_FREQUENCY), 60000))))
                    .setReplaceCurrent(true)
                    .setTag(getString(R.string.WALLPAPER_JOB_TAG))
                    .setConstraints(sharedPreferences.getBoolean(getString(R.string.DWL_NETWORK_TYPE), true) ? Constraint.ON_UNMETERED_NETWORK : Constraint.ON_ANY_NETWORK)
                    .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL).build();
            firebaseJobDispatcher.mustSchedule(wallJob);


        }
    }


    private void startAlarmForNotifications() {
        notif_alarm_intent = new Intent(getApplicationContext(), DailyNotificationAlarmReceiver.class);
        notif_alarm_intent.putExtra(Constants.ALARM_INTENT__IS_DOWNLOAD_INTENT_FLAG, false);
        notif_pending_intent = PendingIntent.getBroadcast(getApplicationContext(), 1, notif_alarm_intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 1);
        assert alarmManager != null;
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                notif_pending_intent);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Shared_prefs_constants.SHARED_PREFS_NOTIFICATION_SERVICES_RUNNING_KEY, true);
        editor.apply();
    }

    private void stopAlarmForNotifications() {
        notif_alarm_intent = new Intent(getApplicationContext(), DailyNotificationAlarmReceiver.class);
        notif_pending_intent = PendingIntent.getBroadcast(getApplicationContext(), 1, notif_alarm_intent, 0);
        assert alarmManager != null;
        alarmManager.cancel(notif_pending_intent);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Shared_prefs_constants.SHARED_PREFS_NOTIFICATION_SERVICES_RUNNING_KEY, false);
        editor.apply();
    }


    private void setBuilder(NotificationManager mNotificationManager, NotificationCompat.Builder mNotification_builder, String bundleNotificationId, int notificationID) {
        Notification notification = mNotification_builder
                .setContentTitle("Changed Wallpaper...")
                .setContentText("Wallpaper changed")
                .setGroup(GROUP_ID)
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setBadgeIconType(R.drawable.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("New Wallpaper applied"))
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent))
                .setGroup(bundleNotificationId)
                .setGroupSummary(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(false).build();
        mNotificationManager.notify(notificationID, notification);

    }

}

