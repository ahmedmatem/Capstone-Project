package com.example.android.pfpnotes;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.inputlibrary.KeyboardFragment;
import com.example.android.pfpnotes.common.CameraHelper;
import com.example.android.pfpnotes.data.DateHelper;
import com.example.android.pfpnotes.data.DbContract;
import com.example.android.pfpnotes.data.Preferences;
import com.example.android.pfpnotes.data.daos.ImageDAO;
import com.example.android.pfpnotes.data.daos.PlaceDAO;
import com.example.android.pfpnotes.data.daos.PriceDAO;
import com.example.android.pfpnotes.models.Dimension;
import com.example.android.pfpnotes.models.NoteModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.android.pfpnotes.NoteListFragment.ARG_NOTE;


public class NoteEditActivity extends AppCompatActivity implements
        NoteAddEditFragment.OnFragmentInteractionListener {

    public static final int DIMENSION_REQUEST = 1;
    public static final int REQUEST_IMAGE_CAPTURE = 2;

    private Fragment mFragment;
    private NoteModel mNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        Bundle bundle = getIntent().getExtras();
        mNote = (NoteModel) bundle.getParcelable(ARG_NOTE);

        mFragment = NoteAddEditFragment.newInstance(getSupportLoaderManager(), mNote);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.edit_fragment_container, mFragment)
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_edit_note) {
            updateNote();
            Intent intent = new Intent(this, NoteListActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateNote() {
        int numberOfUpdateRows = updateNoteEntry();
        if (numberOfUpdateRows > 0) {
            updateNoteImages();
        }
    }

    private int updateNoteEntry() {
        String shortPlaceName = mNote.getShortPlaceName();
        PlaceDAO placeDAO = new PlaceDAO(getContentResolver());
        String fullPlaceName = placeDAO.getFullPlaceNameBy(shortPlaceName);
        Dimension dimension = new Dimension(mNote.getDimensionText());

        ContentValues cv = new ContentValues();
        cv.put(DbContract.NoteEntry.COLUMN_PLACE, fullPlaceName);
        cv.put(DbContract.NoteEntry.COLUMN_EMAIL, new Preferences(this).readUserEmail());
        cv.put(DbContract.NoteEntry.COLUMN_WIDTH, dimension.getWidth());
        cv.put(DbContract.NoteEntry.COLUMN_HEIGHT, dimension.getHeight());
        cv.put(DbContract.NoteEntry.COLUMN_LAYERS, dimension.getLayers());
        cv.put(DbContract.NoteEntry.COLUMN_COPIES, dimension.getCopies());
        cv.put(DbContract.NoteEntry.COLUMN_DATE, DateHelper.getCurrentDate());
        double price = new PriceDAO(getContentResolver())
                .getPriceBySquare(dimension.getSquare());
        cv.put(DbContract.NoteEntry.COLUMN_PRICE,
                price * dimension.getLayers() * dimension.getCopies());

        return getContentResolver().update(
                DbContract.NoteEntry.buildUriWithId(String.valueOf(mNote.getNoteId())),
                cv,
                null,
                null);
    }

    private void updateNoteImages() {
        ArrayList<String> paths = mNote.getPhotoPaths();
        if (paths != null && paths.size() > 0) {
            ContentResolver contentResolver = getContentResolver();
            // delete old paths
            new ImageDAO(contentResolver).deleteBy(mNote.getNoteId());
            // add new paths
            boolean isThumbnail = true;
            for (String path : paths) {
                ContentValues cv = new ContentValues();
                cv.put(DbContract.ImageEntry.COLUMN_NOTE_ID, mNote.getNoteId());
                cv.put(DbContract.ImageEntry.COLUMN_IMAGE_PATH, path);
                cv.put(DbContract.ImageEntry.COLUMN_EMAIL,
                        new Preferences(this).readUserEmail());
                cv.put(DbContract.ImageEntry.COLUMN_THUMBNAIL, isThumbnail ? 1 : 0);
                contentResolver.insert(DbContract.ImageEntry.CONTENT_URI, cv);
                if (isThumbnail) {
                    isThumbnail = false;
                }
            }
        }
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
                photoFile = new CameraHelper(this).createImageFile();
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

    }
}
