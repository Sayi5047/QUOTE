package com.hustler.quote.ui.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Sayi on 05-12-2017.
 */

public class ToastSnackDialogUtils {

    public static void show_ShortToast(Activity activity,String message){
        Toast.makeText(activity,message,Toast.LENGTH_SHORT).show();

    }
    public static void show_LongToast(Activity activity,String message){
        Toast.makeText(activity,message,Toast.LENGTH_LONG).show();

    }
}
