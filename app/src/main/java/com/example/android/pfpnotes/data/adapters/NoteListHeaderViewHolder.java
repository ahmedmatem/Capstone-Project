package com.example.android.pfpnotes.data.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.pfpnotes.R;

/**
 * Created by ahmed on 13/03/2018.
 */

public class NoteListHeaderViewHolder extends RecyclerView.ViewHolder {
    public TextView mDate;
    public TextView mTotal;

    public NoteListHeaderViewHolder(View itemView) {
        super(itemView);
        mDate = (TextView) itemView.findViewById(R.id.tv_date);
        mTotal = (TextView) itemView.findViewById(R.id.tv_total_price);
    }
}
