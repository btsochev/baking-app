package com.nanodegree.boyan.bakingapp.data;

import org.parceler.Parcel;
import lombok.Data;


@Parcel
@Data
public class Step {
    private int id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    public Step(){}
}
