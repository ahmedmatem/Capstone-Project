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

import com.example.android.pfpnotes.data.DbContract;
import com.example.android.pfpnotes.data.adapters.NoteAdapter;
import com.example.android.pfpnotes.data.daos.ImageDAO;
import com.example.android.pfpnotes.data.daos.PlaceDAO;
import com.example.android.pfpnotes.models.NoteItem;
import com.example.android.pfpnotes.models.NoteModel;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NoteListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NoteListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteListFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>, NoteAdapter.OnItemClickListener {
    private static final String TAG = "NoteListFragment";
    private static final int NOTE_LOADER_ID = 5;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_NOTE = "note";

    private NoteModel mNote;

    private OnFragmentInteractionListener mListener;

    private static LoaderManager mLoaderManager;

    private RecyclerView mRecyclerView;
    private NoteAdapter mAdapter;

    public NoteListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NoteListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoteListFragment newInstance(LoaderManager loaderManager) {
        mLoaderManager = loaderManager;
        NoteListFragment fragment = new NoteListFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
        }

        mLoaderManager.initLoader(NOTE_LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_notes);
        mRecyclerView.setAdapter(mAdapter);

        return view;
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
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mRecyclerView.setAdapter(null);
    }

    @Override
    public void onItemClick(NoteItem item) {
        String fullPlaceName = item.getPlace();
        String shortPlaceName = new PlaceDAO(getContext().getContentResolver())
                .getShortPlaceNameBy(fullPlaceName);
        ArrayList<String> paths = new ImageDAO(getContext()
                .getContentResolver())
                .getPaths(item.getId());

        mNote = new NoteModel(item.getId(),
                shortPlaceName,
                fullPlaceName,
                item.getDimension(),
                paths);

        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_NOTE, mNote);

        Intent intent = new Intent(getContext(), NoteEditActivity.class);
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}