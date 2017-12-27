package com.hustler.quote.ui.superclasses;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.Log;

import com.hustler.quote.R;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.database.QuotesDbHelper;
import com.hustler.quote.ui.pojo.Quote;

import java.util.ArrayList;

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


    }





    /*Method that returns the desired font typeface object*/
    public static Typeface getZingCursive(Activity activity, String fontname) {

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
