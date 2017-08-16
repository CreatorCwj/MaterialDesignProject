//package com.adapter;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.materialdesign.R;
//
///**
// * Created by cwj on 16/8/17.
// */
//public class TestAdapter extends BaseAdapter {
//
//    @Override
//    public int getCount() {
//        return 0;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        int type = getItemViewType(position);
//        if (type == 1) {
//            ViewHolder1 viewHolder;
//            if (convertView == null) {
//                viewHolder = new ViewHolder1();
//                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item1, parent, false);
//                viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
//                convertView.setTag(viewHolder);
//            } else {
//                viewHolder = (ViewHolder1) convertView.getTag();
//            }
//            viewHolder.textView.setText(getItem(position));
//        } else if (type == 2) {
//            ViewHolder2 viewHolder;
//            if (convertView == null) {
//                viewHolder = new ViewHolder2();
//                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2, parent, false);
//                viewHolder.editText = (EditText) convertView.findViewById(R.id.editText);
//                convertView.setTag(viewHolder);
//            } else {
//                viewHolder = (ViewHolder2) convertView.getTag();
//            }
//            viewHolder.editText.setHint(getItem(position));
//        }
//        return convertView;
//    }
//
//    private class ViewHolder1 {
//
//        TextView textView;
//    }
//
//    private class ViewHolder2 {
//
//        private EditText editText;
//    }
//}
