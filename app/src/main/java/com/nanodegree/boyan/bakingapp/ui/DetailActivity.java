package com.nanodegree.boyan.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.nanodegree.boyan.bakingapp.R;

import com.nanodegree.boyan.bakingapp.data.Ingredient;
import com.nanodegree.boyan.bakingapp.data.Recipe;
import com.nanodegree.boyan.bakingapp.data.Step;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.app.NavUtils.navigateUpFromSameTask;


public class DetailActivity extends AppCompatActivity implements RecipeDetailFragment.OnStepClickListener {

    private boolean mTwoPane;
    private boolean isInDetail;
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        mRecipe = Parcels.unwrap(getIntent().getParcelableExtra("recipe"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Recipe details");


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.step_detail_container) != null) {
            mTwoPane = true;
        }

        if (savedInstanceState == null){
            Bundle arguments = new Bundle();
            arguments.putParcelable("recipe", Parcels.wrap(mRecipe));
            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setArguments(arguments);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_recipe_detail, recipeDetailFragment)
                    .commit();

        }else{

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStepClicked(int stepPosition) {
        isInDetail = true;
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putParcelable("recipe", Parcels.wrap(mRecipe));
                arguments.putInt("step_position", stepPosition);
                StepDetailFragment fragment = new StepDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.step_detail_container, fragment)
                        .commit();
            } else {
                Context context = getApplicationContext();
                Intent intent = new Intent(context, StepActivity.class);
                intent.putExtra("recipe", Parcels.wrap(mRecipe));
                intent.putExtra("step_position", stepPosition);

                context.startActivity(intent);
            }
    }
}
