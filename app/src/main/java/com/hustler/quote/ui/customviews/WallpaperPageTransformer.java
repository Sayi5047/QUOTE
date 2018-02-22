package com.hustler.quote.ui.customviews;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by Sayi on 27-01-2018.
 */

public class WallpaperPageTransformer implements ViewPager.PageTransformer {
    private int border = 0;
    private float speed = 0.8f;
    int imageId;

    public WallpaperPageTransformer(int imageId) {
        this.imageId = imageId;
    }




    public void setBorder(int px) {
        border = px;
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
        View parralexView = page.findViewById(imageId);
        if (page == null) {

        } else {
            if (parralexView != null && position > -1 && position < 1) {
                float width = parralexView.getWidth();

                parralexView.setTranslationX(-(position * width * speed));
                float scaleFactor = ((float) page.getWidth() - border) / page.getWidth();
                if (position == 0) {
                    page.setScaleX(1);
                    page.setScaleY(1);

                } else {
                    page.setScaleX(scaleFactor);
                    page.setScaleY(scaleFactor);
                }
            }
        }
    }
}
