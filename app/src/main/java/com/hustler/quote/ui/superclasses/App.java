package com.hustler.quote.ui.superclasses;

import android.app.Activity;
import android.app.Application;
import android.widget.Toast;

/**
 * Created by Sayi on 07-10-2017.
 */

public class App extends Application{
     private static Application appInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance=this;
    }

    public static void showToast(Activity activity,String message) {
        Toast.makeText(activity,message,Toast.LENGTH_SHORT).show();
    }
}
