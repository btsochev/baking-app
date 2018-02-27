package com.nanodegree.boyan.bakingapp.data;

import org.parceler.Parcel;
import lombok.Data;


@Parcel
@Data
public class Ingredient {
    private double quantity;
    private String measure;
    private String ingredient;

    public Ingredient(){

    }
}
