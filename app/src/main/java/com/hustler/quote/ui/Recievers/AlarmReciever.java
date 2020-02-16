package com.hustler.quote.ui.Recievers;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.legacy.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.Services.DailyWallpaperService;

/**
 * Created by Sayi on 06-02-2018.
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
public class AlarmReciever extends WakefulBroadcastReceiver {
    Intent downloadIntent;

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        if(intent.getBooleanExtra(Constants.ALARM_INTENT__IS_DOWNLOAD_INTENT_FLAG,false)){
            downloadIntent = new Intent(context, DailyWallpaperService.class);

        }else {

        }
        startWakefulService(context, downloadIntent);
        Log.i("WAKEFULRECEIVER","RECEIVED");
    }
}
