package com.hustler.quote.ui.customviews;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by anvaya5 on 04/01/2018.
 */

public class ParrallaxPageTransformer implements ViewPager.PageTransformer {
    int id;
    private int border = 0;
    private float speed = 0.2f;

    public ParrallaxPageTransformer(int id) {
        this.id = id;
    }


    @Override
    public void transformPage(View page, float position) {
        View parralexView = page.findViewById(id);
        if (page == null) {
            Log.e("ParallaxERROR", "NO VIEW");

        } else {
            if (parralexView != null && position > -1 && position < 1) {
                float width = parralexView.getWidth();
                parralexView.setTranslationX(-(position * width * speed));
                float sc = ((float) page.getWidth() - border) / page.getWidth();
                if (position == 0) {
                    page.setScaleX(1);
                    page.setScaleY(1);
                } else {
                    page.setScaleX(sc);
                    page.setScaleY(sc);
                }
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
