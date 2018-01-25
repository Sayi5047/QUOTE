package com.hustler.quote.ui.Widgets;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.hustler.quote.R;
import com.hustler.quote.ui.Services.RandomQuoteUpdateService;
import com.hustler.quote.ui.activities.EditorActivity;
import com.hustler.quote.ui.activities.HomeActivity;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.utils.Toast_Snack_Dialog_Utils;

/**
 * Created by anvaya5 on 23/01/2018.
 */

public class EditWidgetProvider2 extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

    }

}
