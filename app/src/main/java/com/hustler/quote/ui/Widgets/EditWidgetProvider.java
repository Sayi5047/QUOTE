package com.hustler.quote.ui.Widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.hustler.quote.R;
import com.hustler.quote.ui.activities.EditorActivity;
import com.hustler.quote.ui.activities.HomeActivity;
import com.hustler.quote.ui.activities.MainActivity;
import com.hustler.quote.ui.apiRequestLauncher.Constants;

/**
 * Created by Sayi on 22-01-2018.
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
public class EditWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.editor_widget_layout);
        Intent intent_edit = new Intent(context, EditorActivity.class);
        Intent intent_home = new Intent(context, MainActivity.class);
        intent_edit.putExtra(Constants.INTENT_IS_FROM_EDIT_KEY, 0);
        PendingIntent pendingIntent_edit = PendingIntent.getActivity(context, 0, intent_edit, 0);
        PendingIntent pendingIntent_home = PendingIntent.getActivity(context, 0, intent_home, 0);
        remoteView.setOnClickPendingIntent(R.id.edit_widget, pendingIntent_edit);
        remoteView.setOnClickPendingIntent(R.id.edit_widget_quotes, pendingIntent_home);
        appWidgetManager.updateAppWidget(appWidgetId, remoteView);
    }


}
