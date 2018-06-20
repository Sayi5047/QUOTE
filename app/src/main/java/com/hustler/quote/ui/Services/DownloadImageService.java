package com.hustler.quote.ui.Services;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.hustler.quote.R;
import com.hustler.quote.ui.Recievers.NotifcationReciever;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.utils.Toast_Snack_Dialog_Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import static android.os.Build.VERSION_CODES.O;

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
    public File downloading_File;
    ImageDownloader imageDownloader;
    private static final String CHANNEL_ID = "QUOTZY";
    private static final int NOTIFY_ID = 5004;

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

        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= O) {
            CharSequence name = "Iamge Downloader";
            String description = getString(R.string.notication_channel_desc);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(channel);
            mNotification_Builder = new NotificationCompat.Builder(this, CHANNEL_ID);
            setBuilder();

        } else {
            mNotification_Builder = new NotificationCompat.Builder(this);
            setBuilder();

        }

        // Start a image download operation in a background thread
        imageDownloader = new ImageDownloader();
        imageDownloader.execute(url);
// Check for notification settings
//        ContentResolver contentResolver = getContentResolver();
//        String enabledNotifications = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
//        String packageName = getPackageName();
//        if (enabledNotifications == null || (enabledNotifications.contains(packageName) == false)) {
//            Intent intent1 = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
//            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent1);
//        }
//Start the notification recieer
        mNotification_Reciever = new NotifcationReciever();
        IntentFilter filter = new IntentFilter();
        filter.addAction(NotificationCustomListener_Service.NOTIFICATION_TAG);
        getApplicationContext().registerReceiver(mNotification_Reciever, filter);
        return START_REDELIVER_INTENT;

    }

    private void setBuilder() {
        mNotification_Builder.setContentTitle("Downloading images...")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setBadgeIconType(R.drawable.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Download in progress"))
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setProgress(0, 0, true)
                .setAutoCancel(true)
                .setOngoing(true)
        ;
        mNotificationManager.notify(NOTIFY_ID, mNotification_Builder.build());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        killAllNotifs();
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


    public class ImageDownloader extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(final String... params) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    downloading_File = FileUtils.downloadImageToSd_Card(params[0], mFileName + ".jpg", ImageDownloader.this);
//                }
//            }).run();

            FileOutputStream fileOutputStream = null;
            InputStream inputStream = null;

            try {
//                GET URL
                URL url = new URL(params[0]);
//                CRETAE DIRECTORY IN SD CARD WITH GIVEN NAME
                String SdCard = Environment.getExternalStorageDirectory().toString();
                File destination_downloading_directory = new File(Constants.APP_WALLPAPERS_FOLDER);
                if (!destination_downloading_directory.exists()) {
                    destination_downloading_directory.mkdirs();
                }
//                NOW CREATE ONE MORE FILE INSIDE THE DIRECTORY THAT BEEN MADE
                downloading_File = new File(destination_downloading_directory + File.separator + mFileName + ".jpg");
                if (downloading_File.exists()) {
                    downloading_File.delete();

                }

                try {
//                    OPEN A URL CONNECTION AND ATTACH TO HTTPURLCONNECTION
                    URLConnection urlConnection = url.openConnection();
                    HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();
                    int lengthOfFile = httpURLConnection.getContentLength();

//                    GET DATA FROM INPUT STREAM && ATTACH OOUTPUT STREAM OBJECT TO THE FILE TO BE DOWNLOADED FILE OUTPUT STRAM OBJECT
                    inputStream = httpURLConnection.getInputStream();
                    fileOutputStream = new FileOutputStream(downloading_File);
//                    WRITE THE DATA TO BUFFER SO WE CAN COPY EVERYTHING AT ONCE TO MEMORY WHICH IMPROOVES EFFECIANCY
                    byte[] buffer = new byte[2048];
                    int bufferLength = 0;
                    long total = 0;
                    while ((bufferLength = inputStream.read(buffer)) != -1) {
                        total += bufferLength;
//                        mNotification_Builder.setProgress(100, ((int) (total * 100 / lengthOfFile)), false);
//                        mNotificationManager.notify(NOTIFY_ID, mNotification_Builder.build());
                        fileOutputStream.write(buffer, 0, bufferLength);
                        publishProgress(String.valueOf((int) (total * 100 / lengthOfFile)));
                        Log.d("do in BG", String.valueOf((int) (total * 100 / lengthOfFile)));


                    }


                    inputStream.close();
                    fileOutputStream.close();
                    Log.d("IMAGE SAVED", "Image Saved in sd card");

                } catch (IOException e) {
                    inputStream = null;
                    fileOutputStream = null;
                    e.printStackTrace();
                    return null;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("ON POST EXEC", String.valueOf("COMPLETED"));
            Log.d("ON POST EXEC LOC", String.valueOf(FileProvider.getUriForFile(getApplicationContext(), getString(R.string.file_provider_authority), (downloading_File))));
            Intent intent_gallery = new Intent(Intent.ACTION_VIEW, FileProvider.getUriForFile(getApplicationContext(), getString(R.string.file_provider_authority), (downloading_File)));
            intent_gallery.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), NOTIFY_ID, intent_gallery, PendingIntent.FLAG_ONE_SHOT);
            mNotification_Builder.setContentTitle("Completed");
            mNotification_Builder.setContentIntent(pendingIntent);
            mNotification_Builder.setContentText("Images Successfully downloaded to SD card").setProgress(100, 100, false);// Removes the progress bar
            mNotificationManager.notify(NOTIFY_ID, mNotification_Builder.build());
            if (!is_to_set_wallpaper) {
                imageDownloader.cancel(true);
                stopSelf();
            } else {
                if (downloading_File != null) {
                    Intent intent = new Intent(WallpaperManager.getInstance(getApplicationContext()).
                            getCropAndSetWallpaperIntent(FileProvider.getUriForFile(getApplicationContext(), "com.hustler.quote.fileprovider", (downloading_File))));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else {
                    try {
                        Toast_Snack_Dialog_Utils.show_ShortToast((Activity) getApplicationContext(), "Failed to set wallpaper");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                killAllNotifs();
            }


        }


        @Override
        protected void onProgressUpdate(final String... values) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d("PROGRESS", String.valueOf(values[0]));

                    mNotification_Builder.setContentTitle("Downloading");
                    mNotification_Builder.setContentText(values[0] + "% completed").setProgress(100, Integer.valueOf(values[0]), true);
                    // Removes the progress bar
                    mNotificationManager.notify(NOTIFY_ID, mNotification_Builder.build());
                }
            }).start();

            super.onProgressUpdate(values);
        }
    }

}
