package com.manager;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.materialdesign.R;
import com.utils.Utils;

import java.util.List;

/**
 * Created by cwj on 16/12/20.
 */
public class DashboardManager extends BaseManager {

    public DashboardManager(LayoutInflater inflater, ViewGroup container) {
        super(inflater, container);
    }

    @Override
    protected View generateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.dashboard, container, false);
    }

    @Override
    public void updView(Object data) {
        List<String> list = (List<String>) data;
        LinearLayout linearLayout = (LinearLayout) getView();
        linearLayout.removeAllViews();
        if (list == null || list.size() <= 0) {
            return;
        }
        for (String str : list) {
            linearLayout.addView(getTextView(linearLayout.getContext(), str));
        }
    }

    private View getTextView(Context context, String str) {
        TextView textView = new TextView(context);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dp2px(context, 50)));
        textView.setBackgroundColor(Color.BLACK);
        textView.setTextColor(Color.WHITE);
        textView.setText(str);
        return textView;
    }

}
