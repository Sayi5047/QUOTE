package com.hustler.quote.ui.livewallpaper;

import android.os.Handler;
import android.service.wallpaper.WallpaperService;

/**
 * Created by Sayi on 31-12-2017.
 */

public class MyWallpaperService extends WallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new MyWallpaperEngine();
    }

    private class MyWallpaperEngine extends Engine{
        private final Handler handler=new Handler();
        private final Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };

        private void draw() {

        }
    }

}
