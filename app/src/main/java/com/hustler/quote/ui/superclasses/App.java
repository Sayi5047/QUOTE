package com.hustler.quote.ui.superclasses;

import android.app.Activity;
import android.app.Application;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.widget.Toast;

import com.hustler.quote.R;

/**
 * Created by Sayi on 07-10-2017.
 */

public  class App extends Application{
     private static Application appInstance;
    Typeface ZingCursive,ZingSans;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance=this;
    }

    public static void showToast(Activity activity,String message) {
        Toast.makeText(activity,message,Toast.LENGTH_SHORT).show();
    }

    public static Typeface getZingCursive(Activity activity,String fontname)
    {

        return Typeface.createFromAsset(activity.getApplicationContext().getAssets(), fontname);
    }
}
