package com.hustler.quote.ui.Recievers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.hustler.quote.ui.Services.DailyNotificationService;

/**
 * Created by Sayi on 22-02-2018.
 */

public class NotifAlarmReciever extends WakefulBroadcastReceiver {
    Intent downloadIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        downloadIntent = new Intent(context, DailyNotificationService.class);
        startWakefulService(context, downloadIntent);
        Log.i("NOTIF RECIVER", "RECEIVED");
    }
}
