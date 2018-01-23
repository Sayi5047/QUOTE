package com.hustler.quote.ui.Widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.hustler.quote.R;
import com.hustler.quote.ui.activities.EditorActivity;
import com.hustler.quote.ui.activities.HomeActivity;
import com.hustler.quote.ui.apiRequestLauncher.Constants;

/**
 * Created by anvaya5 on 23/01/2018.
 */

public class EditWidgetProvider2 extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.quote_widget_configuration_layout);
        Intent intent_edit = new Intent(context, EditorActivity.class);
        Intent intent_home = new Intent(context, HomeActivity.class);
        intent_edit.putExtra(Constants.INTENT_IS_FROM_EDIT_KEY, false);
        PendingIntent pendingIntent_edit = PendingIntent.getActivity(context, 0, intent_edit, 0);
        PendingIntent pendingIntent_home = PendingIntent.getActivity(context, 0, intent_home, 0);
        remoteView.setOnClickPendingIntent(R.id.edit_widget, pendingIntent_edit);
        remoteView.setOnClickPendingIntent(R.id.edit_widget_quotes, pendingIntent_home);

        appWidgetManager.updateAppWidget(appWidgetId, remoteView);
    }
}
