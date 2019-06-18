package io.hustler.qtzy.ui.utils;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;

public class NotificationUtils {
    private Context context;
    NotificationManager notificationManager;
    NotificationChannel notificationChannel;
    NotificationChannelGroup notificationChannelGroup;

    public NotificationUtils(Context context, NotificationManager notificationManager, NotificationChannel notificationChannel, NotificationChannelGroup notificationChannelGroup) {
        this.context = context;
        this.notificationManager = notificationManager;
        this.notificationChannel = notificationChannel;
        this.notificationChannelGroup = notificationChannelGroup;
    }
}
