package com.hustler.quote.ui.utils;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.support.v4.content.ContextCompat;

import com.hustler.quote.R;

/**
 * Created by anvaya5 on 04/01/2018.
 */

public class ColorUtils {
    public static int getHEaderColor(int position, float positionOffset, Activity activity) {
        int[] colors;

        colors = new int[]{ContextCompat.getColor(activity, R.color.colorPrimaryDark), ContextCompat.getColor(activity, R.color.colorAccent), ContextCompat.getColor(activity, R.color.primary_dark), ContextCompat.getColor(activity, R.color.textColor)};

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
