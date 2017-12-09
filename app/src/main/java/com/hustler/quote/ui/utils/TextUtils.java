package com.hustler.quote.ui.utils;

import android.app.Activity;
import android.content.res.TypedArray;
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

    public static String getArrayItem_of_String(Activity activity, String name, int index) {
        int arrayid;
        arrayid = activity.getResources().getIdentifier(name, "array", activity.getApplicationContext().getPackageName());
        TypedArray typedArray;
        typedArray = activity.getResources().obtainTypedArray(arrayid);
        String string_from_array = typedArray.getString(index);
        typedArray.recycle();
        return string_from_array;

    }


}
