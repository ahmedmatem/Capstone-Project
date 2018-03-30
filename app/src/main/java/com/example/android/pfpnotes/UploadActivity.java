package com.example.android.pfpnotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.pfpnotes.asynctasks.UploadAsyncTask;
import com.example.android.pfpnotes.asynctasks.UploadInfoAsyncTask;
import com.example.android.pfpnotes.data.Preferences;
import com.example.android.pfpnotes.models.Image;
import com.example.android.pfpnotes.models.Note;
import com.example.android.pfpnotes.net.Connection;
import com.example.android.pfpnotes.ui.ConnectionDialogFragment;

import java.util.List;
import java.util.Map;

import static com.example.android.pfpnotes.SignInActivity.SOURCE_ACTIVITY_NAME;

public class UploadActivity extends AppCompatActivity
        implements UploadInfoAsyncTask.UploadDataListener,
        ConnectionDialogFragment.ConnectionDialogListener,
        UploadAsyncTask.OnUploadListener {
    private static final String TAG = "UploadActivity";

    public static final int SIGN_IN_REQUEST_CODE = 20;

    private ConstraintLayout mLayoutUpload;

    private TextView mNoUploadTextView;
    private TextView mNotesToUploadInfo;
    private TextView mImagesToUploadInfo;

    private ProgressBar mNotesProgressBar;
    private ProgressBar mImagesProgressBar;

    private Map<Note, List<Image>> mData;
    private int mImagesCount;
    private int mNotesCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        mLayoutUpload = findViewById(R.id.layout_upload);
        mNoUploadTextView = findViewById(R.id.tv_no_upload);
        mNotesToUploadInfo = findViewById(R.id.tv_notes_to_upload);
        mImagesToUploadInfo = findViewById(R.id.tv_images_to_upload);
        mNotesProgressBar = findViewById(R.id.pr_bar_notes);
        mImagesProgressBar = findViewById(R.id.pr_bar_images);

        Button uploadButton = findViewById(R.id.btn_detail);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                Connection connection = new Connection(UploadActivity.this);
                if (connection.isConnected()) {
                    boolean isSignedIn = new Preferences(UploadActivity.this).isSignedIn();
                    if (isSignedIn) {
                        new UploadAsyncTask(UploadActivity.this, mData)
                                .execute(UploadActivity.this);
                    } else {
                        Intent intent = new Intent(UploadActivity.this,
                                SignInActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(SOURCE_ACTIVITY_NAME, "UploadActivity");
                        intent.putExtras(bundle);
                        startActivityForResult(intent, SIGN_IN_REQUEST_CODE);
                    }
                } else {
                    ConnectionDialogFragment connectionDialogFragment =
                            new ConnectionDialogFragment();
                    connectionDialogFragment.show(getSupportFragmentManager(), "ConnectionDialog");
                }
            }
        });

        new UploadInfoAsyncTask(this).execute(this);
    }

    @Override
    public void onUploadDataReceived(Map<Note, List<Image>> data) {
        if (data == null) {
            mLayoutUpload.setVisibility(View.GONE);
            mNoUploadTextView.setVisibility(View.VISIBLE);
            return;
        } else {
            mLayoutUpload.setVisibility(View.VISIBLE);
            mNoUploadTextView.setVisibility(View.GONE);
        }

        mData = data;
        mImagesCount = getImagesCount();
        mNotesCount = data.size();
        mNotesToUploadInfo.setText(String.format(
                getString(R.string.notes_size_text), data.size()));
        int imagesCount = getImagesCount();
        mImagesToUploadInfo.setText(String.format(
                getString(R.string.images_size_text), imagesCount));
    }

    private int getImagesCount() {
        int count = 0;
        for (Map.Entry<Note, List<Image>> entry : mData.entrySet()) {
            if (entry.getValue() != null) {
                count += entry.getValue().size();
            }
        }
        return count;
    }

    @Override
    public void onDialogPositiveClick(DialogInterface dialog, int which) {

    }

    @Override
    public void onDialogNegativeClick(DialogInterface dialog, int which) {

    }

    @Override
    public void onNotesProgressChanged(int currentNoteNumber) {
        mNotesProgressBar.setProgress((100 * currentNoteNumber) / mNotesCount);
    }

    @Override
    public void onImagesProgressChanged(int currentImageNumber) {
        mImagesProgressBar.setProgress((100 * currentImageNumber) / mImagesCount);
    }
}
