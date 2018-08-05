package com.hustler.quotzy.ui.Services;

import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

/**
 * Created by Sayi on 28-01-2018.
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
public class NotificationCustomListener_Service extends NotificationListenerService {

    public  final String NOTIFICATION_DEBUG_TAG=this.getClass().getSimpleName();
    public static final String NOTIFICATION_TAG ="com.hustler.quote.ui.Services.NotificationCustomListener_Service";

    public static final String NOTIFICATION_EVENT_KEY="notification_key";

    public static final String NOTIFICATION_POSTED_FLAG_VALUE ="Posted";
    public static final String NOTIFICATION_POSTED_REMOVED_FLAG_VALUE ="Removed";


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
        Log.i(NOTIFICATION_DEBUG_TAG, NOTIFICATION_POSTED_FLAG_VALUE);
        Intent intent=new Intent(NOTIFICATION_TAG);
        intent.putExtra(NOTIFICATION_EVENT_KEY, NOTIFICATION_POSTED_FLAG_VALUE);
        sendBroadcast(intent);

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(NOTIFICATION_DEBUG_TAG, NOTIFICATION_POSTED_REMOVED_FLAG_VALUE);

        Intent intent=new Intent(NOTIFICATION_TAG);
        intent.putExtra(NOTIFICATION_EVENT_KEY, NOTIFICATION_POSTED_REMOVED_FLAG_VALUE);
        sendBroadcast(intent);
        stopSelf();
    }
}
