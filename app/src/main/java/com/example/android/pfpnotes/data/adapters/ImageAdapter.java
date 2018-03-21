package com.example.android.pfpnotes.data.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.example.android.pfpnotes.R;
import com.example.android.pfpnotes.common.Performance;

import java.util.ArrayList;

/**
 * Created by ahmed on 08/03/2018.
 */

public class ImageAdapter extends BaseAdapter {
    private ArrayList<String> mPaths;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private OnThumbnailClickListener mListener;

    public interface OnThumbnailClickListener{
        void onDeleteClick(String path);
    }

    public ImageAdapter(ArrayList<String> paths,
                        Context context,
                        LayoutInflater inflater,
                        OnThumbnailClickListener listener) {
        mPaths = paths;
        mContext = context;
        mLayoutInflater = inflater;
        mListener = listener;
    }

    @Override
    public int getCount() {
        return mPaths != null ? mPaths.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mPaths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.thumbnail_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final String path = mPaths.get(position);
        Bitmap bmp = Performance.decodeSampledBitmapFromFile(
                path, mContext.getResources().getInteger(R.integer.thumbnailWidth),
                mContext.getResources().getInteger(R.integer.thumbnailHeight));
        viewHolder.mThumbnail.setImageBitmap(bmp);
        viewHolder.mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onDeleteClick(path);
                }
            }
        });

        return convertView;
    }

    public void setPaths(ArrayList<String> paths) {
        mPaths = paths;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        public ImageView mThumbnail;
        private Button mDeleteBtn;

        public ViewHolder(View view) {
            mThumbnail = (ImageView) view.findViewById(R.id.iv_thumbnail);
            mDeleteBtn = (Button) view.findViewById(R.id.btn_delete_thumbnail);
        }
    }
}
