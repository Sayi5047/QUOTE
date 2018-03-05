package com.hustler.quote.ui.Services;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.Service;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.hustler.quote.R;
import com.hustler.quote.ui.Recievers.NotifcationReciever;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.utils.FileUtils;
import com.hustler.quote.ui.utils.Toast_Snack_Dialog_Utils;

import java.io.File;
import java.io.IOException;

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
public class DownloadImageService extends Service {

    android.support.v4.app.NotificationCompat.Builder mNotification_Builder;
    NotificationManager mNotificationManager;
    NotifcationReciever mNotification_Reciever;
    int id = 1;
    int counter = 0;
    String mUrl_To_Download;
    String mFileName;
    boolean is_to_set_wallpaper;
    AsyncTask<String, String, Void> mDownload_Async_Task;
    File downloading_File;
    ImageDownloader imageDownloader;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = null;
//        GET DATA FROM THE INTENT
        url = intent.getStringExtra(Constants.ImageUrl_to_download);
        mFileName = intent.getStringExtra(Constants.Image_Name_to_save_key);
        is_to_set_wallpaper = intent.getBooleanExtra(Constants.is_to_setWallpaper_fromActivity, false);
//        PREPARE notification to build
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotification_Builder = new NotificationCompat.Builder(this);
        mNotification_Builder.setContentTitle("Downloading images...").setContentText("Download in progress").setSmallIcon(R.drawable.ic_launcher);
        mNotification_Builder.setProgress(0, 0, true);
        mNotificationManager.notify(id, mNotification_Builder.build());
        mNotification_Builder.setAutoCancel(true);
        // Start a image download operation in a background thread
        imageDownloader = new ImageDownloader();
        imageDownloader.execute(url);
// Check for notification settings
        ContentResolver contentResolver = getContentResolver();
        String enabledNotifications = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        String packageName = getPackageName();
        if (enabledNotifications == null || (enabledNotifications.contains(packageName) == false)) {
            Intent intent1 = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);
        }
//Start the notification recieer
        mNotification_Reciever = new NotifcationReciever();
        IntentFilter filter = new IntentFilter();
        filter.addAction(NotificationCustomListener_Service.NOTIFICATION_TAG);
        getApplicationContext().registerReceiver(mNotification_Reciever, filter);
        return START_REDELIVER_INTENT;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        killAllNotifs();
        getApplicationContext().unregisterReceiver(mNotification_Reciever);
        stopSelf();
        Log.d("Notification got killed", "KILLED");
    }


    private void killAllNotifs() {
        // TODO: 28-01-2018 DESTROY DOWNLOADING TASK
        imageDownloader.cancel(true);
        mNotificationManager.cancelAll();
        stopSelf();
        Log.d("Notification got killed", "KILLED");

    }


    class ImageDownloader extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(final String... params) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    downloading_File = FileUtils.downloadImageToSd_Card(params[0], mFileName + ".jpg");
                }
            }).run();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mNotification_Builder.setContentTitle("Completed");
            mNotification_Builder.setContentText("Images Successfully downloaded to SD card").setProgress(100, 100, false);// Removes the progress bar

            mNotificationManager.notify(id, mNotification_Builder.build());
            if (is_to_set_wallpaper == false) {
                imageDownloader.cancel(true);
                stopSelf();
            } else {
                if (downloading_File != null) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        Intent intent = new Intent(WallpaperManager.getInstance(getApplicationContext()).
                                getCropAndSetWallpaperIntent(FileUtils.getImageContentUri(getApplicationContext(), downloading_File)));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                        try {
                            wallpaperManager.setBitmap(BitmapFactory.decodeFile((downloading_File).getAbsolutePath()));
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("WALLPAPEREXCEPTION", "WALLPAPER NOT FOUND");
                        }
                    }

                } else {
                    Toast_Snack_Dialog_Utils.show_ShortToast((Activity) getApplicationContext(), "Failed to set wallpaper");
                }
                killAllNotifs();

            }


        }

        @Override
        protected void onProgressUpdate(String... values) {
            mNotification_Builder.setContentTitle("Downloading");
            mNotification_Builder.setContentText("Downloaded" + values[0]).setProgress(100, Integer.valueOf(values[0]), false);
            // Removes the progress bar

            mNotificationManager.notify(id, mNotification_Builder.build());
            super.onProgressUpdate(values);
        }
    }

}
