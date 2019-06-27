package io.hustler.qtzy.ui.Recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import io.hustler.qtzy.ui.Services.DailyNotificationJobService;

import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

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
public class DailyNotificationAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(@NonNull Context context, Intent intent) {
        startWakefulService(context, new Intent(context, DailyNotificationJobService.class));
        Log.i("NOTIF RECIVER", "RECEIVED");
    }
}
