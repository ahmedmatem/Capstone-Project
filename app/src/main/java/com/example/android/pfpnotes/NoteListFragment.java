package com.example.android.pfpnotes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.pfpnotes.asynctasks.DetailAsyncTask;
import com.example.android.pfpnotes.data.DbContract;
import com.example.android.pfpnotes.data.adapters.NoteAdapter;
import com.example.android.pfpnotes.data.daos.ImageDAO;
import com.example.android.pfpnotes.data.daos.PlaceDAO;
import com.example.android.pfpnotes.models.Item;
import com.example.android.pfpnotes.models.NoteItem;
import com.example.android.pfpnotes.models.NoteModel;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NoteListListener} interface
 * to handle interaction events.
 * Use the {@link NoteListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteListFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>,
        NoteAdapter.OnItemClickListener {
    private static final String TAG = "NoteListFragment";
    private static final int NOTE_LOADER_ID = 5;
    public static final String NOTE_ID = "note_id";

    public static final String ARG_NOTE = "note";

    private NoteModel mNote;

    private NoteListListener mListener;

    private static LoaderManager mLoaderManager;

    private RecyclerView mRecyclerView;
    private NoteAdapter mAdapter;

    private static int mNoteId;

    public NoteListFragment() {
        // Required empty public constructor
    }

    public static NoteListFragment newInstance(LoaderManager loaderManager) {
        mLoaderManager = loaderManager;
        return new NoteListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoaderManager.initLoader(NOTE_LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);

        mRecyclerView = view.findViewById(R.id.rv_notes);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NoteListListener) {
            mListener = (NoteListListener) context;
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
                DbContract.NoteEntry.CONTENT_URI,
                null,
                null,
                null,
                DbContract.NoteEntry.COLUMN_DATE + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter = new NoteAdapter(this, data, getContext(), getLayoutInflater());
        mRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        if(getResources().getBoolean(R.bool.twoPane)){
            if(mListener != null){
                mListener.onNotesLoadFinished();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mRecyclerView.setAdapter(null);
    }

    @Override
    public void onActionClick(NoteItem item, int actionId) {
        switch (actionId){
            case R.id.btn_edit:
                startNoteEditActivity(item);
                break;
            case R.id.btn_detail:
                if(getResources().getBoolean(R.bool.twoPane)){
                    new DetailAsyncTask((DetailAsyncTask.DetailListener) getContext(), item.getId())
                            .execute(getContext());
                    break;
                }
                
                startDetailActivity(item);
                break;
        }
    }

    private void startNoteEditActivity(NoteItem item) {
        String fullPlaceName = item.getPlace();
        String shortPlaceName = new PlaceDAO(getContext().getContentResolver())
                .getShortPlaceNameBy(fullPlaceName);
        ArrayList<String> paths = new ImageDAO(getContext()
                .getContentResolver())
                .getPaths(item.getId());

        mNote = new NoteModel(
                item.getId(),
                shortPlaceName,
                fullPlaceName,
                item.getDimension(),
                paths,
                item.getNoteStatus());

        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_NOTE, mNote);

        Intent intent = new Intent(getContext(), NoteEditActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void startDetailActivity(NoteItem item) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(NOTE_ID, item.getId());
        intent.putExtras(bundle);
        startActivity(intent);
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
    public interface NoteListListener {
        // TODO: Update argument type and name
        void onNotesLoadFinished();
    }

    public NoteAdapter getAdapter() {
        return mAdapter;
    }
}
