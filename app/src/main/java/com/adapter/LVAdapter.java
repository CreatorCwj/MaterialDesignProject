package com.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by cwj on 16/9/7.
 */
public class LVAdapter extends BaseAdapter {

    private List<String> list;

    public LVAdapter(List<String> list) {
        this.list = list;
    }

    public void addData(List<String> data){
        this.list.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean hasStableIds() {
        return super.hasStableIds();
    }

    @Override
    public int getItemViewType(int position) {
        return position==0?100:1000;
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
            convertView.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
            convertView.setBackgroundColor(Color.GREEN);
            viewHolder.textView = (TextView) convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(getItem(position));
        return convertView;
    }

    private static class ViewHolder {
        private TextView textView;
    }
}
