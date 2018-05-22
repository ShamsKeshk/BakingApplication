package com.example.shams.bakingapplication.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shams.bakingapplication.R;
import com.example.shams.bakingapplication.model.Recipes;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecyclerViewHolder> {

    private List<Recipes> recipesList;
    private ListClickListenerInterface listClickListenerInterface;
    private Context context;

    public RecipeAdapter(ListClickListenerInterface listClickListenerInterface) {
        this.listClickListenerInterface = listClickListenerInterface;
    }

    public Recipes getItem(int position) {
        return recipesList.get(position);
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_list_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        if (position < getItemCount()) {

            Recipes recipe = getItem(position);


            String recipeName = recipe.getName();
            int recipeServings = recipe.getServings();
            String recipeImage = recipe.getImage();

            if (!TextUtils.isEmpty(recipeName)) {
                holder.recipeNameTextView.setText(recipeName);
            } else {
                holder.recipeNameTextView.setText(context.getString(R.string.not_provided));
            }

            if (!TextUtils.isEmpty(recipeImage)) {
                Glide.with(context).load(recipeImage).into(holder.recipeImageView);
            } else {
                Glide.with(context).load(R.drawable.baker).into(holder.recipeImageView);
            }

            if (!(recipeServings < 0)) {
                String servingsText = context.getString(R.string.servings) + recipeServings;
                holder.recipeServingsTextView.setText(servingsText);
            } else {
                holder.recipeNameTextView.setText("-");
            }

        }

    }

    @Override
    public int getItemCount() {
        if (recipesList != null) {
            return recipesList.size();
        } else {
            return 0;
        }
    }

    public void setRecipesList(List<Recipes> recipesList) {
        this.recipesList = recipesList;
        notifyDataSetChanged();
    }

    public interface ListClickListenerInterface {
        void onItemClickListener(Recipes recipe);
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_recipe_image_view_id)
        ImageView recipeImageView;

        @BindView(R.id.tv_recipe_name_text_view_id)
        TextView recipeNameTextView;

        @BindView(R.id.tv_recipe_servings_text_view_id)
        TextView recipeServingsTextView;

        public RecyclerViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Recipes recipe = recipesList.get(getAdapterPosition());
            listClickListenerInterface.onItemClickListener(recipe);
        }
    }
}
