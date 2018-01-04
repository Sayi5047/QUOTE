package com.hustler.quote.ui.utils;

import android.animation.ArgbEvaluator;
import android.app.Activity;

/**
 * Created by anvaya5 on 04/01/2018.
 */

public class ColorUtils {
    public static int getHEaderColor(int[] colors, int position, float positionOffset, Activity activity) {


        if (position == colors.length - 1) {
            return colors[position];
        } else {
            int start = colors[position];
            int end = colors[position + 1];
            int color = (int) new ArgbEvaluator().evaluate(positionOffset, start, end);
            return color;
        }
    }

}
