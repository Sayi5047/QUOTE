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

//    public static void showAlertWithListener(Activity activity){
//        AlertDialog.Builder alertDialog=new AlertDialog.Builder(activity);
//        alertDialog.create();
//        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        })
//    }

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

    public static String getArrayItemFont(Activity activity, String name, int index) {
        int arrayid;
        arrayid = activity.getResources().getIdentifier(name, "array", activity.getApplicationContext().getPackageName());
        TypedArray typedArray;
        typedArray = activity.getResources().obtainTypedArray(arrayid);
        String id = typedArray.getString(index);
        typedArray.recycle();
        return id;

    }

    /*Metohd to get a snap of the given layout and its location*/
    public static File savetoDevice(final ViewGroup layout) {
        final File[] filetoReturn = new File[1];
//        Bitmap bitmap = layout.getDrawingCache();
        layout.buildDrawingCache(true);
        Bitmap bitmap = layout.getDrawingCache(true).copy(Bitmap.Config.RGB_565, false);
        layout.destroyDrawingCache();
//        layout.setDrawingCacheEnabled(false);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        File file = new File(new StringBuilder().append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES))
                .append(File.separator)
                .append("QUOTES--")
//                        .append(quote.getAuthor())
                .append(System.currentTimeMillis())
                .append(".jpeg")
                .toString());
        filetoReturn[0] = file;
        Log.d("ImageLocation -->", file.toString());
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
            fileOutputStream.close();
//                    App.showToast(QuoteDetailsActivity.this,getString(R.string.image_saved));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        new Thread() {
//            @Override
//            public void run() {
//
//
//            }
//        }.start();
        return filetoReturn[0];

    }

}
