package com.maralexbar.lpwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {




    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        SharedPreferences prefs;
        int m,c;
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        prefs = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        m=prefs.getInt("max",1);
        c=prefs.getInt("current",1);


        if ((c*100)/m > 50 ) {

            views.setProgressBar(R.id.lpbar_full,m,c,false);
            views.setViewVisibility(R.id.lpbar_full,1);
            views.setViewVisibility(R.id.lpbar_red,View.INVISIBLE);
            views.setViewVisibility(R.id.lpbar_med,View.INVISIBLE);

        }


        if ((c*100)/m <= 50 && (c*100)/m >= 10 ) {

            views.setProgressBar(R.id.lpbar_med,m,c,false);
            views.setViewVisibility(R.id.lpbar_med,1);
            views.setViewVisibility(R.id.lpbar_red,View.INVISIBLE);
            views.setViewVisibility(R.id.lpbar_full,View.INVISIBLE);

        }


        if ((c*100)/m < 10) {

            views.setProgressBar(R.id.lpbar_red,m,c,false);
            views.setViewVisibility(R.id.lpbar_red,1);
            views.setViewVisibility(R.id.lpbar_full, View.INVISIBLE);
            views.setViewVisibility(R.id.lpbar_med,View.INVISIBLE);

        }


        //CharSequence widgetText = "LP:";
        // Construct the RemoteViews object


        views.setTextViewText(R.id.lp_text, "LP");

        views.setTextViewText(R.id.current, Integer.toString(c)+"/"+Integer.toString(m));





        //views.setProgressBar(R.id.lpbar,80,75,false);
        //views.setViewVisibility(R.id.lpbar_red,1);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

