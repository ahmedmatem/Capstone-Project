package com.example.android.pfpnotes.data.adapters;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.pfpnotes.R;

/**
 * Created by ahmed on 13/03/2018.
 */

public class NoteViewHolder extends RecyclerView.ViewHolder {
    ConstraintLayout mActionLayout;

    public ImageView mThumbnail;
    public TextView mPlace;
    public TextView mDimension;
    public TextView mPrice;

    public ImageView mEdit;
    public ImageView mDetail;

    public NoteViewHolder(View itemView) {
        super(itemView);
        mActionLayout = (ConstraintLayout) itemView.findViewById(R.id.action_layout);
        mThumbnail = (ImageView) itemView.findViewById(R.id.iv_thumb);
        mPlace = (TextView) itemView.findViewById(R.id.tv_place);
        mDimension = (TextView) itemView.findViewById(R.id.tv_dimension);
        mPrice = (TextView) itemView.findViewById(R.id.tv_price);
        mEdit = (ImageView) itemView.findViewById(R.id.btn_edit);
        mDetail = (ImageView) itemView.findViewById(R.id.btn_upload);
    }
}
