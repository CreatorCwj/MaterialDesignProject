package com.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwj on 16/9/7.
 */
public class LVAdapter extends BaseAdapter {

    private List<String> list;

    private boolean secondType;

    public LVAdapter(List<String> list) {
        this.list = list;
    }

    public void addData(List<String> data) {
        this.list.addAll(data);
        notifyDataSetChanged();
    }

    public void resetData(boolean secondType, List<String> data) {
        this.secondType = secondType;
        this.list.clear();
        this.list.addAll(data);
        notifyDataSetChanged();
    }

//    @Override
//    public int getViewTypeCount() {
//        return 2;
//    }

    @Override
    public boolean hasStableIds() {
        return super.hasStableIds();
    }

//    @Override
//    public int getItemViewType(int position) {
//        return position==0?100:1000;
//    }


    @Override
    public int getItemViewType(int position) {
        return secondType ? 1 : 0;
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
        viewHolder.textView.setText(getItem(position));
        viewHolder.textView.setTextColor(Color.WHITE);
        viewHolder.textView.setBackgroundColor(secondType ? Color.BLUE : Color.BLACK);
//        viewHolder.textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addData(getData());
//            }
//        });
        return convertView;
    }

    private List<String> getData() {
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            datas.add("item" + i);
        }
        return datas;
    }

    private static class ViewHolder {
        private TextView textView;
    }
}
