package com.example.android.pfpnotes.data.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.android.pfpnotes.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ahmed on 08/03/2018.
 */

public class ImageAdapter extends BaseAdapter {
    private ArrayList<String> mPaths;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public ImageAdapter(ArrayList<String> paths,
                        Context context,
                        LayoutInflater inflater) {
        mPaths = paths;
        mContext = context;
        mLayoutInflater = inflater;
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
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.thumbnail_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String path = mPaths.get(position);
        File imageFile = new File(path);
        Uri contentUri = FileProvider.getUriForFile(mContext,
                "com.example.android.fileprovider", imageFile);

        viewHolder.mThumbnail.setImageURI(contentUri);

        return convertView;
    }

    public void setPaths(ArrayList<String> paths) {
        mPaths = paths;
        notifyDataSetChanged();
    }

    private class ViewHolder{
        public ImageView mThumbnail;

        public ViewHolder(View view) {
            mThumbnail = (ImageView) view.findViewById(R.id.iv_thumbnail);
        }
    }
}
