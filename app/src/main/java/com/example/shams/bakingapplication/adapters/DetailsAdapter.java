package com.example.shams.bakingapplication.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shams.bakingapplication.R;
import com.example.shams.bakingapplication.model.Steps;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailsRecyclerViewHolder> {


    private Context context;
    private List<Steps> stepsList;
    private DetailItemOnClickListener mOnItemClickListener;

    public DetailsAdapter(Context context, DetailItemOnClickListener onClickListener) {
        this.context = context;
        this.mOnItemClickListener = onClickListener;
    }

    private Steps getItem(int position) {
        return stepsList.get(position);
    }

    @NonNull
    @Override
    public DetailsRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.details_list_item, parent, false);
        return new DetailsRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsRecyclerViewHolder holder, int position) {
        if (position < getItemCount()) {
            Steps step = getItem(position);

            String shortDescription = step.getShortDescription();

            holder.detailStepsTextView.setText(shortDescription);
        }

    }

    @Override
    public int getItemCount() {
        if (stepsList != null) {
            return stepsList.size();
        } else {
            return 0;
        }
    }

    public void setStepsList(List<Steps> stepsList) {
        this.stepsList = stepsList;
        notifyDataSetChanged();
    }

    public interface DetailItemOnClickListener {
        void onItemClicked(Steps steps);
    }

    public class DetailsRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_recipe_detail_short_description_text_view_id)
        TextView detailStepsTextView;

        public DetailsRecyclerViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnItemClickListener.onItemClicked(stepsList.get(getAdapterPosition()));
        }
    }
}
