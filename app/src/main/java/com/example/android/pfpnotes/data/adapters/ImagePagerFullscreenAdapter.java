package com.example.android.pfpnotes.data.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.pfpnotes.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

public class ImagePagerFullscreenAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<String> mPaths;

    public ImagePagerFullscreenAdapter(Context context, ArrayList<String> paths) {
        mContext = context;
        mPaths = paths;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        String path = mPaths.get(position);
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View layout = layoutInflater.inflate(R.layout.fullscreen_item,
                container, false);
//        ImageView imageView = layout.findViewById(R.id.fullscreen_image);
//        imageView.setImageURI(Uri.parse(path));
        PhotoView photoView = (PhotoView) layout.findViewById(R.id.fullscreen_image);
        photoView.setImageURI(Uri.parse(path));
        container.addView(layout);
        return layout;
    }

    @Override
    public int getCount() {
        return mPaths != null ? mPaths.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
