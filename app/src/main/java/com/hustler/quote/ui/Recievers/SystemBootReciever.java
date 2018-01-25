package com.hustler.quote.ui.Recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.hustler.quote.R;
import com.hustler.quote.ui.Widgets.QuoteWidgetConfigurationActivity;
import com.hustler.quote.ui.utils.Toast_Snack_Dialog_Utils;

/**
 * Created by Sayi on 26-01-2018.
 */



public class SystemBootReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

    }
//    public void onReceive(Context context, Intent intent) {
//        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
////        Toast_Snack_Dialog_Utils.show_ShortToast(QuoteWidgetConfigurationActivity.this, "DEVICE SUCCESSFULLY BOOTED");
//
//        if (sharedPreferences.getBoolean(context.getString(R.string.widget_added_key), false) == true) {
//            Log.d("PREFERNCE LOADED", String.valueOf(sharedPreferences.getBoolean(context.getString(R.string.widget_added_key), false)));
////            startServices();
//        } else {
//
//        }
//    }
}