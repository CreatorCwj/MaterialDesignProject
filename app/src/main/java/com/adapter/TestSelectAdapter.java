package com.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.materialdesign.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwj on 17/6/7.
 */

public class TestSelectAdapter extends BaseAdapter {

    private final List<String> dataList;

    public TestSelectAdapter(List<String> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public String getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_select_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.setData(getItem(position), position % 2 == 0);
        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.tv)
        TextView tv;

        private ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        private void setData(String data, boolean selected) {
            tv.setText(data);
            tv.setSelected(selected);
        }
    }
}
