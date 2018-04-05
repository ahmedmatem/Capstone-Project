package com.example.android.pfpnotes;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.example.android.pfpnotes.asynctasks.DetailAsyncTask;
import com.example.android.pfpnotes.data.adapters.DetailPagerAdapter;
import com.example.android.pfpnotes.models.Image;
import com.example.android.pfpnotes.models.Note;

import java.util.List;
import java.util.Map;

import static com.example.android.pfpnotes.NoteListFragment.NOTE_ID;

public class DetailActivity extends AppCompatActivity
        implements DetailFragment.OnDetailFragmentListener,
        DetailAsyncTask.DetailListener {
    public static final String PAGER_CURRENT_INDEX = "pager_position";

    private static DetailPagerAdapter mDetailPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private int mCurrentIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deatil);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(PAGER_CURRENT_INDEX);
        }

        int noteId = 0;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(NOTE_ID)) {
            noteId = bundle.getInt(NOTE_ID);
        }

        mDetailPagerAdapter = new DetailPagerAdapter(getSupportFragmentManager(), null);

        mViewPager = findViewById(R.id.detail_container);
        mViewPager.setAdapter(mDetailPagerAdapter);

        // load detail data asynchronously
        new DetailAsyncTask(this, noteId).execute(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(PAGER_CURRENT_INDEX, mViewPager.getCurrentItem());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDetailLoadFinished(Map<Note, List<Image>> data, int currentItem) {
        mDetailPagerAdapter.setData(data);
        if (mCurrentIndex != -1) {
            mViewPager.setCurrentItem(mCurrentIndex);
        } else {
            mViewPager.setCurrentItem(currentItem);
        }
    }
}
