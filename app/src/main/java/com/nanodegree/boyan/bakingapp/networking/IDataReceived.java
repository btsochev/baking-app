package com.nanodegree.boyan.bakingapp.networking;


import com.nanodegree.boyan.bakingapp.data.Recipe;

import java.util.List;

public interface IDataReceived {
    void onDataReceived(List<Recipe> recipes);
}
