package com.nanodegree.boyan.bakingapp.ui;

import android.app.Application;

import com.nanodegree.boyan.bakingapp.data.Recipe;

import java.util.ArrayList;


public class BakingApp extends Application {
    public static ArrayList<Recipe> recipes = new ArrayList<>();
}
