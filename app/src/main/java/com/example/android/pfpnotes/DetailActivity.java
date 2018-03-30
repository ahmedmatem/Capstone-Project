package com.example.android.pfpnotes;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.android.pfpnotes.data.adapters.DetailPagerAdapter;

public class DetailActivity extends AppCompatActivity
        implements DetailFragment.OnFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private DetailPagerAdapter mDetailPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deatil);

        mDetailPagerAdapter = new DetailPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.detail_container);
        mViewPager.setAdapter(mDetailPagerAdapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
