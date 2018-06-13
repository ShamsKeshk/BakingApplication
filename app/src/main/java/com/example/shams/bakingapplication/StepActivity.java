package com.example.shams.bakingapplication;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.shams.bakingapplication.model.Recipes;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepActivity extends AppCompatActivity {

    private int mCurrentStepId;
    private int mStepsCellsNumber;
    private ArrayList<Recipes> mCurrentRecipesArrayList;

    FragmentManager fragmentManager;
    StepFragment stepFragment;

    @BindView(R.id.btn_next_step_button_id)
    Button btnNextStep;
    @BindView(R.id.btn_previous_step_button_id)
    Button btnPreviousStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        mCurrentStepId = bundle.getInt(Constants.KEY_CURRENT_STEP_ID);
        mStepsCellsNumber = bundle.getInt(Constants.KEY_STEPS_SIZE_NUMBER);
        mCurrentRecipesArrayList = bundle.getParcelableArrayList(Constants.KEY_RECIPE_PARCELABLE_ARRAY_LIST);

        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            displayFragment();
        }

        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextStep();
            }
        });

        btnPreviousStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousStep();
            }
        });

    }

    private void nextStep(){
        if (mCurrentStepId < ( mStepsCellsNumber - 1)){
            mCurrentStepId ++;
            displayFragment();
        }
    }

    private void previousStep(){
        if (mCurrentStepId > 0){
            mCurrentStepId --;
            displayFragment();
        }
    }

    private void displayFragment(){
        stepFragment =
                StepFragment.newInstance(mCurrentStepId,mStepsCellsNumber,mCurrentRecipesArrayList);

        fragmentManager.beginTransaction()
                .replace(R.id.fl_step_container_fragment_id,stepFragment).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.KEY_CURRENT_STEP_ID , mCurrentStepId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mCurrentStepId = savedInstanceState.getInt(Constants.KEY_CURRENT_STEP_ID);
        }
    }
}
