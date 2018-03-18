package com.example.android.pfpnotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.android.inputlibrary.KeyboardFragment;

import java.util.ArrayList;

import static com.example.android.pfpnotes.NoteAddEditFragment.ARG_DIMENSION;
import static com.example.android.pfpnotes.NoteAddEditFragment.ARG_PATHS;
import static com.example.android.pfpnotes.NoteAddEditFragment.ARG_PLACE_SHORT_NAME;

public class NoteEditActivity extends AppCompatActivity implements
        NoteAddEditFragment.OnFragmentInteractionListener{

    public static final int DIMENSION_REQUEST = 1;

    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        Bundle bundle = getIntent().getExtras();
        String place = bundle.getString(ARG_PLACE_SHORT_NAME);
        String dimension = bundle.getString(ARG_DIMENSION);
        ArrayList<String> paths = bundle.getStringArrayList(ARG_PATHS);

        mFragment = NoteAddEditFragment.newInstance(getSupportLoaderManager(),
                dimension, place, paths);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.edit_fragment_container, mFragment)
                .commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(mFragment != null){
            mFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDimensionClick(View view) {
        Intent intent = new Intent(this, DimensionActivity.class);
        startActivityForResult(intent, DIMENSION_REQUEST);
    }

    @Override
    public void onCameraClick(View view) {

    }

    @Override
    public void onUserInputChanged() {

    }
}
