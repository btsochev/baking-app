package com.nanodegree.boyan.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nanodegree.boyan.bakingapp.R;
import com.nanodegree.boyan.bakingapp.data.Ingredient;
import com.nanodegree.boyan.bakingapp.data.Recipe;
import com.nanodegree.boyan.bakingapp.data.Step;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeDetailFragment extends Fragment implements StepsAdapter.StepsOnClickHandler {
    @BindView(R.id.recipe_list_rv)
    RecyclerView recyclerView;

    @BindView(R.id.ingredients)
    TextView ingredients;

    private OnStepClickListener stepClickListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            stepClickListener = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(String.format("OnStepClickListener Interface not found", context.toString()));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, rootView);

        Recipe mRecipe = Parcels.unwrap(getArguments().getParcelable("recipe"));

        for (int i = 0; i < mRecipe.getIngredients().size(); i++) {
            Ingredient ingredient = mRecipe.getIngredients().get(i);
            ingredients.append(String.format("%s %s %s", String.valueOf(ingredient.getQuantity()), ingredient.getMeasure(), ingredient.getIngredient()));
            if (i < mRecipe.getIngredients().size() -1)
                ingredients.append("\n");
        }

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        StepsAdapter adapter = new StepsAdapter(mRecipe.getSteps(), this);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onClick(int position) {
        stepClickListener.onStepClicked(position);
    }

    public interface OnStepClickListener {
        void onStepClicked(int stepPosition);
    }
}
