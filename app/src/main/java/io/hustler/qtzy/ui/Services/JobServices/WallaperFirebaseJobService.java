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
import io.hustler.qtzy.ui.Executors.AppExecutor;
import io.hustler.qtzy.ui.apiRequestLauncher.Constants;
import io.hustler.qtzy.ui.apiRequestLauncher.Restutility;
import io.hustler.qtzy.ui.database.ImagesDbHelper;
import io.hustler.qtzy.ui.pojo.ResGetSearchResultsDto;
import io.hustler.qtzy.ui.pojo.listeners.SearchImagesResponseListener;
import io.hustler.qtzy.ui.pojo.unspalsh.Unsplash_Image;
import io.hustler.qtzy.ui.utils.FileUtils;

import static android.os.Build.VERSION_CODES.O;

public class WallaperFirebaseJobService extends JobService {
    public String TAG = this.getClass().getSimpleName();
    private static int BUNDLENOTIFICATIONID = 5005;
    private static int SINGLENOTIFICATIONID = 5005;
    List<Unsplash_Image> images;
    @Nullable
    File downloading_File;
    SharedPreferences sharedPreferences;
    int image;
    AppExecutor appExecutor;
    SharedPreferences.Editor editor;
    Unsplash_Image unsplash_image;
    WallpaperManager wallpaperManager;
    private String BUNDLECHANNEL_ID = "BUNDLE_DAILY_WALL_CHANNEL";
    private String GROUP_ID = "io.hustler.qtzy.DAILY_WALL_GROUP";
    private static AsyncTask mtask;

    @Override
    public boolean onStartJob(@NonNull final JobParameters jobParameters) {
        Log.i(TAG, "onStartJob: " + " --> JOB CALLED");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        appExecutor = AppExecutor.getInstance();
        mtask = new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] objects) {


                if (sharedPreferences.getString(getResources().getString(R.string.DWL_CATEGORIES), "Space").equals("Favourites")) {

                    images = new ImagesDbHelper(getApplicationContext()).getAllFav();/*            IF USER SELECTS FAV BUT HE HAS NO FAV LIST IN DB THEN WE CALL API WITH NATURE IMAGES*/
                    if (images.size() <= 0) {
                        callApi(editor, "Space", jobParameters);
                        editor.putString(getResources().getString(R.string.DWL_CATEGORIES), "Space");
                    } else {
                        image = new Random().nextInt(images.size());
                        unsplash_image = images.get(image);
                    }
                    Log.i("SERVICE CHECK", "FAV S SET");
                } else {
                    if (sharedPreferences.getInt(Constants.currentWallpaperIndexSharedPreferenceKey, 0) <= 0)
                        callApi(editor, sharedPreferences.getString(getResources().getString(R.string.DWL_CATEGORIES), "Space"), jobParameters);
                    else {
                        Type type = new TypeToken<ResGetSearchResultsDto>() {
                        }.getType();
                        int imageIndex = sharedPreferences.getInt(Constants.currentWallpaperIndexSharedPreferenceKey, 0);
                        if (sharedPreferences.getInt(Constants.currentWallpaperIndexSharedPreferenceKey, 0) >= sharedPreferences.getInt(Constants.Shared_prefs_current_service_image_Size_key, 0))
                            callApi(editor, sharedPreferences.getString(getResources().getString(R.string.DWL_CATEGORIES), "Nature"), jobParameters);
                        else {
                            Unsplash_Image[] unsplash_images;
                            Log.e(TAG, "doInBackground: " + sharedPreferences.getString(Constants.savedImagesJsonResponseforDailyWallpapers, null));
                            ResGetSearchResultsDto collection_response = new Gson().fromJson(sharedPreferences.getString(Constants.savedImagesJsonResponseforDailyWallpapers, null), type);
                            assert collection_response != null;
                            unsplash_images = collection_response.getResults();
                            assert unsplash_images != null;
                            unsplash_image = unsplash_images[imageIndex];
                            editor.putInt(Constants.currentWallpaperIndexSharedPreferenceKey, imageIndex + 1).apply();
                            Log.i("Updated Image index", String.valueOf(sharedPreferences.getInt(Constants.currentWallpaperIndexSharedPreferenceKey, 0)));
                            downloadAndSetWallpaper(jobParameters);
                        }
                    }
                    Log.i("SERVICE CHECK", "APIS SET");
                }

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                NotificationManager mNotificationManager = null;
                mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); /*CREATE A BUNDLE*/
                NotificationCompat.Builder mNotification_Builder;
                if (Build.VERSION.SDK_INT >= O) { /*DWN -- Daily wallpaper notification*/
                    CharSequence bundleChannelName = "DWN_bundle_channel";
                    String description = getString(R.string.notication_channel_desc);
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    assert mNotificationManager != null;
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
                    assert mNotificationManager != null;
                    setBuilder(mNotificationManager, mNotification_Builder, "bundle_group_notification_id", BUNDLENOTIFICATIONID);
                } /*ADD NOTIFICATION TO THE BUNDLE*/
                jobFinished(jobParameters, false);
                Log.i(TAG, "onStopJob: " + " --> JOB STOPPED");

            }
        };
        mtask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(@NonNull JobParameters jobParameters) {
        if (mtask != null) mtask.cancel(true);
        /*ENABLES THE JOB WHE THE CONDITIONS ARE MET*/
        return true;
    }

    private void callApi(@NonNull final SharedPreferences.Editor editor, String query, final JobParameters jobParameters) {
        final String request = Constants.UNSPLASH_SEARCH_IMAGES_API + "&query=" + query;
        Log.i(TAG, "callApi: " + request);
        new Restutility(getApplication()).getUnsplashImagesForSearchQuery(getApplicationContext(), new SearchImagesResponseListener() {
            @Override
            public void onSuccess(@NonNull final ResGetSearchResultsDto response) {
                if (response.getResults().length > 0) {
                    Gson gson = new Gson();
                    String imagess = gson.toJson(response);
                    editor.putString(Constants.savedImagesJsonResponseforDailyWallpapers, imagess);
                    editor.putInt(Constants.currentWallpaperIndexSharedPreferenceKey, 1);
                    editor.putInt(Constants.Shared_prefs_current_service_image_Size_key, response.getResults().length);
                    editor.apply();
                    unsplash_image = response.getResults()[0];
                    downloadAndSetWallpaper(jobParameters);
                } else {
                    onError("Response Empty");

                }
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "onError: " + error);
                jobFinished(jobParameters, true);
            }
        }, request);
    }


    private void downloadAndSetWallpaper(final JobParameters jobParameters) {
        wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        appExecutor.getNetworkExecutor().execute(new Runnable() {
            @Override
            public void run() {
                downloading_File = FileUtils.downloadImageToSd_Card(unsplash_image.getUrls().getFull(), unsplash_image.getId() + "DW.jpg", getApplicationContext());
                try {
                    assert downloading_File != null;
                    wallpaperManager.setBitmap(BitmapFactory.decodeFile((downloading_File).getAbsolutePath()));
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                if (downloading_File != null)
                    downloading_File.delete();

            }
        });


    }


    private void setBuilder(NotificationManager mNotificationManager, NotificationCompat.Builder mNotification_builder, String bundleNotificationId, int notificationID) {
        mNotificationManager.cancel(notificationID);
        Notification notification = mNotification_builder.setContentTitle("Changed Wallpaper...").setContentText("Wallpaper changed").setGroup(GROUP_ID).setSmallIcon(R.drawable.ic_launcher).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher)).setBadgeIconType(R.drawable.ic_launcher).setStyle(new NotificationCompat.BigTextStyle().bigText("New Wallpaper applied")).setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent)).setGroup(bundleNotificationId).setGroupSummary(true).setPriority(NotificationCompat.PRIORITY_DEFAULT).setAutoCancel(false).build();
        mNotificationManager.notify(notificationID, notification);
    }


}