package com.example.android.pfpnotes;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.pfpnotes.common.CameraHelper;
import com.example.android.pfpnotes.data.DbContract;
import com.example.android.pfpnotes.data.adapters.ImageAdapter;
import com.example.android.pfpnotes.models.NoteModel;
import com.example.android.pfpnotes.ui.DeleteDialogFragment;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.example.android.pfpnotes.DimensionActivity.DIMENSION_STRING;
import static com.example.android.pfpnotes.NoteAddActivity.DIMENSION_REQUEST;
import static com.example.android.pfpnotes.NoteAddActivity.REQUEST_IMAGE_CAPTURE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NoteAddEditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NoteAddEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteAddEditFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>,
        ImageAdapter.OnThumbnailClickListener{
    private static final String TAG = "NoteAddEditFragment";

    public static final int PLACE_LOADER_ID = 3;

    public static final String ARG_NOTE = "note";

    private NoteModel mNote;

    private static LoaderManager mLoaderManager;

    private OnFragmentInteractionListener mListener;

    private SimpleCursorAdapter mPlaceAdapter;
    private ImageAdapter mImageAdapter;

    private TextView tvDimension;
    private TextView mSelectedPlace;

    public NoteAddEditFragment() {
        // Required empty public constructor
    }

    public static NoteAddEditFragment newInstance(LoaderManager loaderManager, NoteModel note) {
        mLoaderManager = loaderManager;
        NoteAddEditFragment fragment = new NoteAddEditFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle args = getArguments();
            mNote = (NoteModel) args.getParcelable(ARG_NOTE);
        }
        mImageAdapter = new ImageAdapter(mNote.getPaths(),
                getContext(),
                getLayoutInflater(),
                this);
        mLoaderManager.initLoader(PLACE_LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note_add_edit, container, false);

        String[] fromColumns = {DbContract.PlaceEntry.COLUMN_SHORT_NAME};
        int[] toViews = {R.id.place_short_name};
        mPlaceAdapter = new SimpleCursorAdapter(getContext(),
                R.layout.place_grid_item, null, fromColumns, toViews, 0);
        mPlaceAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (columnIndex == cursor.getColumnIndex(DbContract.PlaceEntry.COLUMN_SHORT_NAME)) {
                    TextView place = (TextView) view;
                    place.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                    place.setLayoutParams(new ViewGroup
                            .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 140));
                    place.setBackground(getResources()
                            .getDrawable(R.drawable.bg_border));
                    if (mNote.getShortPlaceName() != null &&
                            mNote.getShortPlaceName().equals(cursor.getString(columnIndex))) {
                        mSelectedPlace = place;
                        place.setBackgroundColor(getResources()
                                .getColor(R.color.colorAccent));
                    }
                    place.setText(cursor.getString(columnIndex));
                    return true;
                }
                return false;
            }
        });

        GridView gv_places = (GridView) view.findViewById(R.id.gv_places);
        gv_places.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null) {
                    if (mSelectedPlace != null) {
                        mSelectedPlace.setBackground(getResources()
                                .getDrawable(R.drawable.bg_border));
                    }
                    mSelectedPlace = (TextView) view;
                    mSelectedPlace.setBackgroundColor(getResources()
                            .getColor(R.color.colorAccent));
                    mNote.setShortPlaceName(mSelectedPlace.getText().toString());

                    mListener.onUserInputChanged();
                }
            }
        });
        gv_places.setAdapter(mPlaceAdapter);

        tvDimension = (TextView) view.findViewById(R.id.tv_dimens_value);
        if (mNote.getDimensionText() != null) {
            tvDimension.setText(mNote.getDimensionText());
        }

        GridView gv_thumbs = (GridView) view.findViewById(R.id.gv_thumbnails);
        gv_thumbs.setAdapter(mImageAdapter);

        return view;
    }

    public void onDimensionClick(View view) {
        if (mListener != null) {
            mListener.onDimensionClick(view);
        }
    }

    public void onCameraClick(View view) {
        if (mListener != null) {
            mListener.onCameraClick(view);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DIMENSION_REQUEST && resultCode == RESULT_OK) {
            mNote.setDimensionText(data.getStringExtra(DIMENSION_STRING));
            tvDimension.setText(mNote.getDimensionText());

            if (mListener != null) {
                mListener.onUserInputChanged();
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (mNote.getPaths() == null) {
                mNote.setPaths(new ArrayList<String>());
            }
            String path = CameraHelper.getPhotoPath();
            mNote.getPaths().add(path);
            mImageAdapter.setPaths(mNote.getPaths());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnKeyboardInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(),
                DbContract.PlaceEntry.CONTENT_URI,
                null,
                null,
                null,
                DbContract.PlaceEntry.COLUMN_SHORT_NAME);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mPlaceAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPlaceAdapter.swapCursor(null);
    }

    @Override
    public void onDeleteClick(String path) {
        mNote.setImageToDelete(path);
        DeleteDialogFragment dialogFragment = new DeleteDialogFragment();
        dialogFragment.show(getChildFragmentManager(), "DeleteDialogFragment");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onDimensionClick(View view);
        void onCameraClick(View view);
        void onUserInputChanged();
    }

    public NoteModel getNote() {
        return mNote;
    }

    public ImageAdapter getImageAdapter() {
        return mImageAdapter;
    }
}
