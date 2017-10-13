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
    public static int getArrayItemColor(Activity activity,String name,int index,int defaultval){
        int arrayid;
        arrayid = activity.getResources().getIdentifier(name,"array",activity.getApplicationContext().getPackageName());
        TypedArray typedArray;
        typedArray = activity.getResources().obtainTypedArray(arrayid);
        int id=typedArray.getColor(index,defaultval);
        typedArray.recycle();
        return id;

    }

    public static String getArrayItemFont(Activity activity,String name,int index,int defaultval){
        int arrayid;
        arrayid = activity.getResources().getIdentifier(name,"array",activity.getApplicationContext().getPackageName());
        TypedArray typedArray;
        typedArray = activity.getResources().obtainTypedArray(arrayid);
        String id=typedArray.getString(index);
        typedArray.recycle();
        return id;

    }
}
