package com.example.shams.bakingapplication;


import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.shams.bakingapplication.customEspress.RecyclerViewMatcher;
import com.example.shams.bakingapplication.model.Recipes;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Test
    public void testList() {

       // onView(withId(R.id.rv_recipe_recycler_view_main_activity_id))
              //  .perform(RecyclerViewActions.scrollToPosition(1));

       // onView(withText("Nutella Pie")).check(matches(isDisplayed()));
                //.check(matches(hasDescendant(withText("Nutella Pie"))));

        onView(withRecyclerView(R.id.rv_recipe_recycler_view_main_activity_id)
                .atPositionOnView(0, R.id.tv_recipe_name_text_view_id))
                .check(matches(withText("Nutella Pie")));
        }
}
