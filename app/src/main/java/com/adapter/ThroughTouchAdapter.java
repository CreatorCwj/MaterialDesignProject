package com.adapter;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.materialdesign.R;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwj on 16/9/7.
 */
public class ThroughTouchAdapter extends BaseAdapter {

    private List<String> list;

    {
        list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            list.add("俺是Item" + i);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = new TextView(parent.getContext());
            convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
            viewHolder.textView = (TextView) convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        int padding = Utils.dp2px(parent.getContext(), 10);
        viewHolder.textView.setPadding(padding, padding, padding, padding);
        viewHolder.textView.setText(getItem(position));
        viewHolder.textView.setTextColor(Color.BLACK);
        viewHolder.textView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        viewHolder.textView.setBackgroundResource(R.drawable.item_click_selector);
        return convertView;
    }

    private static class ViewHolder {
        private TextView textView;
    }
}
