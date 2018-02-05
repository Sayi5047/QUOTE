package com.hustler.quote.ui.Services;

import android.app.Service;
import android.app.WallpaperManager;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.hustler.quote.ui.database.ImagesDbHelper;
import com.hustler.quote.ui.pojo.unspalsh.Unsplash_Image;

import java.util.List;
import java.util.Random;

/**
 * Created by Sayi on 25-01-2018.
 */

public class DailyWallpaperService extends Service {
    List<Unsplash_Image> images;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        images=new ImagesDbHelper(getApplicationContext()).getAllFav();
        int image=new Random().nextInt(images.size());
        WallpaperManager wallpaperManager=WallpaperManager.getInstance(getApplicationContext());
        return super.onStartCommand(intent, flags, startId);
    }
}
