package com.hustler.quote.ui.superclasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Environment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;
import android.support.v8.renderscript.*;

import com.hustler.quote.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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

    public static void showToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }


    /*Method that returns the desired font typeface object*/
    public static Typeface getZingCursive(Activity activity, String fontname) {

        return Typeface.createFromAsset(activity.getApplicationContext().getAssets(), fontname);
    }

    /*
    * method to get the array and its contents that are defined in the xml,we can retrieve array using <TypedArray>
    *    params --  @activity -- calling activity
    *               @name --  arrayname to be retrieved
    *               @index -- position of the item to be retrieved
    *               @defaultvalue -- Default value
    *
    *               get the defined arrayid with its defined name in xml
    *               next get that total array into TypedArray
    *               next get that items based on positions passed to this
    *               */
    public static int getArrayItemColor(Activity activity, String name, int index, int defaultval) {
        int arrayid;
        arrayid = activity.getResources().getIdentifier(name, "array", activity.getApplicationContext().getPackageName());
        TypedArray typedArray;
        typedArray = activity.getResources().obtainTypedArray(arrayid);
        int id = typedArray.getColor(index, defaultval);
        typedArray.recycle();
        return id;

    }



    /*Metohd to get a snap of the given layout and its location*/

}
