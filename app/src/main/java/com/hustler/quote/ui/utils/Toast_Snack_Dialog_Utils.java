package com.hustler.quote.ui.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

/**
 * Created by Sayi on 05-12-2017.
 */

public class Toast_Snack_Dialog_Utils {

    public interface Alertdialoglistener {
        void onPositiveselection();

        void onNegativeSelection();
    }

    public static void show_ShortToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

    }

    public static void show_LongToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();

    }

    public static void createDialog(Activity activity, String title, String message, String negative, String positive, final Alertdialoglistener alertdialoglistener) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity)
                .setTitle(title).setMessage(message).setPositiveButton(positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertdialoglistener.onPositiveselection();
                        dialog.dismiss();
                    }
                }).setNegativeButton(negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertdialoglistener.onNegativeSelection();
                        dialog.dismiss();
                    }
                });
        alertDialog.setCancelable(true);
        alertDialog.show();

    }
}
