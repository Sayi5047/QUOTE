package com.hustler.quote.ui.Recievers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.hustler.quote.ui.Services.DailyNotificationService;
import com.hustler.quote.ui.Services.DailyWallpaperService;
import com.hustler.quote.ui.Services.DownloadImageService;
import com.hustler.quote.ui.apiRequestLauncher.Constants;

/**
 * Created by Sayi on 06-02-2018.
 */

public class AlarmReciever extends WakefulBroadcastReceiver {
    Intent downloadIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getBooleanExtra(Constants.ALARM_INTENT__IS_DOWNLOAD_INTENT_FLAG,false)){
            downloadIntent = new Intent(context, DailyWallpaperService.class);

        }else {

        }
        startWakefulService(context, downloadIntent);
        Log.i("WAKEFULRECEIVER","RECEIVED");
    }
}
