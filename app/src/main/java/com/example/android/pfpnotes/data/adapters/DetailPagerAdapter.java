package com.example.android.pfpnotes.data.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.pfpnotes.DetailFragment;
import com.example.android.pfpnotes.models.Detail;
import com.example.android.pfpnotes.models.Image;
import com.example.android.pfpnotes.models.Note;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class DetailPagerAdapter extends FragmentPagerAdapter {

    private Map<Note, List<Image>> mData;

    public DetailPagerAdapter(FragmentManager fm, Map<Note, List<Image>> data) {
        super(fm);
        mData = data;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        for (Map.Entry<Note,List<Image>> noteEntry : mData.entrySet()) {
            if (noteEntry.getKey().getPosition() == position) {
                Detail detail = new Detail();
                detail.setData(noteEntry);
                return DetailFragment.newInstance(detail);
            }
        }
        return DetailFragment.newInstance(null);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return mData != null ? mData.size() : 0;
    }

    public void setData(Map<Note, List<Image>> data) {
        mData = data;
        notifyDataSetChanged();
    }
}
