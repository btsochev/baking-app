package com.nanodegree.boyan.bakingapp.data;

import org.parceler.Parcel;
import java.util.List;
import lombok.Data;


@Parcel
@Data
public class Recipe{
    private int id;
    private String name;
    private List<Ingredient> ingredients;
    private List<Step> steps;
    private int servings;
    private String image;

    public Recipe(){

    }
}
