package com.example.shams.bakingapplication;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.shams.bakingapplication.adapters.RecipeAdapter;
import com.example.shams.bakingapplication.model.Recipes;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
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

    public static ArrayList<Recipes> recipesArrayList;

    RecipeAdapter recipeAdapter;
    Snackbar snackbar ;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);

        if (NetworkStatues.isConnected(this)){
            recipeAdapter = new RecipeAdapter(this);

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(recipeAdapter);

            recipeAdapter.setRecipesList(recipesArrayList);
            recipeAdapter.notifyDataSetChanged();

            retrievalListOfRecipes();
        }else {
//           snackbar = Snackbar.make(coordinatorLayout ,,Snackbar.LENGTH_LONG);
//           snackbar.setAction("Connect" , new ConnectListener());
//           snackbar.show();
            displaySnackBarConnectionError("No Internet Connection");
        }


    }

    private void displaySnackBarConnectionError(String errorMessage){
        snackbar = Snackbar.make(coordinatorLayout ,errorMessage,Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Connect" , new ConnectListener());
        snackbar.show();
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
                tvEmptyTextView.setText("Failed to Retrieve Data");
                tvEmptyTextView.setVisibility(View.VISIBLE);
//                Snackbar.make(coordinatorLayout ,"Check your Network",Snackbar.LENGTH_LONG)
//                        .show();
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
}
