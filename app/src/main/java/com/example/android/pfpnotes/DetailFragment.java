package com.example.android.pfpnotes;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.pfpnotes.data.DbContract;
import com.example.android.pfpnotes.models.Detail;
import com.example.android.pfpnotes.models.Image;
import com.example.android.pfpnotes.models.Note;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {
    public static final String ARG_NOTE_DETAIL = "note_detail";

    private OnFragmentInteractionListener mListener;

    private Detail mDetail;

    public DetailFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static DetailFragment newInstance(Detail detail) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE_DETAIL, detail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        TextView date = rootView.findViewById(R.id.tv_detail_date);
        TextView place = rootView.findViewById(R.id.tv_detail_place);
        TextView dimension = rootView.findViewById(R.id.tv_detail_dimension);
        TextView price = rootView.findViewById(R.id.tv_detail_price);
        TextView uploadStatus = rootView.findViewById(R.id.tv_detail_upload_status);

        Note note = mDetail.getData().getKey();
        List<Image> imageList = mDetail.getData().getValue();

        date.setText(note.getSampleDate());
        place.setText(note.getPlace());
        dimension.setText(note.getDimension());
        price.setText(String.format(getString(R.string.price_text), note.getPrice()));
        uploadStatus.setText(note.getStatus() == DbContract.NoteEntry.Status.DONE ?
                "Uploaded" : "Not uploaded");

        return rootView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mDetail = args.getParcelable(ARG_NOTE_DETAIL);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        void onFragmentInteraction(Uri uri);
    }
}
