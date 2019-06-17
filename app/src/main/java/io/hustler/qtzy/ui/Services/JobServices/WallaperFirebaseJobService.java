package io.hustler.qtzy.ui.Services.JobServices;

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
import io.hustler.qtzy.ui.apiRequestLauncher.Restutility;
import io.hustler.qtzy.ui.database.ImagesDbHelper;
import io.hustler.qtzy.ui.pojo.UnsplashImages_Collection_Response;
import io.hustler.qtzy.ui.pojo.Unsplash_Image_collection_response_listener;
import io.hustler.qtzy.ui.pojo.unspalsh.Unsplash_Image;
import io.hustler.qtzy.ui.utils.FileUtils;

import static android.os.Build.VERSION_CODES.O;

public class WallaperFirebaseJobService extends JobService {
    private static final int DAILY_WALL_NOTIFY_ID = 5005;
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
    private String CHANNEL_ID = "DAILY_WALL_CHANNEL";
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
        request = Constants.API_GET_Collections_FROM_UNSPLASH + "&orientation=portrait&featured=true&count=30&query=" + query + "&page=" + new Random().nextInt(8);
        new Restutility().getImages_for_service(getApplicationContext(), new Unsplash_Image_collection_response_listener() {
            @Override
            public void onSuccess(@NonNull UnsplashImages_Collection_Response response) {
                Gson gson = new Gson();
                String imagess = gson.toJson(response.getResults());
                editor.putString(Constants.Shared_prefs_loaded_images_for_service_key, imagess);
                editor.putInt(Constants.Shared_prefs_current_service_image_key, 1);
                editor.putInt(Constants.Shared_prefs_current_service_image_Size_key, response.getResults().length);
                editor.apply();
                unsplash_image = response.getResults()[0];
                downloadAndSetWallpaper();

            }

            @Override
            public void onError(String error) {

            }
        }, request);

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
            if (sharedPreferences.getString(getResources().getString(R.string.DWL_key3), "Nature").equals("Favourites")) {
                images = new ImagesDbHelper(getApplicationContext()).getAllFav();
//            IF USER SELECTS FAV BUT HE HAS NO FAV LIST IN DB THEN WE CALL API WITH NATURE IMAGES
                if (images == null || images.size() <= 0) {
                    callApi(editor, "Nature");
                    editor.putString(getResources().getString(R.string.DWL_key3), "Nature");
                } else {
                    image = new Random().nextInt(images.size()) + 0;
                    unsplash_image = images.get(image);

                }

                Log.i("SERVICE CHECK", "FAV S SET");

            } else {
                if (sharedPreferences.getInt(Constants.Shared_prefs_current_service_image_key, 0) <= 0) {
                    callApi(editor, sharedPreferences.getString(getResources().getString(R.string.DWL_key3), "Nature"));
                } else {
                    Type type = new TypeToken<Unsplash_Image[]>() {
                    }.getType();
                    int imageIndex = sharedPreferences.getInt(Constants.Shared_prefs_current_service_image_key, 0);
                    Log.i("Image index", String.valueOf(imageIndex));
                    Log.i("images length", String.valueOf(sharedPreferences.getInt(Constants.Shared_prefs_current_service_image_Size_key, 0)));

                    if (sharedPreferences.getInt(Constants.Shared_prefs_current_service_image_key, 0) >= sharedPreferences.getInt(Constants.Shared_prefs_current_service_image_Size_key, 0)) {
                        callApi(editor, sharedPreferences.getString(getResources().getString(R.string.DWL_key3), "Nature"));

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

            if (Build.VERSION.SDK_INT >= O) {
                CharSequence name = "Image Downloader";
                String description = getString(R.string.notication_channel_desc);
                int importance = NotificationManager.IMPORTANCE_HIGH;
                if (mNotificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                    channel.setDescription(description);
                    mNotificationManager.createNotificationChannel(channel);
                }
                mNotification_Builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
                setBuilder(mNotificationManager, mNotification_Builder);

            } else {
                mNotification_Builder = new NotificationCompat.Builder(getApplicationContext());
                setBuilder(mNotificationManager, mNotification_Builder);

            }

            jobFinished(jobParameters, false);

        }
    }

    private void downloadAndSetWallpaper() {
        wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        downloading_File = FileUtils.downloadImageToSd_Card(unsplash_image.getUrls().getRegular(), unsplash_image.getId() + "SAYI.jpg", getApplicationContext());
        try {
            wallpaperManager.setBitmap(BitmapFactory.decodeFile((downloading_File).getAbsolutePath()));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        if (downloading_File != null) {
            downloading_File.delete();
        }
    }

    private void setBuilder(NotificationManager mNotificationManager, NotificationCompat.Builder mNotification_Builder) {
        mNotification_Builder.setContentTitle("Changed Wallpaper...").setContentText("Wallpaper changed")
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setBadgeIconType(R.drawable.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("New Wallpaper applied"))
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT).setGroup(GROUP_ID).setAutoCancel(false)
        ;
        mNotificationManager.notify(DAILY_WALL_NOTIFY_ID+1, mNotification_Builder.build());

    }
}
