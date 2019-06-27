package io.hustler.qtzy.ui.Services.JobServices;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

import io.hustler.qtzy.R;
import io.hustler.qtzy.ui.apiRequestLauncher.Constants;
import io.hustler.qtzy.ui.database.ImagesDbHelper;
import io.hustler.qtzy.ui.pojo.unspalsh.Unsplash_Image;
import io.hustler.qtzy.ui.utils.FileUtils;

import static android.os.Build.VERSION_CODES.O;

public class WallaperFirebaseJobService extends JobService {
    private static int BUNDLENOTIFICATIONID = 5005;
    private static int SINGLENOTIFICATIONID = 5005;
    List<Unsplash_Image> images;
    @Nullable
    File downloading_File;
    SharedPreferences sharedPreferences;
    int image;
    SharedPreferences.Editor editor;
    String request;
    Unsplash_Image unsplash_image;
    WallpaperManager wallpaperManager;
    ImageDownloaderTask imageDownloaderTask;
    private String BUNDLECHANNEL_ID = "BUNDLE_DAILY_WALL_CHANNEL";
    private String GROUP_ID = "io.hustler.qtzy.DAILY_WALL_GROUP";

    @Override
    public boolean onStartJob(@NonNull JobParameters jobParameters) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        imageDownloaderTask = (ImageDownloaderTask) new ImageDownloaderTask(jobParameters).execute();
        return true;
    }

    @Override
    public boolean onStopJob(@NonNull JobParameters jobParameters) {
        if (imageDownloaderTask != null) {
            imageDownloaderTask.cancel(true);
        }
        /*ENABLES THE JOB WHE THE CONDITIONS ARE MET*/
        return true;
    }

    private void callApi(@NonNull final SharedPreferences.Editor editor, String query) {
//      query  request = Constants.UNSPLASH_SEARCH_IMAGES_API + "&orientation=portrait&featured=true&count=30&query=" + query + "&page=" + new Random().nextInt(8);
//        new Restutility().getImages_for_service(getApplicationContext(), new SearchImagesResponseListener() {
//            @Override
//            public void onSuccess(@NonNull UnsplashImages_Collection_Response response) {
//                Gson gson = new Gson();
//                String imagess = gson.toJson(response.getResults());
//                editor.putString(Constants.Shared_prefs_loaded_images_for_service_key, imagess);
//                editor.putInt(Constants.Shared_prefs_current_service_image_key, 1);
//                editor.putInt(Constants.Shared_prefs_current_service_image_Size_key, response.getResults().length);
//                editor.apply();
//                unsplash_image = response.getResults()[0];
//                downloadAndSetWallpaper();
//
//            }
//
//            @Override
//            public void onError(String error) {
//
//            }
//        }, request);

    }

    private class ImageDownloaderTask extends AsyncTask<String, String, Void> {
        private final JobParameters jobParameters;
        NotificationManager mNotificationManager;
        NotificationCompat.Builder mNotification_Builder;

        public ImageDownloaderTask(JobParameters jobParameters) {
            this.jobParameters = jobParameters;
        }

        @Override
        protected void onPreExecute() {

        }

        @Nullable
        @Override
        protected Void doInBackground(final String... params) {
            if (sharedPreferences.getString(getResources().getString(R.string.DWL_NETWORK_TYPE), "Space").equals("Favourites")) {
                images = new ImagesDbHelper(getApplicationContext()).getAllFav();
//            IF USER SELECTS FAV BUT HE HAS NO FAV LIST IN DB THEN WE CALL API WITH NATURE IMAGES
                if (images == null || images.size() <= 0) {
                    callApi(editor, "Space");
                    editor.putString(getResources().getString(R.string.DWL_NETWORK_TYPE), "Space");
                } else {
                    image = new Random().nextInt(images.size()) + 0;
                    unsplash_image = images.get(image);

                }

                Log.i("SERVICE CHECK", "FAV S SET");

            } else {
                if (sharedPreferences.getInt(Constants.Shared_prefs_current_service_image_key, 0) <= 0) {
                    callApi(editor, sharedPreferences.getString(getResources().getString(R.string.DWL_NETWORK_TYPE), "Space"));
                } else {
                    Type type = new TypeToken<Unsplash_Image[]>() {
                    }.getType();
                    int imageIndex = sharedPreferences.getInt(Constants.Shared_prefs_current_service_image_key, 0);
                    Log.i("Image index", String.valueOf(imageIndex));
                    Log.i("images length", String.valueOf(sharedPreferences.getInt(Constants.Shared_prefs_current_service_image_Size_key, 0)));

                    if (sharedPreferences.getInt(Constants.Shared_prefs_current_service_image_key, 0) >= sharedPreferences.getInt(Constants.Shared_prefs_current_service_image_Size_key, 0)) {
                        callApi(editor, sharedPreferences.getString(getResources().getString(R.string.DWL_NETWORK_TYPE), "Nature"));

                    } else {
                        Unsplash_Image[] unsplash_images = new Gson().fromJson(sharedPreferences.getString(Constants.Shared_prefs_loaded_images_for_service_key, null), type);
                        unsplash_image = unsplash_images[imageIndex];
                        editor.putInt(Constants.Shared_prefs_current_service_image_key, imageIndex + 1);
                        editor.commit();
                        Log.i("Updated Image index", String.valueOf(sharedPreferences.getInt(Constants.Shared_prefs_current_service_image_key, 0)));
                        downloadAndSetWallpaper();

                    }


                }
                Log.i("SERVICE CHECK", "APIS SET");
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (mNotificationManager == null) {
                mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            }

            /*CREATE A BUNDLE*/
            if (Build.VERSION.SDK_INT >= O) {
                /*DWN -- Daily wallpaper notification*/
                CharSequence bundleChannelName = "DWN_bundle_channel";
                String description = getString(R.string.notication_channel_desc);
                int importance = NotificationManager.IMPORTANCE_DEFAULT;

                if (mNotificationManager.getNotificationChannel(BUNDLECHANNEL_ID) == null) {
                    NotificationChannel groupChannel = new NotificationChannel(BUNDLECHANNEL_ID, bundleChannelName, importance);
                    groupChannel.setDescription(description);
                    mNotificationManager.createNotificationChannel(groupChannel);

                }
                BUNDLENOTIFICATIONID += 100;
                String bundle_group_notification_id = BUNDLENOTIFICATIONID + BUNDLECHANNEL_ID;

                mNotification_Builder = new NotificationCompat.Builder(getApplicationContext(), BUNDLECHANNEL_ID);
                setBuilder(mNotificationManager, mNotification_Builder, bundle_group_notification_id, BUNDLENOTIFICATIONID);


            } else {
                mNotification_Builder = new NotificationCompat.Builder(getApplicationContext());
                setBuilder(mNotificationManager, mNotification_Builder, "bundle_group_notification_id", BUNDLENOTIFICATIONID);

            }
            /*ADD NOTIFICATION TO THE BUNDLE*/


            jobFinished(jobParameters, false);

        }
    }

    private void downloadAndSetWallpaper() {
        wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        downloading_File = FileUtils.downloadImageToSd_Card(unsplash_image.getUrls().getRaw(), unsplash_image.getId() + "DW.jpg", getApplicationContext());
        try {
            wallpaperManager.setBitmap(BitmapFactory.decodeFile((downloading_File).getAbsolutePath()));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        if (downloading_File != null) {
            downloading_File.delete();
        }
    }

    private void setBuilder(NotificationManager mNotificationManager, NotificationCompat.Builder mNotification_builder, String bundleNotificationId, int notificationID) {
        Notification notification = mNotification_builder
                .setContentTitle("Changed Wallpaper...")
                .setContentText("Wallpaper changed")
                .setGroup(GROUP_ID)
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setBadgeIconType(R.drawable.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("New Wallpaper applied"))
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent))
                .setGroup(bundleNotificationId)
                .setGroupSummary(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(false).build();
        mNotificationManager.notify(notificationID, notification);

    }
}
