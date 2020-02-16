package com.hustler.quote.ui.utils;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

/**
 * Created by Sayi on 05-12-2017.
 */
/*   Copyright [2018] [Sayi Manoj Sugavasi]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.*/
public class Toast_Snack_Dialog_Utils {

    public interface Alertdialoglistener {
        void onPositiveSelection();

        void onNegativeSelection();
    }

    public static void show_ShortToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

    }

    public static void show_LongToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();

    }

    public static void createDialog(Activity activity, String title, String message, String negative, String positive, @NonNull final Alertdialoglistener alertdialoglistener) {
        final androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(activity)
                .setTitle(title).setMessage(message).setPositiveButton(positive, (dialog, which) -> {
                    alertdialoglistener.onPositiveSelection();
                    dialog.dismiss();
                }).setNegativeButton(negative, (dialog, which) -> {
                    alertdialoglistener.onNegativeSelection();
                    dialog.dismiss();
                });
        alertDialog.setCancelable(true);
        alertDialog.show();

    }
}
