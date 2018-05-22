package com.example.shams.bakingapplication;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.shams.bakingapplication.model.Recipes;

import java.util.ArrayList;

public class RecipeDetailsActivity extends AppCompatActivity
        implements RecipeDetailsFragment.OnFragmentListItemClickListener {

    Bundle currentBundle;
    ArrayList<Recipes> recipesArrayList;

    boolean isTwoPane;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        currentBundle = getIntent().getExtras();
        recipesArrayList = currentBundle.getParcelableArrayList(Constants.KEY_RECIPE_PARCELABLE_ARRAY_LIST);

        fragmentManager = getSupportFragmentManager();

        if (isTwoPane){

        }else {
            RecipeDetailsFragment recipeDetailsFragment = RecipeDetailsFragment.newInstance(recipesArrayList);
            fragmentManager.beginTransaction()
                    .replace(R.id.fl_details_container_fragment,recipeDetailsFragment)
                    .commit();
        }
    }

    @Override
    public void onItemClickListenerInteraction(int itemId) {
        int stepCellsNUmber = recipesArrayList.get(0).getSteps().size();
        Intent intent = new Intent(this,StepActivity.class);
        intent.putExtra(Constants.KEY_CURRENT_STEP_ID,itemId);
        intent.putExtra(Constants.KEY_STEPS_SIZE_NUMBER,stepCellsNUmber);
        intent.putParcelableArrayListExtra(Constants.KEY_RECIPE_PARCELABLE_ARRAY_LIST,recipesArrayList);
        startActivity(intent);
    }
}
