package com.hustler.quote.ui.livewallpaper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.hustler.quote.R;

/**
 * Created by Sayi on 31-12-2017.
 */

public class MyWallpaperService extends WallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new MyWallpaperEngine();
    }

    private class MyWallpaperEngine extends Engine {
        private final Handler handler = new Handler();
        private final Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };

        public MyWallpaperEngine() {
            handler.post(drawRunner);
        }

        private void draw() {
            SurfaceHolder surfaceHolder = getSurfaceHolder();
            Bitmap icon=BitmapFactory.decodeResource(getResources(), R.drawable.animation_list);
            Canvas canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas();
                canvas.drawColor(Color.YELLOW);
                canvas.drawBitmap(icon,105,105,null);
                icon.recycle();
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
            handler.removeCallbacks(drawRunner);
        }
    }

}
