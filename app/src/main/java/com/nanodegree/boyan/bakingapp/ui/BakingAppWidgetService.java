package com.nanodegree.boyan.bakingapp.ui;


import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.nanodegree.boyan.bakingapp.BakingAppWidget;

public class BakingAppWidgetService extends IntentService {

    private static final String ACTION_UPDATE_WIDGET = "com.nanodegree.boyan.bakingapp.widget.action.update_widget";

    public BakingAppWidgetService() {
        super("BakingAppWidgetService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            final String[] extra = intent.getStringArrayExtra(WidgetActivity.WIDGET_INGREDIENTS);
            if (ACTION_UPDATE_WIDGET.equals(action)) {
                handleActionUpdateWidget(extra);
            }
        }
    }

    private void handleActionUpdateWidget(String[] recipe) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidget.class));


        BakingAppWidget.updateWidget(this, appWidgetManager, appWidgetIds, recipe);
    }

    public static void startActionUpdateWidget(Context context, String[] recipe) {
        Intent intent = new Intent(context, BakingAppWidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        intent.putExtra(WidgetActivity.WIDGET_INGREDIENTS, recipe);
        context.startService(intent);
    }
}
