package com.hustler.quote.ui.utils;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hustler.quote.ui.apiRequestLauncher.Constants;

/**
 * Created by Sayi on 02-12-2017.
 */

public class TextUtils {
    public static void setFont(Activity activity, TextView tv, String fontname) {
        tv.setTypeface(Typeface.createFromAsset(activity.getApplicationContext().getAssets(), fontname));
    }

    public static void setEdit_Font(Activity activity, EditText et, String fontname) {
        et.setTypeface(Typeface.createFromAsset(activity.getApplicationContext().getAssets(), fontname));
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

    public static void applyTextShadow(TextView tv, float raduis, float x, float y, int color) {
        tv.setShadowLayer(raduis, x, y, color);
    }

    public static void findText_and_applyTypeface(ViewGroup viewGroup, Activity activity) {
        int childcount = viewGroup.getChildCount();
        for (int i = 0; i < childcount; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) {
                findText_and_applyTypeface((ViewGroup) view, activity);
            } else if (view instanceof TextView) {
                setFont(activity, ((TextView) view), Constants.FONT_Sans_Bold);
            } else if (view instanceof EditText) {
                setEdit_Font(activity, ((EditText) view), Constants.FONT_Sans_Bold);
            } else {

            }
        }
    }

}
