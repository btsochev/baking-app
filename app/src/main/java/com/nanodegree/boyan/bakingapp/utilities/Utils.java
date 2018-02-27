package com.nanodegree.boyan.bakingapp.utilities;


import android.content.res.Resources;

public class Utils {
    public static float convertDpToPixel(float dips) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return dips * scale;
    }
}
