package com.adapter;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.utils.Utils;

import java.util.List;

/**
 * Created by cwj on 16/8/19.
 */
public class ExpAdapter extends BaseExpandableListAdapter implements ExpandableListView.OnGroupClickListener {

    private List<String> groups;
    private List<List<String>> childs;

    public ExpAdapter(List<String> groups, List<List<String>> childs) {
        this.groups = groups;
        this.childs = childs;
    }

    public void clear() {
        groups.clear();
        childs.clear();
        notifyDataSetChanged();
    }

    public void addData(List<String> groups, List<List<String>> childs) {
        this.groups.addAll(groups);
        this.childs.addAll(childs);
        notifyDataSetChanged();
    }

    public void setData(List<String> groups, List<List<String>> childs) {
        this.groups.clear();
        this.groups.addAll(groups);
        this.childs.clear();
        this.childs.addAll(childs);
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childs.get(groupPosition).size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
        return childs.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder;
        if (convertView == null) {
            convertView = new TextView(parent.getContext());
            convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dp2px(parent.getContext(), 50)));

            groupHolder = new GroupHolder();
            groupHolder.textView = (TextView) convertView;
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        groupHolder.textView.setText(getGroup(groupPosition));
        groupHolder.textView.setBackgroundColor(Color.BLACK);
        groupHolder.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView textView = new TextView(parent.getContext());
        textView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dp2px(parent.getContext(), 50)));
        textView.setText(getChild(groupPosition, childPosition));
        textView.setBackgroundColor(Color.WHITE);
        return textView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return true;
    }

    class GroupHolder {
        TextView textView;
    }
}
