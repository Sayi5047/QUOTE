package com.hustler.quote.ui.superclasses;

import android.app.Activity;
import android.app.Application;
import android.content.res.TypedArray;
import android.graphics.Typeface;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Sayi on 07-10-2017.
 */

public class App extends Application {
    private static Application appInstance;
    Typeface ZingCursive, ZingSans;


    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...



    }


    /*Method that returns the desired font typeface object*/
    public static Typeface applyFont(Activity activity, String fontname) {

        return Typeface.createFromAsset(activity.getApplicationContext().getAssets(), fontname);
    }

    public static int getArrayItemColor(Activity activity, String name, int index, int defaultval) {
        int arrayid;
        arrayid = activity.getResources().getIdentifier(name, "array", activity.getApplicationContext().getPackageName());
        TypedArray typedArray;
        typedArray = activity.getResources().obtainTypedArray(arrayid);
        int id = typedArray.getColor(index, defaultval);
        typedArray.recycle();
        return id;

    }
}
