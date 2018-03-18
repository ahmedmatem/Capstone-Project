package com.example.android.inputlibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by ahmed on 04/03/2018.
 */

public class KeyboardAdapter extends BaseAdapter {
    public static final String KEY_0 = "0";
    public static final String KEY_1 = "1";
    public static final String KEY_2 = "2";
    public static final String KEY_3 = "3";
    public static final String KEY_4 = "4";
    public static final String KEY_5 = "5";
    public static final String KEY_6 = "6";
    public static final String KEY_7 = "7";
    public static final String KEY_8 = "8";
    public static final String KEY_9 = "9";
    public static final String KEY_X = "x";
    public static final String KEY_OK = "OK";

    private static final String[] keys = new String[]{
            KEY_7, KEY_8, KEY_9,
            KEY_4, KEY_5, KEY_6,
            KEY_1, KEY_2, KEY_3,
            KEY_0, KEY_X, KEY_OK
    };

    private static String[] mDisabledKeys;

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public KeyboardAdapter(Context context, LayoutInflater inflater) {
        mContext = context;
        mLayoutInflater = inflater;
    }

    @Override
    public int getCount() {
        return keys.length;
    }

    @Override
    public Object getItem(int position) {
        return keys[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.key_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mKeyTextView.setText(keys[position]);
        if (position == keys.length - 1) {
            viewHolder.mKeyTextView.setTextColor(
                    mContext.getResources().getColor(android.R.color.white));

            viewHolder.mKeyTextView.setBackgroundColor(
                    mContext.getResources().getColor(R.color.colorAccent));
        }

        viewHolder.mKeyTextView.setEnabled(isEnabled(position));

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        boolean enable  = true;
        if (mDisabledKeys == null) {
            return enable;
        }

        String key = keys[position];
        if(keyDisabled(key)){
            enable = false;
        }

        return enable;
    }

    private class ViewHolder {
        public TextView mKeyTextView;

        public ViewHolder(View view) {
            mKeyTextView = (TextView) view.findViewById(R.id.tv_key);
        }
    }

    private boolean keyDisabled(String key) {
        if(mDisabledKeys == null){
            return false;
        }

        for (String k : mDisabledKeys) {
            if(key.equals(k)){
                return  true;
            }
        }
        return false;
    }

    public String[] getDisabledKeys() {
        return mDisabledKeys;
    }

    public void setDisabledKeys(String[] disabledKeys) {
        mDisabledKeys = disabledKeys;
        notifyDataSetChanged();
    }
}
