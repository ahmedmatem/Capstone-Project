package com.example.android.pfpnotes.common;

/**
 * Created by ahmed on 28/02/2018.
 */

public class MathHelper {
    public static final int ROUND_PLACE_2 = 2;

    public static double round(double value, int places){
        double factor = Math.pow(10, places);
        value = value * factor;
        value = Math.round(value);
        return (double) value / factor;
    }
}
