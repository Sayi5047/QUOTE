package io.hustler.qtzy.ui.Services;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.firebase.jobdispatcher.JobService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import io.hustler.qtzy.R;
import io.hustler.qtzy.ui.Recievers.NotifcationReciever;
import io.hustler.qtzy.ui.apiRequestLauncher.Constants;
import io.hustler.qtzy.ui.utils.Toast_Snack_Dialog_Utils;

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
    android.support.v4.app.NotificationCompat.Builder mNotification_Builder;
    @Nullable
    NotificationManager mNotificationManager;
    NotifcationReciever mNotification_Reciever;
    String mFileName;
    Boolean is_to_set_wallpaper;
    public File downloading_File;
    ImageDownloader imageDownloaderTask;
    private static final String CHANNEL_ID = "QUOTZY";
    private static final int NOTIFY_ID = 5004;

    @Override
    public boolean onStartJob(@NonNull com.firebase.jobdispatcher.JobParameters jobParameters) {

        String url;
        url = jobParameters.getExtras().getString(Constants.ImageUrl_to_download);
        mFileName = jobParameters.getExtras().getString(Constants.Image_Name_to_save_key);
        is_to_set_wallpaper = (Boolean) jobParameters.getExtras().get(Constants.is_to_setWallpaper_fromActivity);

        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= O) {
            CharSequence name = "Image Downloader";
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
        imageDownloaderTask = new ImageDownloader(jobParameters);
        imageDownloaderTask.execute(url);
        mNotification_Reciever = new NotifcationReciever();
        IntentFilter filter = new IntentFilter();
        filter.addAction(NotificationCustomListener_Service.NOTIFICATION_TAG);
        getApplicationContext().registerReceiver(mNotification_Reciever, filter);


        return false;
    }

    @Override
    public boolean onStopJob(@NonNull com.firebase.jobdispatcher.JobParameters jobParameters) {
        if (null != imageDownloaderTask) {
            imageDownloaderTask.cancel(true);
        }
        return true;
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
                .setOngoing(true);
        mNotificationManager.notify(NOTIFY_ID, mNotification_Builder.build());

    }


    public class ImageDownloader extends AsyncTask<String, String, Void> {

        private final com.firebase.jobdispatcher.JobParameters jobParameters;

        public ImageDownloader(com.firebase.jobdispatcher.JobParameters jobParameters) {
            this.jobParameters = jobParameters;
        }

        @Override
        protected void onPreExecute() {

        }

        @Nullable
        @Override
        protected Void doInBackground(final String... params) {

            FileOutputStream fileOutputStream;
            InputStream inputStream;

            try {
                URL url = new URL(params[0]);
                File destination_downloading_directory = new File(Constants.APP_WALLPAPERS_FOLDER);
                if (!destination_downloading_directory.exists()) {
                    destination_downloading_directory.mkdirs();
                }
                downloading_File = new File(destination_downloading_directory + File.separator + mFileName + ".jpg");
                if (downloading_File.exists()) {
                    downloading_File.delete();

                }

                URLConnection urlConnection = url.openConnection();
                HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                int lengthOfFile = httpURLConnection.getContentLength();

                inputStream = httpURLConnection.getInputStream();
                fileOutputStream = new FileOutputStream(downloading_File);
                byte[] buffer = new byte[2048];
                int bufferLength = 0;
                long total = 0;
                while ((bufferLength = inputStream.read(buffer)) != -1) {
                    total += bufferLength;
//                        mNotification_Builder.setProgress(100, ((int) (total * 100 / lengthOfFile)), false);
//                        mNotificationManager.notify(NOTIFY_ID, mNotification_Builder.build());
                    fileOutputStream.write(buffer, 0, bufferLength);
                    publishProgress(String.valueOf((int) (total * 100 / lengthOfFile)));
//                        Log.d("do in BG", String.valueOf((int) (total * 100 / lengthOfFile)));


                }


                inputStream.close();
                fileOutputStream.close();
                ContentValues contentValues = getImageContent(downloading_File);
                getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                Log.d("IMAGE SAVED", "Image Saved in sd card");
                return null;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPostExecute(Void aVoid) throws NullPointerException {
            Intent intent_gallery = new Intent(Intent.ACTION_VIEW, FileProvider.getUriForFile(getApplicationContext(), getString(R.string.file_provider_authority), (downloading_File)));
            intent_gallery.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), NOTIFY_ID, intent_gallery, PendingIntent.FLAG_ONE_SHOT);
            mNotification_Builder.setContentTitle("Completed");
            mNotification_Builder.setContentIntent(pendingIntent);
            mNotification_Builder.setContentText("Images Successfully downloaded to SD card").setProgress(100, 100, false);// Removes the progress bar
            mNotification_Builder.setStyle(new NotificationCompat.BigTextStyle().bigText("Click to open"));

            mNotificationManager.notify(NOTIFY_ID, mNotification_Builder.build());
            if (!is_to_set_wallpaper) {
                imageDownloaderTask.cancel(true);
            } else {
                if (downloading_File != null) {
                    try {

                        Intent intent = new Intent(WallpaperManager.getInstance(getApplicationContext()).
                                getCropAndSetWallpaperIntent(FileProvider.getUriForFile(getApplicationContext(), "com.hustler.quote.fileprovider", (downloading_File))));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                } else {
                    try {
                        Toast_Snack_Dialog_Utils.show_ShortToast((Activity) getApplicationContext(), "Failed to set wallpaper");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            jobFinished(jobParameters, false);
        }


        @Override
        protected void onProgressUpdate(final String... values) {

            Log.d("PROGRESS", String.valueOf(values[0]));

            mNotification_Builder.setContentTitle("Downloading");
            mNotification_Builder.setContentText(values[0] + "% completed");
            mNotification_Builder.setStyle(new NotificationCompat.BigTextStyle().bigText(values[0] + "% completed"))
                    .setProgress(100, Integer.valueOf(values[0]), false);
            // Removes the progress bar
            mNotificationManager.notify(NOTIFY_ID, mNotification_Builder.build());


            super.onProgressUpdate(values);
        }
    }

}
