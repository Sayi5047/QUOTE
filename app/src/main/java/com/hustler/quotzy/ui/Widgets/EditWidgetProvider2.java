package com.hustler.quotzy.ui.Widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.hustler.quotzy.R;

/**
 * Created by anvaya5 on 23/01/2018.
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
public class EditWidgetProvider2 extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("ON UPDATE","TRUE");
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(context.getString(R.string.widget_added_key),true).apply();

    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d("ON ENABLED","TRUE");

        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(context.getString(R.string.widget_added_key),true).apply();

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.d("ON DELETED","FALSE");

        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(context.getString(R.string.widget_added_key),false).apply();
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        Log.d("ON DISABLED","FALSE");

        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(context.getString(R.string.widget_added_key),false).apply();

        super.onDisabled(context);
    }
}
