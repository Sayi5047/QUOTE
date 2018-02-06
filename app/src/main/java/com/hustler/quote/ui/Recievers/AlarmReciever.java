package com.hustler.quote.ui.Recievers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.hustler.quote.ui.Services.DailyWallpaperService;
import com.hustler.quote.ui.Services.DownloadImageService;

/**
 * Created by Sayi on 06-02-2018.
 */

public class AlarmReciever extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent downloadIntent = new Intent(context, DailyWallpaperService.class);
        startWakefulService(context, downloadIntent);
        Log.i("WAKEFULRECEIVER","RECEIVED");
    }
}
