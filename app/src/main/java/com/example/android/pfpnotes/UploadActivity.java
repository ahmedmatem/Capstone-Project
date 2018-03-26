package com.example.android.pfpnotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
        ConnectionDialogFragment.ConnectionDialogListener{

    public static final int SIGN_IN_REQUEST_CODE = 20;

    private TextView mNotesToUploadInfo;
    private TextView mImagesToUploadInfo;

    private Map<Note,List<Image>> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        mNotesToUploadInfo = (TextView) findViewById(R.id.tv_notes_to_upload);
        mImagesToUploadInfo = (TextView) findViewById(R.id.tv_images_to_upload);

        Button uploadButton = (Button) findViewById(R.id.btn_upload);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection connection = new Connection(UploadActivity.this);
                if(connection.isConnected()){
                    boolean isSignedIn = new Preferences(UploadActivity.this).isSignedIn();
                    if (isSignedIn) {
                        new UploadAsyncTask(mData).execute(UploadActivity.this);
                    } else {
                        Intent intent = new Intent(UploadActivity.this,
                                SignInActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(SOURCE_ACTIVITY_NAME, "UploadActivity");
                        intent.putExtras(bundle);
                        startActivityForResult(intent, SIGN_IN_REQUEST_CODE);
                    }
                } else {
                    ConnectionDialogFragment  connectionDialogFragment =
                            new ConnectionDialogFragment();
                    connectionDialogFragment.show(getSupportFragmentManager(), "ConnectionDialog");
                }
            }
        });

        new UploadInfoAsyncTask(this).execute(this);
    }

    @Override
    public void onUploadDataReceived(Map<Note,List<Image>> data) {
        mData = data;
        mNotesToUploadInfo.setText("Notes: " + data.size());
        int imagesCount = getImagesCount();
        mImagesToUploadInfo.setText("Images: " + imagesCount );
    }

    private int getImagesCount() {
        int count = 0;
        for (Map.Entry<Note, List<Image>> entry : mData.entrySet()){
            count += entry.getValue().size();
        }
        return  count;
    }

    @Override
    public void onDialogPositiveClick(DialogInterface dialog, int which) {

    }

    @Override
    public void onDialogNegativeClick(DialogInterface dialog, int which) {

    }
}
