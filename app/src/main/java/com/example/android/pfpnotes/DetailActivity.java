package com.example.android.pfpnotes;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.pfpnotes.asynctasks.DetailAsyncTask;
import com.example.android.pfpnotes.data.adapters.DetailPagerAdapter;
import com.example.android.pfpnotes.models.Image;
import com.example.android.pfpnotes.models.Note;

import java.util.List;
import java.util.Map;

import static com.example.android.pfpnotes.NoteListFragment.NOTE_ID;

public class DetailActivity extends AppCompatActivity
        implements DetailFragment.OnFragmentInteractionListener,
        DetailAsyncTask.DetailListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private static DetailPagerAdapter mDetailPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deatil);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey(NOTE_ID)){
            int noteId = bundle.getInt(NOTE_ID);
        }

        mDetailPagerAdapter = new DetailPagerAdapter(getSupportFragmentManager(), null);

        mViewPager = findViewById(R.id.detail_container);
        mViewPager.setAdapter(mDetailPagerAdapter);

        // load detail data asynchronously
        new DetailAsyncTask(this).execute(this);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDetailLoadFinished(Map<Note, List<Image>> data) {
        mDetailPagerAdapter.setData(data);
    }
}
