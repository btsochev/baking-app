package com.nanodegree.boyan.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.nanodegree.boyan.bakingapp.R;
import com.nanodegree.boyan.bakingapp.data.Recipe;
import com.nanodegree.boyan.bakingapp.data.Step;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        if (savedInstanceState == null) {
            Recipe recipe = Parcels.unwrap(getIntent().getParcelableExtra("recipe"));
            int stepPosition = getIntent().getIntExtra("step_position", 0);
            Bundle arguments = new Bundle();
            arguments.putParcelable("recipe", Parcels.wrap(recipe));
            arguments.putInt("step_position", stepPosition);
            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, DetailActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
