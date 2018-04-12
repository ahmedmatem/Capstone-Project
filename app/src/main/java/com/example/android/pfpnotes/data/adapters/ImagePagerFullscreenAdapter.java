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
    private OnFullscreenListener mListener;

    public interface OnFullscreenListener {
        void onClick();
    }

    public ImagePagerFullscreenAdapter(Context context, ArrayList<String> paths) {
        mContext = context;
        try {
            mListener = (OnFullscreenListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.getClass().getSimpleName() + " must implement " +
                    "interface OnFullscreenListener");
        }
        mPaths = paths;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        String path = mPaths.get(position);
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View layout = layoutInflater.inflate(R.layout.fullscreen_item,
                container, false);
        PhotoView photoView = (PhotoView) layout.findViewById(R.id.fullscreen_image);
        photoView.setImageURI(Uri.parse(path));
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick();
                }
            }
        });
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
