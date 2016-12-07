package com.manager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.materialdesign.R;

/**
 * Created by cwj on 16/12/20.
 */
public class BulletinManager extends BaseManager {

    public BulletinManager(LayoutInflater inflater, ViewGroup container) {
        super(inflater, container);
    }

    @Override
    protected View generateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.bulletin, container, false);
    }

    @Override
    public void updView(Object data) {
        String text = (String) data;
        ViewGroup.LayoutParams params = getView().getLayoutParams();
        if (TextUtils.isEmpty(text)) {
            params.height = 0;
        } else {
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        getView().setLayoutParams(params);

        TextView textView = (TextView) getView().findViewById(R.id.textView);
        textView.setText(text);
    }

}
