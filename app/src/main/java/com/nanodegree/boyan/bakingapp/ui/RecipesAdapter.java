package com.nanodegree.boyan.bakingapp.ui;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nanodegree.boyan.bakingapp.R;
import com.nanodegree.boyan.bakingapp.data.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {
    private Context mContext;
    private List<Recipe> mData;
    private RecipeClickedHandler mClickHandler;

    public interface RecipeClickedHandler {
        void onRecipeClicked(Recipe recipe);
    }

    public RecipesAdapter(Context context, List<Recipe> data, RecipeClickedHandler clickedHandler) {
        mContext = context;
        mData = data;
        this.mClickHandler = clickedHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.recipe_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe recipe = mData.get(position);
        holder.recipe = recipe;
        holder.name.setText(recipe.getName());
        holder.servingsCount.setText(String.format("Servings: %d", recipe.getServings()));


        if (recipe.getImage().isEmpty()) {
            holder.image.setImageResource(R.drawable.default_recipe_image);
        } else {
            Picasso.with(mContext)
                    .load(recipe.getImage())
                    .placeholder(R.drawable.default_recipe_image)
                    .error(R.drawable.default_recipe_image)
                    .into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.image)
        ImageView image;

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.servings_count)
        TextView servingsCount;

        Recipe recipe;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickHandler.onRecipeClicked(recipe);
        }

    }


}
