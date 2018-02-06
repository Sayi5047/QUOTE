package com.hustler.quote.ui.Services;

import android.app.IntentService;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.hustler.quote.ui.Recievers.AlarmReciever;
import com.hustler.quote.ui.database.ImagesDbHelper;
import com.hustler.quote.ui.pojo.unspalsh.Unsplash_Image;
import com.hustler.quote.ui.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by Sayi on 25-01-2018.
 */

public class DailyWallpaperService extends IntentService {
    List<Unsplash_Image> images;
    File downloading_File;

    public DailyWallpaperService() {
        super("DailyWallpaperService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        images = new ImagesDbHelper(getApplicationContext()).getAllFav();
        int image = new Random().nextInt(images.size())+0;
        Unsplash_Image unsplash_image = images.get(image);
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        downloading_File = FileUtils.downloadImageToSd_Card(unsplash_image.getUrls().getRaw(), unsplash_image.getId());
        try {
            wallpaperManager.setBitmap(BitmapFactory.decodeFile((downloading_File).getAbsolutePath()));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        AlarmReciever.completeWakefulIntent(intent);
        stopSelf();

    }


}
