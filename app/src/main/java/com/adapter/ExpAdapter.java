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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwj on 16/8/19.
 */
public class ExpAdapter extends BaseExpandableListAdapter implements ExpandableListView.OnGroupClickListener {

    private static final int DATA_COUNT = 9;
    private static final int CHILD_DATA_COUNT = 9;

    private List<String> groups;
    private List<List<String>> childs;

    private boolean secondType;

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

    public void setData(boolean secondType, List<String> groups, List<List<String>> childs) {
        this.secondType = secondType;
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
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public int getGroupTypeCount() {
        return 2;
    }

    @Override
    public int getGroupType(int groupPosition) {
        return secondType ? 1 : 0;
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
        groupHolder.textView.setBackgroundColor(secondType ? Color.BLUE : Color.BLACK);
        groupHolder.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        int gc = getChildrenCount(groupPosition);
        ChildHolder childHolder;
        if (convertView == null) {
            convertView = new TextView(parent.getContext());
            convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dp2px(parent.getContext(), 50)));

            childHolder = new ChildHolder();
            childHolder.textView = (TextView) convertView;
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }
        childHolder.textView.setText(getChild(groupPosition, childPosition));
        childHolder.textView.setBackgroundColor(Color.WHITE);
        childHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData(false,getNewData(), getNewChildsData());
            }
        });
        return convertView;
    }

    private List<String> getData() {
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < DATA_COUNT; i++) {
            datas.add("item" + i);
        }
        return datas;
    }

    private List<String> getNewData() {
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < DATA_COUNT; i++) {
            datas.add("newItem" + i + "a\na\na\na\na\n");
        }
        return datas;
    }

    private List<List<String>> getChildsData() {
        List<List<String>> datas = new ArrayList<>();
        for (int i = 0; i < DATA_COUNT; i++) {
            List<String> list = new ArrayList<>();
            for (int j = 0; j < CHILD_DATA_COUNT; j++) {
                list.add("item-" + i + "child-" + j);
            }
            datas.add(list);
        }
        return datas;
    }

    private List<List<String>> getNewChildsData() {
        List<List<String>> datas = new ArrayList<>();
        for (int i = 0; i < DATA_COUNT; i++) {
            List<String> list = new ArrayList<>();
            for (int j = 0; j < CHILD_DATA_COUNT; j++) {
                list.add("newItem-" + i + "newChild-" + j);
            }
            datas.add(list);
        }
        return datas;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return true;
    }

    class ChildHolder {
        TextView textView;
    }

    class GroupHolder {
        TextView textView;
    }
}
