package com.hustler.quote.ui.Recievers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hustler.quote.ui.Services.NotificationCustomListener_Service;

/**
 * Created by anvaya5 on 23/02/2018.
 */

public class NotifcationReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String evet = intent.getStringExtra(NotificationCustomListener_Service.NOTIFICATION_EVENT_KEY);
        Log.i(" NOTIFICATIOn DETAILS", evet);
        if (evet.trim().equalsIgnoreCase(NotificationCustomListener_Service.NOTIFICATION_POSTED_REMOVED_FLAG_VALUE)) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancelAll();
        }

    }
}
