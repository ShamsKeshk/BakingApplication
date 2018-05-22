package com.example.shams.bakingapplication;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.shams.bakingapplication.model.Recipes;

import java.util.ArrayList;

public class StepActivity extends AppCompatActivity {

    private int mCurrentStepId;
    private int mStepsCellsNumber;
    private ArrayList<Recipes> mCurrentRecipesArrayList;

    FragmentManager fragmentManager;
    StepFragment stepFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        Bundle bundle = getIntent().getExtras();
        mCurrentStepId = bundle.getInt(Constants.KEY_CURRENT_STEP_ID);
        mStepsCellsNumber = bundle.getInt(Constants.KEY_STEP_CELLS_NUMBER);
        mCurrentRecipesArrayList = bundle.getParcelableArrayList(Constants.KEY_RECIPE_PARCELABLE_ARRAY_LIST);



        fragmentManager = getSupportFragmentManager();
        stepFragment =
                StepFragment.newInstance(mCurrentStepId,mStepsCellsNumber,mCurrentRecipesArrayList);

        fragmentManager.beginTransaction()
                .replace(R.id.fl_step_container_fragment_id,stepFragment).commit();

    }


}
