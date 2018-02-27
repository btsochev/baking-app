package com.nanodegree.boyan.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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


public class RecipeListActivity extends AppCompatActivity {
    @BindView(R.id.recipe_list_rv)
    RecyclerView recyclerView;

    @BindView(R.id.ingredients)
    TextView ingredients;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);

        Recipe recipe = (Recipe) Parcels.unwrap(getIntent().getParcelableExtra("recipe"));

        for (Ingredient ingredient : recipe.getIngredients()){
            ingredients.append(String.format("%s %s %s \n", String.valueOf(ingredient.getQuantity()), ingredient.getMeasure(), ingredient.getIngredient()));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Recipe details");


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.recipe_detail_container) != null) {
            mTwoPane = true;
        }

        StepsAdapter adapter = new StepsAdapter(this, recipe.getSteps(), mTwoPane);
        recyclerView.setAdapter(adapter);

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


    public static class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

        private final RecipeListActivity mParentActivity;
        private final List<Step> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Step item = (Step) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putInt(RecipeDetailFragment.ARG_ITEM_ID, item.getId());
                    RecipeDetailFragment fragment = new RecipeDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipe_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, RecipeDetailActivity.class);
                    intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, item.getId());

                    context.startActivity(intent);
                }
            }
        };

        StepsAdapter(RecipeListActivity parent, List<Step> items, boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            int positionToShow = mValues.get(position).getId() + 1;
            holder.mIdView.setText(String.valueOf(positionToShow));
            holder.mContentView.setText(mValues.get(position).getShortDescription());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.id_text)
            TextView mIdView;
            @BindView(R.id.content)
            TextView mContentView;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}
