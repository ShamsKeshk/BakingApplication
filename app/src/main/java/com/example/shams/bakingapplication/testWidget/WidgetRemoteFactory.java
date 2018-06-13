package com.example.shams.bakingapplication.testWidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.shams.bakingapplication.Constants;
import com.example.shams.bakingapplication.MainActivity;
import com.example.shams.bakingapplication.R;
import com.example.shams.bakingapplication.model.Ingredients;
import com.example.shams.bakingapplication.model.Recipes;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class WidgetRemoteFactory implements RemoteViewsService.RemoteViewsFactory {

    private Intent intent;
    private List<Ingredients> ingredients;
    private Context context;
    private int currentWidgetId;

    public WidgetRemoteFactory(Intent intent, Context context) {
        this.intent = intent;
        this.context = context;
    }

    @Override
    public void onCreate() {
        ingredients = new ArrayList<>();
        ingredients = getListOfIngredients(ingredients);
    }

    private List<Ingredients> getListOfIngredients(List<Ingredients> ingredients) {
        Gson gson = new Gson();

        String currentRecipeJson = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(Constants.KEY_SHARED_PREFERENCE_CURRENT_RECIPE_KEY, null);

        if (currentRecipeJson != null) {
            Recipes recipe = gson.fromJson(currentRecipeJson, Recipes.class);
            ingredients = recipe.getIngredients();
        }
        return ingredients;
    }


    @Override
    public void onDataSetChanged() {

        ingredients = getListOfIngredients(ingredients);
        currentWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        RecipesAppWidget.updateAppWidget(context, AppWidgetManager.getInstance(context), currentWidgetId);


    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (ingredients != null) {
            return ingredients.size();
        } else {
            return 0;
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {

        String ingredient = ingredients.get(position).getIngredient();
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);


        remoteViews.setTextViewText(R.id.tv_recipe_ingredient_widget_list_item_id, ingredient);

        //When Item from list clicked Open Main Activity
        Intent intent = new Intent(context, MainActivity.class);
        remoteViews.setOnClickFillInIntent(R.id.tv_recipe_ingredient_widget_list_item_id, intent);


        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
