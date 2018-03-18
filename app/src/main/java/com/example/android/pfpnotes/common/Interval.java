package com.example.android.pfpnotes.common;

/**
 * Created by ahmed on 28/02/2018.
 */

public class Interval {
    private double minValue;
    private double maxValue;
    private double mValue;

    public Interval(double min, double max, double value){
        minValue = min;
        maxValue = max;
        mValue = value;
    }

    public boolean contain(double value){
        value = MathHelper.round(value, MathHelper.ROUND_PLACE_2);
        if(minValue <= value && value <= maxValue){
            return true;
        }
        return false;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public double getValue() {
        return mValue;
    }
}
