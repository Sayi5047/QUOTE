package com.hustler.quotzy.ui.Services;

import android.app.IntentService;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hustler.quotzy.R;
import com.hustler.quotzy.ui.Recievers.AlarmReciever;
import com.hustler.quotzy.ui.apiRequestLauncher.Constants;
import com.hustler.quotzy.ui.apiRequestLauncher.Restutility;
import com.hustler.quotzy.ui.database.ImagesDbHelper;
import com.hustler.quotzy.ui.pojo.UnsplashImages_Collection_Response;
import com.hustler.quotzy.ui.pojo.Unsplash_Image_collection_response_listener;
import com.hustler.quotzy.ui.pojo.unspalsh.Unsplash_Image;
import com.hustler.quotzy.ui.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

/**
 * Created by Sayi on 25-01-2018.
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
public class DailyWallpaperService extends IntentService {
    List<Unsplash_Image> images;
    File downloading_File;
    SharedPreferences sharedPreferences;
    int image;
    SharedPreferences.Editor editor;
    String request;
    Unsplash_Image unsplash_image;
    WallpaperManager wallpaperManager;

    public DailyWallpaperService() {
        super("DailyWallpaperService");
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        if (sharedPreferences.getString(getResources().getString(R.string.DWL_key3), "Nature").equals("Favourites")) {
            images = new ImagesDbHelper(getApplicationContext()).getAllFav();
//            IF USER SELECTS FAV BUT HE HAS NO FAV LIST IN DB THEN WE CALL API WITH NATURE IMAGES
            if (images == null || images.size() <= 0) {
                callApi(sharedPreferences, editor, "Nature", intent);
                editor.putString(getResources().getString(R.string.DWL_key3), "Nature");
            } else {
                image = new Random().nextInt(images.size()) + 0;
                unsplash_image = images.get(image);
                download_and_setWallapper(intent, unsplash_image);
            }

            Log.i("SERVICE CHECK", "FAV S SET");

        } else {
            if (sharedPreferences.getInt(Constants.Shared_prefs_current_service_image_key, 0) <= 0) {
                callApi(sharedPreferences, editor, sharedPreferences.getString(getResources().getString(R.string.DWL_key3), "Nature"), intent);

            } else {
                Type type = new TypeToken<Unsplash_Image[]>() {
                }.getType();
                int imageIndex = sharedPreferences.getInt(Constants.Shared_prefs_current_service_image_key, 0);
                Log.i("Image index", String.valueOf(imageIndex));
                Log.i("images length", String.valueOf(sharedPreferences.getInt(Constants.Shared_prefs_current_service_image_Size_key, 0)));

                if (sharedPreferences.getInt(Constants.Shared_prefs_current_service_image_key, 0) >= sharedPreferences.getInt(Constants.Shared_prefs_current_service_image_Size_key, 0)) {
                    callApi(sharedPreferences, editor, sharedPreferences.getString(getResources().getString(R.string.DWL_key3), "Nature"), intent);

                } else {
                    Unsplash_Image[] unsplash_images = new Gson().fromJson(sharedPreferences.getString(Constants.Shared_prefs_loaded_images_for_service_key, null), type);
                    unsplash_image = unsplash_images[imageIndex];
                    editor.putInt(Constants.Shared_prefs_current_service_image_key, imageIndex + 1);
                    editor.commit();
                    Log.i("Updated Image index", String.valueOf(sharedPreferences.getInt(Constants.Shared_prefs_current_service_image_key, 0)));

                    download_and_setWallapper(intent, unsplash_image);
                }


            }

            Log.i("SERVICE CHECK", "APIS SET");

        }


    }

    private void callApi(SharedPreferences sharedPreferences, final SharedPreferences.Editor editor, String query, final Intent intent) {
        request = Constants.API_GET_Collections_FROM_UNSPLASH + "&orientation=portrait&featured=true&count=30&query=" + query + "&page=" + new Random().nextInt(8);
        new Restutility().getImages_for_service(getApplicationContext(), new Unsplash_Image_collection_response_listener() {
            @Override
            public void onSuccess(UnsplashImages_Collection_Response response) {
                Gson gson = new Gson();
                String imagess = gson.toJson(response.getResults());
                editor.putString(Constants.Shared_prefs_loaded_images_for_service_key, imagess);
                editor.putInt(Constants.Shared_prefs_current_service_image_key, 1);
                editor.putInt(Constants.Shared_prefs_current_service_image_Size_key, response.getResults().length);
                editor.apply();
                unsplash_image = response.getResults()[0];
                download_and_setWallapper(intent, unsplash_image);

            }

            @Override
            public void onError(String error) {

            }
        }, request);

    }

    private void download_and_setWallapper(final Intent intent, final Unsplash_Image unsplash_image) {
        wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        new ImageDownloader(intent, unsplash_image).execute();

    }

    class ImageDownloader extends AsyncTask<String, String, Void> {
        public ImageDownloader(Intent intent, Unsplash_Image unsplash_image) {
            this.intent = intent;
            this.unsplash_image = unsplash_image;
        }

        Intent intent;
        Unsplash_Image unsplash_image;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(final String... params) {

            downloading_File = FileUtils.downloadImageToSd_Card(unsplash_image.getUrls().getFull(), unsplash_image.getId() + "SAYI.jpg", null,getApplicationContext());
            try {
                wallpaperManager.setBitmap(BitmapFactory.decodeFile((downloading_File).getAbsolutePath()));
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            if (downloading_File != null) {
                downloading_File.delete();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            AlarmReciever.completeWakefulIntent(intent);
            stopSelf();


        }
    }

}
