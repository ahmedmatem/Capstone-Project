package com.example.android.pfpnotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.android.inputlibrary.KeyboardAdapter;
import com.example.android.inputlibrary.KeyboardFragment;

public class DimensionActivity extends AppCompatActivity
        implements KeyboardFragment.OnKeyboardInteractionListener {
    private static final String TAG = "DimensionActivity";
    public static final String DIMENSION_STRING = "dimension_string";

    private StringBuilder mStringBuilder;
    private TextView mDimension;

    private KeyboardFragment mKeyboardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dimension);

        mKeyboardFragment =
                KeyboardFragment.newInstance(null, null);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.keyboard_container, mKeyboardFragment)
                .commit();

        mStringBuilder = new StringBuilder();
        mDimension = findViewById(R.id.tv_dimension);
    }

    @Override
    public void onKeyboardClick(String key) {
        if(key.equals(KeyboardAdapter.KEY_OK)){
            Intent intent = getIntent().putExtra(DIMENSION_STRING, mDimension.getText());
            setResult(RESULT_OK, intent);
            finish();
            return;
        }
        mStringBuilder.append(key);
        updateDimension();
    }

    public void backspaceClick(View view) {
        if (mStringBuilder.length() > 0) {
            int lastCharIndex = mStringBuilder.length() - 1;
            mStringBuilder.deleteCharAt(lastCharIndex);
            updateDimension();
        }
    }

    private void updateDimension() {
        mDimension.setText(mStringBuilder.toString());
        updateKeyboard();
    }

    private void updateKeyboard() {
        String dimensionValue = mStringBuilder.toString();
        KeyboardAdapter adapter = mKeyboardFragment.getAdapter();

        if (adapter == null)
            return;

        // dimension is {empty}
        if (dimensionValue.length() == 0) {
            adapter.setDisabledKeys(new String[]{
                    KeyboardAdapter.KEY_0,
                    KeyboardAdapter.KEY_X,
                    KeyboardAdapter.KEY_OK
            });
            return;
        }

        // exm. WWW{x}, or WWWxHH{x}, or WWWxHHHxCCC{x}
        // Key X is the last character
        if (KeyboardAdapter.KEY_X
                .equals(String.valueOf((dimensionValue.charAt(dimensionValue.length() - 1))))) {
            adapter.setDisabledKeys(new String[]{
                    KeyboardAdapter.KEY_0,
                    KeyboardAdapter.KEY_X,
                    KeyboardAdapter.KEY_OK
            });
            return;
        }

        switch (getKey_XCount(dimensionValue)) {
            case 0:
                // only OK key must be disabled
                adapter.setDisabledKeys(new String[]{KeyboardAdapter.KEY_OK});
                return;
            case 1:
                // exm. 20x30
                // all keys must be enabled
                break;
            case 2:
                // exm. 20x30x{2} or 20x30x?
                // all keys must be enabled
                String[] dimenParts = dimensionValue.split(KeyboardAdapter.KEY_X);
                if(!KeyboardAdapter.KEY_2.equals(dimenParts[2])){
                    // X key must be disabled
                    adapter.setDisabledKeys(new String[]{KeyboardAdapter.KEY_X});
                    return;
                }
                break;
            case 3:
                // exm. 20x30x{2}xCCC
                // X key must be disabled
                adapter.setDisabledKeys(new String[]{KeyboardAdapter.KEY_X});
                return;
        }

        // no disabled keys
        adapter.setDisabledKeys(null);
    }

    private int getKey_XCount(String dimensionValue) {
        int count = 0;
        if (!dimensionValue.contains(KeyboardAdapter.KEY_X)) {
            return count;
        }
        int index;
        while ((index = dimensionValue.indexOf(KeyboardAdapter.KEY_X)) != -1) {
            count++;
            if (index++ < dimensionValue.length()) {
                dimensionValue = dimensionValue.substring(index, dimensionValue.length());
            } else {
                break;
            }
        }
        return count;
    }
}
