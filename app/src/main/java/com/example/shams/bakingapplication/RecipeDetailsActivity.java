package com.example.shams.bakingapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toolbar;

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
        assert currentBundle != null;
        recipesArrayList = currentBundle.getParcelableArrayList(Constants.KEY_RECIPE_PARCELABLE_ARRAY_LIST);

        fragmentManager = getSupportFragmentManager();

        isTwoPane = getResources().getBoolean(R.bool.isTablet);

        if (isTwoPane){
            int stepsSize = recipesArrayList.get(0).getSteps().size();
            setFragmentsIfTwoPane(recipesArrayList,stepsSize,0);

        }else {
            setRecipeDetailsFragmentOnePane(recipesArrayList);
        }
    }

    private void setFragmentsIfTwoPane(ArrayList<Recipes> recipesArrayList,int stepsSize,int currentStepId){
        RecipeDetailsFragment recipeDetailsFragment = RecipeDetailsFragment.newInstance(recipesArrayList);
        fragmentManager.beginTransaction()
                .replace(R.id.fl_details_container_fragment,recipeDetailsFragment)
                .commit();
        StepFragment stepFragment = StepFragment
                .newInstance(currentStepId,
                        stepsSize,recipesArrayList);
        fragmentManager.beginTransaction()
                .replace(R.id.fl_step_container_fragment_id,stepFragment)
                .commit();
    }

    private void setRecipeDetailsFragmentOnePane (ArrayList<Recipes> recipesArrayList){
        RecipeDetailsFragment recipeDetailsFragment = RecipeDetailsFragment.newInstance(recipesArrayList);
        fragmentManager.beginTransaction()
                .replace(R.id.fl_details_container_fragment,recipeDetailsFragment)
                .commit();
    }

    @Override
    public void onItemClickListenerInteraction(int itemId) {
        int stepCellsNUmber = recipesArrayList.get(0).getSteps().size();

        if (isTwoPane) {
            setFragmentsIfTwoPane(recipesArrayList,stepCellsNUmber,itemId);
        } else {
            Intent intent = new Intent(this, StepActivity.class);
            intent.putExtra(Constants.KEY_CURRENT_STEP_ID, itemId);
            intent.putExtra(Constants.KEY_STEPS_SIZE_NUMBER, stepCellsNUmber);
            intent.putParcelableArrayListExtra(Constants.KEY_RECIPE_PARCELABLE_ARRAY_LIST, recipesArrayList);
            startActivity(intent);
        }
    }
}
