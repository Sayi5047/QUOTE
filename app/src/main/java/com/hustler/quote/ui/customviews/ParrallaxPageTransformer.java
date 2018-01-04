package com.hustler.quote.ui.customviews;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by anvaya5 on 04/01/2018.
 */

public class ParrallaxPageTransformer implements ViewPager.PageTransformer {
    private final int id2, id3, id4, id5;
    int id;
    private int border = 0;
    private float speed = 0.2f;

    public ParrallaxPageTransformer(int id, int id2, int id3, int id4, int id5) {
        this.id = id;
        this.id2 = id2;
        this.id3 = id3;
        this.id4 = id4;
        this.id5 = id5;
    }


    @Override
    public void transformPage(View page, float position) {
        View parralexView = page.findViewById(id);
        View parralexView2 = page.findViewById(id2);
        View parralexView3 = page.findViewById(id3);
        View parralexView4 = page.findViewById(id4);
        View parralexView5 = page.findViewById(id5);
        if (page == null) {
            Log.e("ParallaxERROR", "NO VIEW");

        } else {
            if (parralexView != null && position > -1 && position < 1) {
                float width = parralexView.getWidth();
                parralexView.setTranslationX(-(position * width * speed));
                parralexView2.setTranslationX((position * width * speed * 4));
                parralexView3.setTranslationX((position * width * speed) * 3);
                parralexView4.setTranslationX((position * width * speed) * 2);
                parralexView5.setTranslationX((position * width * speed) * 1);
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
