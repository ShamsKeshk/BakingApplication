package com.example.shams.bakingapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shams.bakingapplication.adapters.DetailsAdapter;
import com.example.shams.bakingapplication.model.Ingredients;
import com.example.shams.bakingapplication.model.Recipes;
import com.example.shams.bakingapplication.model.Steps;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeDetailsFragment.OnFragmentListItemClickListener} interface
 * to handle interaction events.
 * Use the {@link RecipeDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeDetailsFragment extends Fragment implements DetailsAdapter.DetailItemOnClickListener {


    @BindView(R.id.expand_text_view)
    ExpandableTextView expandableTextView;

    @BindView(R.id.rv_recipe_details_list_recycler_view_id)
    RecyclerView recyclerView;

    private OnFragmentListItemClickListener mListener;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    public static RecipeDetailsFragment newInstance(ArrayList<Recipes> recipesArrayList) {
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(Constants.KEY_RECIPE_PARCELABLE_ARRAY_LIST, recipesArrayList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        ButterKnife.bind(this, view);

        ArrayList<Recipes> currentRecipe;
        List<Ingredients> ingredients;
        List<Steps> steps;
        DetailsAdapter detailsAdapter;

        if (getArguments() != null) {

            currentRecipe = getArguments().getParcelableArrayList(Constants.KEY_RECIPE_PARCELABLE_ARRAY_LIST);

            assert currentRecipe != null;

            ingredients = currentRecipe.get(0).getIngredients();
            steps = currentRecipe.get(0).getSteps();

            RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            detailsAdapter = new DetailsAdapter(getActivity(), this);
            recyclerView.setAdapter(detailsAdapter);
            detailsAdapter.setStepsList(steps);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < ingredients.size(); i++) {
                stringBuilder.append("* ")
                        .append(ingredients.get(0).getQuantity())
                        .append(" ")
                        .append(ingredients.get(0).getMeasure())
                        .append(" of  ")
                        .append(ingredients.get(i).getIngredient())
                        .append("\n\n");
            }

            expandableTextView.setText(String.valueOf(stringBuilder));
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentListItemClickListener) {
            mListener = (OnFragmentListItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + getString(R.string.must_implement_on_fragment_list_item_click_listener));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClicked(Steps steps) {
        mListener.onItemClickListenerInteraction(steps.getId());
    }

    public interface OnFragmentListItemClickListener {

        void onItemClickListenerInteraction(int itemId);
    }
}
