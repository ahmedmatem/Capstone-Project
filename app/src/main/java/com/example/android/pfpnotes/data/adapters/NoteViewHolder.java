package com.example.android.pfpnotes.data.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.pfpnotes.R;
import com.example.android.pfpnotes.models.Item;
import com.example.android.pfpnotes.models.NoteItem;

/**
 * Created by ahmed on 13/03/2018.
 */

public class NoteViewHolder extends RecyclerView.ViewHolder {
    public ImageView mThumbnail;
    public TextView mPlace;
    public TextView mDimension;
    public TextView mPrice;

    public NoteViewHolder(View itemView) {
        super(itemView);
        mThumbnail = (ImageView) itemView.findViewById(R.id.iv_thumb);
        mPlace = (TextView) itemView.findViewById(R.id.tv_place);
        mDimension = (TextView) itemView.findViewById(R.id.tv_dimension);
        mPrice = (TextView) itemView.findViewById(R.id.tv_price);
    }

    public void bind(final NoteItem item, final NoteAdapter.OnItemClickListener listener){
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onItemClick(item);
                }
            }
        });
    }
}