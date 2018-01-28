package com.hustler.quote.ui.Services;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

/**
 * Created by Sayi on 28-01-2018.
 */

@SuppressLint("NewApi")
public class NotificationCustomListener_Service extends NotificationListenerService {

    public  final String NOTIFICATION_DEBUG_TAG=this.getClass().getSimpleName();
    public static final String NOTIFICATION_TAG ="com.hustler.quote.ui.Services.NotificationCustomListener_Service";
    public static final String NOTIFICATION_POSTED_FLAG="Posted";
    public static final String NOTIFICATION_POSTED_REMOVED_FLAG_KEY="Removed";
    public static final String NOTIFICATION_EVENT_KEY="notification_key";


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i(NOTIFICATION_DEBUG_TAG,NOTIFICATION_POSTED_FLAG);
        Intent intent=new Intent(NOTIFICATION_TAG);
        intent.putExtra(NOTIFICATION_EVENT_KEY,NOTIFICATION_POSTED_FLAG);
        sendBroadcast(intent);

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(NOTIFICATION_DEBUG_TAG,NOTIFICATION_POSTED_REMOVED_FLAG_KEY    );

        Intent intent=new Intent(NOTIFICATION_TAG);
        intent.putExtra(NOTIFICATION_EVENT_KEY,NOTIFICATION_POSTED_REMOVED_FLAG_KEY);
        sendBroadcast(intent);
    }
}
