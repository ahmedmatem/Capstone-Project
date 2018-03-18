package com.example.android.pfpnotes;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.pfpnotes.common.CameraHelper;
import com.example.android.pfpnotes.data.DateHelper;
import com.example.android.pfpnotes.data.DbContract;
import com.example.android.pfpnotes.data.daos.PlaceDAO;
import com.example.android.pfpnotes.data.Preferences;
import com.example.android.pfpnotes.data.daos.PriceDAO;
import com.example.android.pfpnotes.models.Dimension;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.android.pfpnotes.NoteAddEditFragment.ARG_DIMENSION;
import static com.example.android.pfpnotes.NoteAddEditFragment.ARG_PATHS;
import static com.example.android.pfpnotes.NoteAddEditFragment.ARG_PLACE_SHORT_NAME;

public class NoteAddActivity extends AppCompatActivity
        implements NoteAddEditFragment.OnFragmentInteractionListener {
    private static final String TAG = "NoteAddActivity";

    public static final int DIMENSION_REQUEST = 1;
    public static final int REQUEST_IMAGE_CAPTURE = 2;

    private Fragment mFragment;

    private MenuItem mAddMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add);

        if (savedInstanceState == null) {
            mFragment = NoteAddEditFragment.newInstance(getSupportLoaderManager(),
                    null, null, null);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, mFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_note, menu);
        mAddMenuItem = menu.findItem(R.id.action_add_note);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_note) {
            addNote();
            Intent intent = new Intent(this, NoteListActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addNote() {
        Uri uri = insertNoteEntry();
        if(uri != null) {
            insertNoteImages(uri);
        }

    }

    private Uri insertNoteEntry() {
        Uri uri = null;

        Bundle args = mFragment.getArguments();
        String shortPlaceName = args.getString(ARG_PLACE_SHORT_NAME);
        PlaceDAO placeDAO = new PlaceDAO(getContentResolver());
        String fullPlaceName = placeDAO.getFullPlaceNameBy(shortPlaceName);
        Dimension dimension = new Dimension(args.getString(ARG_DIMENSION));

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
        cv.put(DbContract.NoteEntry.COLUMN_STATUS, DbContract.NoteEntry.NoteStatus.STATUS_UPLOAD);

        return getContentResolver().insert(DbContract.NoteEntry.CONTENT_URI, cv);
    }

    private void insertNoteImages(Uri uri) {
        if (uri != null) {
            Bundle args = mFragment.getArguments();
            ArrayList<String> paths = args.getStringArrayList(ARG_PATHS);
            if (paths != null && paths.size() > 0) {
                boolean isThumbnail = true;
                int noteId = Integer.valueOf(uri.getLastPathSegment());
                ContentResolver contentResolver = getContentResolver();
                for (String path : paths) {
                    ContentValues cv = new ContentValues();
                    cv.put(DbContract.ImageEntry.COLUMN_NOTE_ID, noteId);
                    cv.put(DbContract.ImageEntry.COLUMN_IMAGE_PATH, path);
                    cv.put(DbContract.ImageEntry.COLUMN_EMAIL,
                            new Preferences(this).readUserEmail());
                    cv.put(DbContract.ImageEntry.COLUMN_THUMBNAIL, isThumbnail ? 1 : 0);
                    contentResolver.insert(DbContract.ImageEntry.CONTENT_URI, cv);
                    if(isThumbnail) {
                        isThumbnail = !isThumbnail;
                    }
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
        boolean enable = false;
        Bundle args = mFragment.getArguments();
        String dimension = args.getString(ARG_DIMENSION);
        String placeShortName = args.getString(ARG_PLACE_SHORT_NAME);
        if (dimension != null && placeShortName != null) {
            enable = true;
        }
        mAddMenuItem.setEnabled(enable);
    }
}
