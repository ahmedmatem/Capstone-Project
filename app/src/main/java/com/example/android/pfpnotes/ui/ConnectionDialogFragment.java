package com.example.android.pfpnotes.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.example.android.pfpnotes.R;

/**
 * Created by ahmed on 26/03/2018.
 */

public class ConnectionDialogFragment extends DialogFragment {
    public interface ConnectionDialogListener {
        void onDialogPositiveClick(DialogInterface dialog, int which);
        void onDialogNegativeClick(DialogInterface dialog, int which);
    }

    private ConnectionDialogListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.connection_dialog_message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mListener != null) {
                            mListener.onDialogPositiveClick(dialog, which);
                        }
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (ConnectionDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() +
                    " must implement ConnectionDialogListener");
        }
    }
}
