package com.example.shams.bakingapplication;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.example.shams.bakingapplication.custom_matcher.RecyclerViewMatcher;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ActivityIntentTest {

    @Rule
    public IntentsTestRule<MainActivity> mainActivityIntentsTestRule
            = new IntentsTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;


    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mainActivityIntentsTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Before
    public void stubAllExternalIntents() {
        intending(not(isInternal()))
                .respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void testOnClickRecipesListItem() {

        //Click on the first Recipe in recyclerView Of MainActivity
        onView(withId(R.id.rv_recipe_recycler_view_main_activity_id))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //Check if RecipeDetailsActivity Is Opened after click
        intended(hasComponent(RecipeDetailsActivity.class.getName()));

        //Check if the first short Description of recyclerView is "Recipe Introduction"
        onView(withRecyclerView(R.id.rv_recipe_details_list_recycler_view_id)
                .atPositionOnView(0, R.id.tv_recipe_detail_short_description_text_view_id))
                .check(matches(withText("Recipe Introduction")));

        //Click on the first Step to check if the step Activity open correctly
        onView(withId(R.id.rv_recipe_details_list_recycler_view_id))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //Check if the correct Activity "Step Activity" Is Opened After Click
        intended(hasComponent(StepActivity.class.getName()));

        //Click on the Next Step Button.
        onView(withId(R.id.btn_next_step_button_id))
                .perform(click());

        //Check If the next step Description Is Correct
        onView(withId(R.id.tv_step_description_text_view_fragment_id))
                .check(matches(withText("1. Preheat the oven to 350°F. Butter a 9\" deep dish pie pan.")));

      }



    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);

        }
    }
}
