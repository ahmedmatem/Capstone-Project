package com.example.android.pfpnotes.models;

import android.content.Context;

import com.example.android.pfpnotes.common.MathHelper;

/**
 * Created by ahmed on 11/03/2018.
 */

public class Dimension {
    public static final String DELIMITER_SIGN = "x";
    public static final int SQUARE_CM_TO_SQUARE_M = 10000;
    // 20x30x2x10
    // e.g. 20{width}x30{height}x2{layers|copies}[x10{copies}]
    private int mWidth;
    private int mHeight;
    private int mLayers;
    private int mCopies;

    public Dimension(String dimensionString) {
        resolveDimension(dimensionString);
    }

    private void resolveDimension(String dimensionString) {
        if(dimensionString == null){
            return;
        }

        mLayers = 1;
        mCopies = 1;

        String[] splitDimension = dimensionString.split(DELIMITER_SIGN);
        mWidth = Integer.valueOf(splitDimension[0]);
        mHeight = Integer.valueOf(splitDimension[1]);

        switch (splitDimension.length){
            case 3:
                int value = Integer.valueOf(splitDimension[2]);
                if(value == 2){
                    mLayers = value;
                } else {
                    mCopies = value;
                }
                break;
            case 4:
                mLayers = Integer.valueOf(splitDimension[2]);
                mCopies = Integer.valueOf(splitDimension[3]);
                break;
        }
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public int getLayers() {
        return mLayers;
    }

    public void setLayers(int layers) {
        mLayers = layers;
    }

    public int getCopies() {
        return mCopies;
    }

    public void setCopies(int copies) {
        mCopies = copies;
    }

    public double getSquare() {
        return (double) mWidth * mHeight / SQUARE_CM_TO_SQUARE_M;
    }
}
