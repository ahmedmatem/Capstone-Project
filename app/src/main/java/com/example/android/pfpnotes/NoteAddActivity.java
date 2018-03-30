package com.example.android.pfpnotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.pfpnotes.asynctasks.DeleteImageAsyncTask;
import com.example.android.pfpnotes.asynctasks.NoteAddAsyncTask;
import com.example.android.pfpnotes.common.ImageHelper;
import com.example.android.pfpnotes.interfaces.OnDatabaseListener;
import com.example.android.pfpnotes.models.NoteModel;
import com.example.android.pfpnotes.ui.DeleteDialogFragment;

import java.io.File;
import java.io.IOException;

public class NoteAddActivity extends AppCompatActivity
        implements NoteAddEditFragment.OnFragmentInteractionListener,
        OnDatabaseListener, DeleteDialogFragment.DeleteDialogListener{
    private static final String TAG = "NoteAddActivity";

    public static final int DIMENSION_REQUEST = 1;
    public static final int REQUEST_IMAGE_CAPTURE = 2;

    private NoteAddEditFragment mFragment;
    private NoteModel mNote = new NoteModel();

    private MenuItem mAddMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add);

        if (savedInstanceState == null) {
            mFragment = NoteAddEditFragment.newInstance(getSupportLoaderManager(), mNote);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, mFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_add, menu);
        mAddMenuItem = menu.findItem(R.id.action_add_note);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_note) {
            new NoteAddAsyncTask(mNote, this).execute(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mFragment != null) {
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
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = new ImageHelper(this).createImageFile();
            } catch (IOException e) {

            }

            // continue if file was successfully created
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onUserInputChanged() {
        boolean enable = false;
        if (mNote.getDimensionText() != null && mNote.getShortPlaceName() != null) {
            enable = true;
        }
        mAddMenuItem.setEnabled(enable);
    }

    @Override
    public void onDataSaved() {
        Intent intent = new Intent(this, NoteListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDataDeleted() {
        if(mNote.getPaths().remove(mNote.getImageToDelete())){
            // this will cause notify data set changed
            mFragment.getImageAdapter().setPaths(mNote.getPaths());
        }
    }

    @Override
    public void onDialogPositiveClick(DialogInterface dialog, int which) {
        new DeleteImageAsyncTask(this).execute(mNote);
        dialog.dismiss();
    }

    @Override
    public void onDialogNegativeClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }
}
