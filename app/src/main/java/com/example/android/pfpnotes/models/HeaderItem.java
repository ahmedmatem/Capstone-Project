package com.example.android.pfpnotes.models;

/**
 * Created by ahmed on 11/03/2018.
 */

public class HeaderItem extends Item {
    private String mDate;
    private Double mTotalPrice;

    public HeaderItem(String date) {
        mDate = date;
    }

    public HeaderItem(String date, Double totalPrice) {
        this(date);
        mTotalPrice = totalPrice;
    }

    @Override
    public int getType() {
        return Item.TYPE_HEADER;
    }

    public String getDate() {
        return mDate;
    }

    public Double getTotalPrice() {
        return mTotalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        mTotalPrice = totalPrice;
    }
}
