package io.hustler.qtzy.ui.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

import io.hustler.qtzy.R;
import io.hustler.qtzy.ui.Executors.AppExecutor;
import io.hustler.qtzy.ui.apiRequestLauncher.Constants;

import static android.os.Build.VERSION_CODES.O;
import static io.hustler.qtzy.ui.utils.FileUtils.getImageContent;

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
public class DownloadImageJobService extends JobService {
    private final String TAG = this.getClass().getSimpleName();
    private Task task;

    @Override
    public boolean onStartJob(@NonNull final com.firebase.jobdispatcher.JobParameters jobParameters) {
        Log.i(TAG, "onStartJob: JOB STARTED");
        // Start a image download operation in a background thread
        task = new Task(jobParameters, this);
        task.execute();
        return true;
    }

    private static class Task extends AsyncTask<String, String, Void> {
        JobParameters jobParameters;
        DownloadImageJobService downloadImageJobService;

        Task(JobParameters jobParameters, DownloadImageJobService downloadImageJobService) {
            this.jobParameters = jobParameters;
            this.downloadImageJobService = downloadImageJobService;
        }

        @Override
        protected Void doInBackground(String... strings) {
            AppExecutor.getInstance().getNetworkExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    android.support.v4.app.NotificationCompat.Builder mNotification_Builder;
                    Notification notification;
                    @Nullable
                    NotificationManager mNotificationManager;
                    String mFileName;
                    Boolean is_to_set_wallpaper;
                    File downloading_File;
                    final String CHANNEL_ID = "QUOTZY";
                    final int NOTIFY_ID = 5004;
                    final FileOutputStream[] fileOutputStream = new FileOutputStream[1];
                    final InputStream[] inputStream = new InputStream[1];
                    final String url;
                    url = Objects.requireNonNull(jobParameters.getExtras()).getString(Constants.ImageUrl_to_download);
                    mFileName = jobParameters.getExtras().getString(Constants.Image_Name_to_save_key);
                    is_to_set_wallpaper = (Boolean) jobParameters.getExtras().get(Constants.is_to_setWallpaper_fromActivity);

                    mNotificationManager = (NotificationManager) downloadImageJobService.getSystemService(Context.NOTIFICATION_SERVICE);

                    if (Build.VERSION.SDK_INT >= O) {
                        CharSequence name = "Image Downloader";
                        String description = downloadImageJobService.getString(R.string.notication_channel_desc);
                        int importance = NotificationManager.IMPORTANCE_HIGH;
                        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                        channel.setDescription(description);
                        channel.setSound(null, null);
                        assert mNotificationManager != null;
                        mNotificationManager.createNotificationChannel(channel);
                        mNotification_Builder = new NotificationCompat.Builder(downloadImageJobService, CHANNEL_ID);


                    } else {
                        mNotification_Builder = new NotificationCompat.Builder(downloadImageJobService);


                    }
                    mNotification_Builder.setContentTitle("Downloading images...")
                            .setContentText("Download in progress")
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setLargeIcon(BitmapFactory.decodeResource(downloadImageJobService.getResources(), R.drawable.ic_launcher))
                            .setBadgeIconType(R.drawable.ic_launcher)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText("Download in progress"))
                            .setColor(ContextCompat.getColor(downloadImageJobService, R.color.colorAccent))
                            .setPriority(NotificationCompat.PRIORITY_MIN)
                            .setProgress(0, 0, true)
                            .setAutoCancel(true)
                            .setOngoing(false);
                    notification = mNotification_Builder.build();
                    assert mNotificationManager != null;
                    mNotificationManager.notify(NOTIFY_ID, notification);
                    try {
                        URL imageUrl = new URL(url);
                        File destination_downloading_directory = new File(Constants.APP_WALLPAPERS_FOLDER);
                        if (!destination_downloading_directory.exists()) {
                            destination_downloading_directory.mkdirs();
                        }
                        downloading_File = new File(destination_downloading_directory + File.separator + mFileName + ".jpg");
                        if (downloading_File.exists()) {
                            downloading_File.delete();

                        }

                        URLConnection urlConnection = imageUrl.openConnection();
                        HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
                        httpURLConnection.setRequestMethod("GET");
                        httpURLConnection.connect();
                        int lengthOfFile = httpURLConnection.getContentLength();

                        inputStream[0] = httpURLConnection.getInputStream();
                        fileOutputStream[0] = new FileOutputStream(downloading_File);
                        byte[] buffer = new byte[2048];
                        int bufferLength = 0;
                        long total = 0;
                        while ((bufferLength = inputStream[0].read(buffer)) != -1) {
                            total += bufferLength;
                            fileOutputStream[0].write(buffer, 0, bufferLength);
                            int[] values = new int[2];
                            values[0] = ((int) (total * 100 / lengthOfFile));
                            Log.d("PROGRESS", String.valueOf(values[0]));
                            mNotification_Builder.setContentTitle("Downloading");
                            mNotification_Builder.setOngoing(false);
                            mNotification_Builder.setContentText(values[0] + "% completed");
                            mNotification_Builder.setPriority(NotificationCompat.PRIORITY_LOW);
                            mNotification_Builder.setStyle(new NotificationCompat.BigTextStyle().bigText("If Download is not happening swipe to dismiss"))
                                    .setProgress(100, values[0], false);
                            mNotification_Builder.setSound(null);
                            // Removes the progress bar
                            notification = mNotification_Builder.build();
                            mNotificationManager.notify(NOTIFY_ID, notification);

                        }


                        inputStream[0].close();
                        fileOutputStream[0].close();
                        ContentValues contentValues = getImageContent(downloading_File);
                        downloadImageJobService.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                        Log.d("IMAGE SAVED", "Image Saved in sd card");


                        if (is_to_set_wallpaper) {
                            try {
                                Intent intent = new Intent(WallpaperManager.getInstance(downloadImageJobService).
                                        getCropAndSetWallpaperIntent(FileProvider.getUriForFile(downloadImageJobService, "io.hustler.qtzy.fileprovider", (downloading_File))));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                downloadImageJobService.startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();

                            }

                        } else {
                            Intent intent_gallery = new Intent(Intent.ACTION_VIEW, FileProvider.getUriForFile(downloadImageJobService, "io.hustler.qtzy.fileprovider", (downloading_File)));
                            intent_gallery.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            PendingIntent pendingIntent = PendingIntent.getActivity(downloadImageJobService, NOTIFY_ID, intent_gallery, PendingIntent.FLAG_ONE_SHOT);
                            mNotification_Builder.setContentTitle("Completed");
                            mNotification_Builder.setContentIntent(pendingIntent);
                            mNotification_Builder.setContentText("Images Successfully downloaded to SD card").setProgress(100, 100, false);// Removes the progress bar
                            mNotification_Builder.setStyle(new NotificationCompat.BigTextStyle().bigText("Click to open"));
                            mNotification_Builder.setSound(null);
                            mNotification_Builder.setOngoing(false);
                            mNotification_Builder.setAutoCancel(true);
                            notification = mNotification_Builder.build();
                            mNotificationManager.notify(NOTIFY_ID, notification);
                        }
                        downloadImageJobService.jobFinished(jobParameters, false);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }
    }

    @Override
    public boolean onStopJob(@NonNull com.firebase.jobdispatcher.JobParameters jobParameters) {
        if (null != task) task.cancel(true);
        NotificationManager notificationManager= (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.cancelAll();
        return true;
    }


}




