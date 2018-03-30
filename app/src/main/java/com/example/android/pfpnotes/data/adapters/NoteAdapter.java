package com.example.android.pfpnotes.data.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.example.android.pfpnotes.R;
import com.example.android.pfpnotes.common.Performance;
import com.example.android.pfpnotes.data.DateHelper;
import com.example.android.pfpnotes.data.DbContract;
import com.example.android.pfpnotes.models.HeaderItem;
import com.example.android.pfpnotes.models.Item;
import com.example.android.pfpnotes.models.NoteItem;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ahmed on 13/03/2018.
 */

public class NoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Cursor mCursor;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Item> mData;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
//        void onItemClick(NoteItem item);
        void onActionClick(NoteItem item, int actionId);
    }

    public NoteAdapter(OnItemClickListener listener,
                       Cursor cursor, Context context, LayoutInflater layoutInflater) {
        mListener = listener;
        mCursor = cursor;
        mContext = context;
        mLayoutInflater = layoutInflater;
        mData = getData(cursor);
    }

    private ArrayList<Item> getData(Cursor cursor) {
        if (mCursor != null) {
            mData = new ArrayList<>();
            int headerPosition = -1;
            double totalPerDay = 0d;
            String date = "";
            String currentDate;
            while (mCursor.moveToNext()) {
                currentDate = mCursor.getString(
                        mCursor.getColumnIndex(DbContract.NoteEntry.COLUMN_DATE));
                currentDate = DateHelper.getDate(currentDate); // dd/MM/yyyy
                if (!currentDate.equals(date)) {
                    if (headerPosition >= 0) {
                        ((HeaderItem) mData.get(headerPosition)).setTotalPrice(totalPerDay);
                    }

                    HeaderItem item = new HeaderItem(currentDate);
                    mData.add(item);

                    headerPosition = mData.size() - 1;
                    totalPerDay = 0d;
                    date = currentDate;
                }
                NoteItem item = new NoteItem(mCursor);
                mData.add(item);
                String totalPerDayAsString = mCursor.getString(
                        mCursor.getColumnIndex(DbContract.NoteEntry.COLUMN_PRICE));
                totalPerDay += Double.valueOf(totalPerDayAsString);
            }

            if (headerPosition >= 0) {
                ((HeaderItem) mData.get(headerPosition)).setTotalPrice(totalPerDay);
            }
            return mData;
        }

        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == Item.TYPE_HEADER) {
            View view = mLayoutInflater.inflate(R.layout.note_list_header, parent, false);
            viewHolder = new NoteListHeaderViewHolder(view);
        }
        if (viewType == Item.TYPE_NOTE) {
            View view = mLayoutInflater.inflate((R.layout.note_list_item), parent, false);
            viewHolder = new NoteViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case Item.TYPE_HEADER:
                HeaderItem headerItem = (HeaderItem) mData.get(position);
                NoteListHeaderViewHolder headerViewHolder = (NoteListHeaderViewHolder) holder;
                headerViewHolder.mDate.setText(headerItem.getDate());
                headerViewHolder.mTotal.setText(String.format("£%.2f", headerItem.getTotalPrice()));
                break;
            case Item.TYPE_NOTE:
                final NoteItem noteItem = (NoteItem) mData.get(position);
                NoteViewHolder itemViewHolder = (NoteViewHolder) holder;
//                itemViewHolder.bind(noteItem, mListener);
                itemViewHolder.mDimension.setText(noteItem.getDimension());
                itemViewHolder.mPlace.setText(noteItem.getPlace());
                itemViewHolder.mPrice.setText(String.format("£%.2f", noteItem.getPrice()));
                if (noteItem.getPath() != null) {
                    itemViewHolder.mThumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Bitmap bmp = Performance
                            .decodeSampledBitmapFromFile(
                                    noteItem.getPath(),
                                    mContext.getResources().getInteger(R.integer.thumbnailWidth),
                                    mContext.getResources().getInteger(R.integer.thumbnailHeight));
                    itemViewHolder.mThumbnail.setImageBitmap(bmp);
                }

                itemViewHolder.mEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int actionId = v.getId();
                        if(mListener != null){
                            mListener.onActionClick(noteItem, actionId);
                        }
                    }
                });
                itemViewHolder.mDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int actionId = v.getId();
                        if(mListener != null){
                            mListener.onActionClick(noteItem, actionId);
                        }
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getType();
    }
}
