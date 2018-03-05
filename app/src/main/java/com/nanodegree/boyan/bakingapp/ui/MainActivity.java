package com.nanodegree.boyan.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import com.nanodegree.boyan.bakingapp.R;
import com.nanodegree.boyan.bakingapp.data.Recipe;
import com.nanodegree.boyan.bakingapp.networking.IDataReceived;
import com.nanodegree.boyan.bakingapp.networking.NetworkUtils;
import com.nanodegree.boyan.bakingapp.utilities.Utils;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements IDataReceived, RecipesAdapter.RecipeClickedHandler {
    @BindView(R.id.recipes_rv)
    RecyclerView recyclerView;

    private List<Recipe> recipesData = new ArrayList<>();
    private RecipesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfColumns());
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
        }

        adapter = new RecipesAdapter(this, recipesData, this);
        recyclerView.setAdapter(adapter);

        NetworkUtils.fetchRecipes(this, this);
    }

    @Override
    public void onDataReceived(List<Recipe> recipes) {
        recipesData.clear();
        recipesData.addAll(recipes);
        BakingApp.recipes.clear();
        BakingApp.recipes.addAll(recipes);
        adapter.notifyDataSetChanged();
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float widthDivider = Utils.convertDpToPixel(300f);
        int width = displayMetrics.widthPixels;
        int nColumns = (int) (width / widthDivider);
        if (nColumns < 2)
            return 2;

        return nColumns;
    }

    @Override
    public void onRecipeClicked(Recipe recipe) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("recipe", Parcels.wrap(recipe));
        startActivity(intent);
    }
}
