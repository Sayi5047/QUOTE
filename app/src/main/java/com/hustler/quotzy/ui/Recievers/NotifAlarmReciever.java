package com.hustler.quotzy.ui.Recievers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.hustler.quotzy.ui.Services.DailyNotificationService;

/**
 * Created by Sayi on 22-02-2018.
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
public class NotifAlarmReciever extends WakefulBroadcastReceiver {
    Intent downloadIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        downloadIntent = new Intent(context, DailyNotificationService.class);
        startWakefulService(context, downloadIntent);
        Log.i("NOTIF RECIVER", "RECEIVED");
    }
}
