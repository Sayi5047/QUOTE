package com.hustler.quote.ui.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hustler.quote.R;
import com.hustler.quote.ui.Recievers.AlarmReciever;

/**
 * Created by anvaya5 on 06/02/2018.
 */

public class ProfeaturesActivity extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public SharedPreferences sharedPreferences;
    Intent alarm_intent;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pro_features_layout);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

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
            if (val == true) {
                alarm_intent = new Intent(getApplicationContext(), AlarmReciever.class);
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarm_intent, 0);
                alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime(),
                        sharedPreferences.getInt(getString(R.string.DWL_key2), 60 * 1000),
                        pendingIntent);
                Log.i("ALARM SET", "SET");
            } else {
                alarm_intent = new Intent(getApplicationContext(), AlarmReciever.class);
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarm_intent, 0);
                alarmManager.cancel(pendingIntent);
                Log.i("ALARM Canceled", "Cancel");
            }
        } else if (key.equals(getString(R.string.DNT_key1))) {
            // TODO: 06/02/2018 needs implementation of this
        }
    }
}
