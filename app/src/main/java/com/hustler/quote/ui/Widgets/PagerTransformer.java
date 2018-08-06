package com.hustler.quote.ui.Widgets;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by Sayi Manoj Sugavasi on 12/03/2018.
 */

public class PagerTransformer implements ViewPager.PageTransformer {

    //    private final int id2, id3, id4, id5;
    int id;
    private int border = 0;
    private float speed = 0.2f;
    private static final float MIN_SCALE = 0.45f;
    private static final float MIN_ALPHA = 0.85f;

    public PagerTransformer(int id) {
        this.id = id;
//        this.id2 = id2;
//        this.id3 = id3;
//        this.id4 = id4;
//        this.id5 = id5;
    }


    @Override
    public void transformPage(@NonNull View page, float position) {
        View parralexView = page.findViewById(id);
        int pageWidth = parralexView.getWidth();
        int pageHeight = parralexView.getHeight();
        if (null == page) {
            Log.e("ParallaxERROR", "NO VIEW");

        } else {
            if (position < -1) {
                parralexView.setAlpha(0);
            } else if (position <= 1) {
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                Log.d("SCALE FACTOR CALLED", scaleFactor + "");

                parralexView.setScaleX(scaleFactor);
                parralexView.setScaleY(scaleFactor);
                // Fade the page relative to its size.
                parralexView.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
                parralexView.setElevation((MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA))*16);
            } else {
                // (1,+Infinity]
                // This page is way off-screen to the right.
                parralexView.setAlpha(0);
            }

        }
    }

    public void setBorder(int px) {
        border = px;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
