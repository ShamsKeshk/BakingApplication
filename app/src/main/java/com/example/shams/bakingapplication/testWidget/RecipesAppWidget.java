package com.example.shams.bakingapplication.testWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.shams.bakingapplication.MainActivity;
import com.example.shams.bakingapplication.R;

/**
 * Implementation of App Widget functionality.
 */
public class RecipesAppWidget extends AppWidgetProvider {

    private static RemoteViews remoteViews;
    private int[] currentWidgetIds;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        startServiceToDisplayList(appWidgetId,context);

        handleRemoteViewClick(context);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private static void startServiceToDisplayList(int appWidgetId,Context context){
        Intent recipeService = new Intent(context , WidgetService.class);
        recipeService.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID , appWidgetId);

        remoteViews = new RemoteViews(context.getPackageName() , R.layout.recipes_app_widget);

        remoteViews.setRemoteAdapter(R.id.lv_recipes_ingredient_widget_list_id ,recipeService);

    }

    private static void handleRemoteViewClick(Context context){
        Intent intent = new Intent(context , MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.lv_recipes_ingredient_widget_list_id , pendingIntent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {

            startServiceToDisplayList(appWidgetId,context);

            handleRemoteViewClick(context);

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

    @Override
    public void onReceive(Context context, Intent intent) {
        currentWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
        if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)){
            onUpdate(context,AppWidgetManager.getInstance(context),currentWidgetIds);
            AppWidgetManager.getInstance(context)
                    .notifyAppWidgetViewDataChanged(currentWidgetIds ,
                            R.id.lv_recipes_ingredient_widget_list_id);
        }
        super.onReceive(context, intent);
    }
}

