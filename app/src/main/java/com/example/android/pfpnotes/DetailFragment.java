package com.example.android.pfpnotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.pfpnotes.common.Performance;
import com.example.android.pfpnotes.data.DbContract;
import com.example.android.pfpnotes.models.Detail;
import com.example.android.pfpnotes.models.Image;
import com.example.android.pfpnotes.models.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.android.pfpnotes.ImagePagerFullscreenActivity.IMAGE_PATHS;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnDetailFragmentListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {
    public static final String ARG_NOTE_DETAIL = "note_detail";
    public static final String CURRENT_POSITION = "current_position";

    private OnDetailFragmentListener mListener;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mDetail = args.getParcelable(ARG_NOTE_DETAIL);
        }
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

        GridView detailImagesGridView = rootView.findViewById(R.id.gv_detail_images);
        DetailGridAdapter adapter;
        adapter = new DetailGridAdapter(getLayoutInflater(), mDetail.getData());
        detailImagesGridView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDetailFragmentListener) {
            mListener = (OnDetailFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement NoteListListener");
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
    public interface OnDetailFragmentListener {
        void onFragmentInteraction(Uri uri);
    }

    public class DetailGridAdapter extends BaseAdapter {
        private LayoutInflater mLayoutInflater;
        private Map.Entry<Note, List<Image>> mNoteListEntry;

        public DetailGridAdapter(LayoutInflater layoutInflater,
                                 Map.Entry<Note, List<Image>> noteListEntry) {
            mLayoutInflater = layoutInflater;
            mNoteListEntry = noteListEntry;
        }

        @Override
        public int getCount() {
            if (mNoteListEntry != null && mNoteListEntry.getValue() != null) {
                return mNoteListEntry.getValue().size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mNoteListEntry != null && mNoteListEntry.getValue() != null) {
                return mNoteListEntry.getValue().get(position).getPath();
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ImageViewHolder imageViewHolder;
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.detail_images_grid_item,
                        parent, false);
                imageViewHolder = new ImageViewHolder(convertView);
                convertView.setTag(imageViewHolder);
            } else {
                imageViewHolder = (ImageViewHolder) convertView.getTag();
            }

            if (mNoteListEntry != null && mNoteListEntry.getValue() != null) {
                List<Image> paths = mNoteListEntry.getValue();
                String path = paths.get(position).getPath();
                Bitmap bitmap;
                if (path != null) {
                    bitmap = Performance.decodeSampledBitmapFromFile(path, 200, 200);
                    imageViewHolder.mImage.setImageBitmap(bitmap);
                    imageViewHolder.mImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            Intent intent =
                                    new Intent(getContext(), ImagePagerFullscreenActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putStringArrayList(IMAGE_PATHS,
                                    imageListToArrayListOfPaths(mNoteListEntry.getValue()
                                    ));
                            bundle.putInt(CURRENT_POSITION, position);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }
            }

            return convertView;
        }

        private ArrayList<String> imageListToArrayListOfPaths(List<Image> imageList) {
            ArrayList<String> pathList = new ArrayList<>();
            for (Image img : imageList) {
                pathList.add(img.getPath());
            }
            return pathList;
        }
    }

    public class ImageViewHolder {
        ImageView mImage;

        public ImageViewHolder(View convertView) {
            mImage = convertView.findViewById(R.id.iv_detail_image);
        }
    }
}
