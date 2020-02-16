package com.hustler.quote.ui.activities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.apiRequestLauncher.Shared_prefs_constants;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.hustler.quote.R;
import com.hustler.quote.ui.Recievers.DailyNotificationAlarmReceiver;
import com.hustler.quote.ui.Services.JobServices.WallaperFirebaseJobService;
import com.hustler.quote.ui.utils.TextUtils;
import com.hustler.quote.ui.utils.Toast_Snack_Dialog_Utils;

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
public class SettingsActivity extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private final String TAG = this.getClass().getSimpleName();
    public SharedPreferences sharedPreferences;
    Intent notif_alarm_intent;
    PendingIntent notif_pending_intent;
    @Nullable
    AlarmManager alarmManager;
    @Nullable
    JobScheduler mJobScheduler;
    FirebaseJobDispatcher firebaseJobDispatcher;
    Driver driver;
    @BindView(R.id.header_name)
    TextView headerNae;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ctl)
    CollapsingToolbarLayout ctl;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
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
        ButterKnife.bind(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        setCollapsingToolbar("Settings", ctl);
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

    protected void setCollapsingToolbar(final String title, final CollapsingToolbarLayout ctl) {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(getApplicationContext(), (R.drawable.ic_keyboard_backspace_white_24dp)));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        final int color1 = TextUtils.getMatColor(this, "mdcolor_800");
        final int color2 = TextUtils.getMatColor(this, "mdcolor_800");
        int[] colors = {color1, color2};
        final GradientDrawable gradientDrawable;
        gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        gradientDrawable.setGradientRadius(135);
        getWindow().setStatusBarColor(color1);
        ctl.setBackground(gradientDrawable);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        TextUtils.setFont_For_Ctl(ctl, SettingsActivity.this, title);

        if (ColorUtils.calculateLuminance(color2) > 0.5) {
            ctl.setExpandedTitleColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            ctl.setCollapsedTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            headerNae.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            Objects.requireNonNull(toolbar.getNavigationIcon()).setTint(getResources().getColor(R.color.colorAccent));

        } else {
            ctl.setExpandedTitleColor(ContextCompat.getColor(getApplicationContext(), R.color.WHITE));
            ctl.setCollapsedTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.WHITE));
            headerNae.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.WHITE));
            Objects.requireNonNull(toolbar.getNavigationIcon()).setTint(getResources().getColor(R.color.WHITE));


        }
        ctl.setCollapsedTitleTypeface(Typeface.createFromAsset(this.getResources().getAssets(), Constants.FONT_CIRCULAR));
        ctl.setExpandedTitleTypeface(Typeface.createFromAsset(this.getResources().getAssets(), Constants.FONT_CIRCULAR));
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollrange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollrange == -1) {
                    scrollrange = appBarLayout.getTotalScrollRange();
                }
                float val = Math.abs(Float.valueOf(new DecimalFormat("0.00").format((float) verticalOffset / scrollrange)));
                int color = ColorUtils.blendARGB(color1, color2, val);
                getWindow().setStatusBarColor(color);
                if (val < 0.65) {
                    ctl.setBackgroundColor(color);
                } else {
                    ctl.setBackground(gradientDrawable);
                }
                ctl.setBackground(gradientDrawable);
                if (scrollrange + verticalOffset == 0) {
                    ctl.setTitleEnabled(false);
                    headerNae.setVisibility(View.VISIBLE);
                    ctl.setBackgroundColor(color);

                } else {

                    ctl.setTitleEnabled(true);
                    headerNae.setVisibility(View.GONE);


                }
            }
        });


    }

    @Override
    public void onSharedPreferenceChanged(@NonNull SharedPreferences sharedPreferences, @NonNull String key) {
        if (key.equals(getString(R.string.DAILY_WALLS_ACTIVATED))) {
            /*IF STARTED THEN SEND ISTOSTOP FALSE AND VICE VERSA*/
            scheduleWallJob(sharedPreferences, !sharedPreferences.getBoolean(getString(R.string.DAILY_WALLS_ACTIVATED), false));
        } else if (key.equals(getString(R.string.DWL_CATEGORIES)) || (key.equals(getString(R.string.DWL_SERVICE_FREQUENCY))) || (key.equals(getString(R.string.DWL_WIFI_ENABLE)))) {
            /*IF ANY PARAMETER CHANGED CHECK IF JOB SCHEDULED AND IF YES THEN STOP THE JOB AND START THE JOB. IF NOT SCHEDULED JUST IGNORE NEXT WHEN THE JOB IS SCHEDULED IT WILL WOK*/
            if (sharedPreferences.getBoolean(getString(R.string.DAILY_WALLS_ACTIVATED), false)) {
                scheduleWallJob(sharedPreferences, true);
                scheduleWallJob(sharedPreferences, false);
            }
        } else {
            if (key.equals(getString(R.string.DNT_key1))) {
                if (sharedPreferences.getBoolean(getString(R.string.DNT_key1), false)) {
                    startAlarmForNotifications();
                    Log.i("ALARM NOTIF SET", "SET");
                } else {
                    stopAlarmForNotifications();
                    Log.i("ALARM NOTIF CANCEL", "CANCEL");

                }
            }
        }
    }


    private void scheduleWallJob(SharedPreferences sharedPreferences, boolean isToStopJob) {
        driver = new GooglePlayDriver(this);
        firebaseJobDispatcher = new FirebaseJobDispatcher(driver);

        if (isToStopJob) {
            firebaseJobDispatcher.cancel(getString(R.string.WALLPAPER_JOB_TAG));
            Toast_Snack_Dialog_Utils.show_ShortToast(this, "JOB Deactivated");
            editor.putInt(Constants.currentWallpaperIndexSharedPreferenceKey, 0);
            editor.putString(Constants.savedImagesJsonResponseforDailyWallpapers, null);

        } else {
            Job wallJob = (firebaseJobDispatcher.newJobBuilder()
                    .setService(WallaperFirebaseJobService.class)
                    .setLifetime(Lifetime.FOREVER)
                    .setRecurring(true)
                    .setTrigger(Trigger.executionWindow(0, Integer.parseInt(((sharedPreferences.getString(getString(R.string.DWL_SERVICE_FREQUENCY), String.valueOf(TimeUnit.HOURS.toSeconds(12))))))))
                    .setReplaceCurrent(true)
                    .setTag(getString(R.string.WALLPAPER_JOB_TAG)))
                    .setConstraints(sharedPreferences.getBoolean(getString(R.string.DWL_WIFI_ENABLE), true) ? Constraint.ON_UNMETERED_NETWORK : Constraint.ON_ANY_NETWORK)
                    .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL).build();
            firebaseJobDispatcher.mustSchedule(wallJob);
            editor.putBoolean(Constants.DAILY_WALLS_ACTIVATED, true).apply();
            Toast_Snack_Dialog_Utils.show_ShortToast(this, "JOB Activated");
            Log.i(TAG, "scheduleWallJob: JOB ACTIVATED");
            Log.i(TAG, "scheduleWallJob: " + sharedPreferences.getBoolean(getString(R.string.DWL_WIFI_ENABLE), true));
            Log.i(TAG, "scheduleWallJob: " + "TIME WINDOW" + Integer.parseInt(((sharedPreferences.getString(getString(R.string.DWL_SERVICE_FREQUENCY), String.valueOf(TimeUnit.HOURS.toMillis(12)))))));
            System.out.println("scheduleWallJob: " + "TIME WINDOW" + Integer.parseInt(((sharedPreferences.getString(getString(R.string.DWL_SERVICE_FREQUENCY), String.valueOf(TimeUnit.HOURS.toMillis(12)))))));
        }
    }


    private void startAlarmForNotifications() {
        notif_alarm_intent = new Intent(getApplicationContext(), DailyNotificationAlarmReceiver.class);
        notif_alarm_intent.putExtra(Constants.ALARM_INTENT__IS_DOWNLOAD_INTENT_FLAG, false);
        notif_pending_intent = PendingIntent.getBroadcast(getApplicationContext(), 1, notif_alarm_intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 27);
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

