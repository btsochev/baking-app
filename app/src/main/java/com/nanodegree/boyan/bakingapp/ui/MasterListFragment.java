package com.nanodegree.boyan.bakingapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nanodegree.boyan.bakingapp.R;
import com.nanodegree.boyan.bakingapp.data.Recipe;
import com.nanodegree.boyan.bakingapp.networking.IDataReceived;
import com.nanodegree.boyan.bakingapp.networking.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MasterListFragment extends Fragment {
    @BindView(R.id.recipes_rv)
    RecyclerView recyclerView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_master_list, container, false);
        ButterKnife.bind(this, view);



        return view;
    }




}
