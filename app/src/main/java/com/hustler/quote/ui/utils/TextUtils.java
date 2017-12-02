package com.hustler.quote.ui.utils;

import android.app.Activity;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by Sayi on 02-12-2017.
 */

public class TextUtils {
    public static void setFont(Activity activity,TextView tv,String fontname)
    {
        tv.setTypeface(Typeface.createFromAsset(activity.getApplicationContext().getAssets(), fontname));
    }



}
