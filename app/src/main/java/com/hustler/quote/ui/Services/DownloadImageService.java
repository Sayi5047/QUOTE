package com.hustler.quote.ui.Services;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.Service;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.hustler.quote.R;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.utils.FileUtils;
import com.hustler.quote.ui.utils.Toast_Snack_Dialog_Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Sayi on 28-01-2018.
 */

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

    public class NotifcationReciever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String evet = intent.getStringExtra(NotificationCustomListener_Service.NOTIFICATION_EVENT_KEY);
            Log.i(" NOTIFICATIOn DETAILS", evet);
            if (evet.trim().equalsIgnoreCase(NotificationCustomListener_Service.NOTIFICATION_POSTED_REMOVED_FLAG_VALUE)) {
                killAllNotifs();
            }

        }
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
                    downloadImageToSd_Card(params[0], mFileName + ".jpg");

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

    private void downloadImageToSd_Card(String param, String s) {
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;

        try {
//                GET URL
            URL url = new URL(param);
//                CRETAE DIRECTORY IN SD CARD WITH GIVEN NAME
            String SdCard = Environment.getExternalStorageDirectory().toString();
            File tibe_Downloaded_Directory = new File(SdCard + File.separator + Constants.APPFOLDER + Constants.Wallpapers);
            if (tibe_Downloaded_Directory.exists() == false) {
                tibe_Downloaded_Directory.mkdirs();
            }
            String fileName = s;
//                NOW CREATE ONE MORE FILE INSIDE THE DIRECTORY THAT BEEN MADE
            downloading_File = new File(tibe_Downloaded_Directory + File.separator + fileName);
            if (downloading_File.exists()) {
                downloading_File.delete();
            }
            try {
//                    OPEN A URL CONNECTION AND ATTACH TO HTTPURLCONNECTION
                URLConnection urlConnection = url.openConnection();
                HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                int length = httpURLConnection.getContentLength();

//                    GET DATA FROM INPUT STREAM && ATTACH OOUTPUT STREAM OBJECT TO THE FILE TO BE DOWNLOADED FILE OUTPUT STRAM OBJECT
                inputStream = httpURLConnection.getInputStream();
                fileOutputStream = new FileOutputStream(downloading_File);
//                    WRITE THE DATA TO BUFFER SO WE CAN COPY EVERYTHING AT ONCE TO MEMORY WHICH IMPROOVES EFFECIANCY
                byte[] buffer = new byte[2048];
                int bufferLength = 0;
                int manoj = 0;
                while ((bufferLength = inputStream.read(buffer)) > 0) {

                    fileOutputStream.write(buffer, 0, bufferLength);
                    manoj++;

                }
                inputStream.close();
                fileOutputStream.close();
                Log.d("IMAGE SAVED", "Image Saved in sd card");
            } catch (IOException e) {
                inputStream = null;
                fileOutputStream = null;
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }
}
