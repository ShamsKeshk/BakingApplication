package com.example.shams.bakingapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.shams.bakingapplication.adapters.RecipeAdapter;
import com.example.shams.bakingapplication.model.Recipes;
import com.example.shams.bakingapplication.utils.NetworkStatues;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements RecipeAdapter.ListClickListenerInterface {

    @BindView(R.id.rv_recipe_recycler_view_main_activity_id)
    RecyclerView recyclerView;
    @BindView(R.id.progress_par_main_activity_id)
    ProgressBar progressBar;
    @BindView(R.id.tv_empty_text_view_main_activity_id)
    TextView tvEmptyTextView;
    @BindView(R.id.coordinator_layout_id)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.swipe_refresh_layout_id_main_activity)
    SwipeRefreshLayout swipeRefreshLayout;

    public ArrayList<Recipes> recipesArrayList;

    RecipeAdapter recipeAdapter;
    Snackbar snackbar ;

    private IdlingResource idlingResource;

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 180;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if (noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, calculateNoOfColumns(this));
        recipeAdapter = new RecipeAdapter(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recipeAdapter);
        recipeAdapter.setRecipesList(recipesArrayList);
        recipeAdapter.notifyDataSetChanged();

        requestRecipesList();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestRecipesList();
                swipeRefreshLayout.setRefreshing(false);
            }

        });

    }

    private void displaySnackBarConnectionError(String errorMessage){
        snackbar = Snackbar.make(coordinatorLayout ,errorMessage,Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Connect" , new ConnectListener());
        snackbar.show();
    }

    private void requestRecipesList() {

        if (NetworkStatues.isConnected(this)) {

            retrievalListOfRecipes();
            if (snackbar != null) {
                snackbar.dismiss();
            }
        } else {
            if (recipesArrayList == null) {
                displayErrorText(getString(R.string.no_internet_connection));
            }
            displaySnackBarConnectionError(getString(R.string.failed_to_retrieve_data));
        }
    }

    public void retrievalListOfRecipes() {

        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api apiInterface = retrofit.create(Api.class);

        final Call<ArrayList<Recipes>> recipeListCall = apiInterface.getRecipes();

        recipeListCall.enqueue(new Callback<ArrayList<Recipes>>() {

            @Override
            public void onResponse(Call<ArrayList<Recipes>> call, Response<ArrayList<Recipes>> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    tvEmptyTextView.setVisibility(View.GONE);
                    recipesArrayList = response.body();
                    recipeAdapter.setRecipesList(recipesArrayList);
                    recipeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipes>> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                displayErrorText("Failed to Retrieve Data");

                displaySnackBarConnectionError("Check your Network");

            }
        });

    }

    @Override
    public void onItemClickListener(Recipes recipe) {
        Bundle bundle = new Bundle();
        ArrayList<Recipes> currentRecipe = new ArrayList<>();
        currentRecipe.add(recipe);
        bundle.putParcelableArrayList(Constants.KEY_RECIPE_PARCELABLE_ARRAY_LIST,currentRecipe);
        Intent intent = new Intent(this ,RecipeDetailsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    public class ConnectListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent settingsIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            startActivity(settingsIntent);
        }
    }

    private void displayErrorText(String message) {
        tvEmptyTextView.setText(message);
        tvEmptyTextView.setVisibility(View.VISIBLE);
    }

    public ArrayList<Recipes> getRecipesArrayList() {
        return recipesArrayList;
    }
}
