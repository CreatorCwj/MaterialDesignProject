package com.widget.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.materialdesign.R;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwj on 16/10/13.
 * 列表对话框
 */
public class ListDialog extends BaseDialog<ListDialog, ListDialog.Builder> {

    //view
    private ListView listView;
    private SelectAdapter selectAdapter;

    //attrs
    private List<ItemBean> items;//items
    private int itemGravity;//item文字对齐方式
    private boolean itemClickDismiss;//item点击取消对话框
    private AdapterView.OnItemClickListener onItemClickListener;//itemClickListener

    private void setItemClickDismiss(boolean itemClickDismiss) {
        this.itemClickDismiss = itemClickDismiss;
    }

    private void setItems(List<ItemBean> items) {
        this.items = items;
    }

    private void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void setItemGravity(int itemGravity) {
        this.itemGravity = itemGravity;
    }

    @Override
    protected void createCustomAttrs(@NonNull Builder builder) {
        setItems(builder.items);
        setItemGravity(builder.itemGravity);
        setItemClickDismiss(builder.itemClickDismiss);
        setOnItemClickListener(builder.onItemClickListener);
    }

    @Override
    protected void onCustomViewCreated(@NonNull View view) {
        if (selectAdapter == null) {
            selectAdapter = new SelectAdapter();
        }
        selectAdapter.setItemGravity(itemGravity);
        selectAdapter.setData(items);
        listView.setAdapter(selectAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (itemClickDismiss) {
                    dismissAllowingStateLoss();
                }
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(parent, view, position, id);
                }
            }
        });
    }

    @Override
    protected View onCreateCustomView(@NonNull ViewGroup container) {
        listView = new ListView(getContext());
        listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        listView.setDivider(new ColorDrawable(getResources().getColor(R.color.divider)));
        listView.setDividerHeight(Utils.dp2px(getContext(), 1));//与setDivider(ColorDrawable(color))顺序不能颠倒,否则没有divider高度
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        return listView;
    }

    public static class Builder extends BaseDialog.Builder<ListDialog, Builder> {

        private List<ItemBean> items;//items
        private int itemGravity = Gravity.START;//item文字对齐方式
        private boolean itemClickDismiss = true;//item点击取消对话框
        private AdapterView.OnItemClickListener onItemClickListener;//itemClickListener

        public Builder() {
        }

        private void checkList() {
            if (items == null) {
                items = new ArrayList<>();
            }
        }

        public Builder addItem(String text) {
            checkList();
            items.add(new ItemBean(text, 0));
            return this;
        }

        public Builder addItem(String text, int iconId) {
            checkList();
            items.add(new ItemBean(text, iconId));
            return this;
        }

        public Builder items(String[] texts) {
            if (texts == null) {
                return this;
            }
            checkList();
            for (String text : texts) {
                items.add(new ItemBean(text, 0));
            }
            return this;
        }

        public Builder items(String[] texts, int[] iconIds) {
            if (texts == null || iconIds == null || texts.length != iconIds.length) {
                return this;
            }
            checkList();
            int len = texts.length;
            for (int i = 0; i < len; i++) {
                items.add(new ItemBean(texts[i], iconIds[i]));
            }
            return this;
        }

        public Builder onItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
            return this;
        }

        public Builder itemClickDismiss(boolean itemClickDismiss) {
            this.itemClickDismiss = itemClickDismiss;
            return this;
        }

        public Builder itemGravity(int itemGravity) {
            this.itemGravity = itemGravity;
            return this;
        }

        @NonNull
        @Override
        protected ListDialog getDialog() {
            return new ListDialog();
        }
    }

    private static class ItemBean {

        private final String text;
        private final int iconId;

        public ItemBean(String text, int iconId) {
            this.text = text;
            this.iconId = iconId;
        }
    }

    private static class SelectAdapter extends BaseAdapter {

        private List<ItemBean> dataList = new ArrayList<>();

        private int itemGravity;

        public void setItemGravity(int itemGravity) {
            this.itemGravity = itemGravity;
        }

        public void setData(List<ItemBean> items) {
            if (items != null) {
                dataList.clear();
                dataList.addAll(items);
                notifyDataSetChanged();
            }
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public ItemBean getItem(int position) {
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
                viewHolder = new ViewHolder();
                convertView = getTextView(parent);
                viewHolder.textView = (TextView) convertView;
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            setBackground(viewHolder.textView, position);
            viewHolder.textView.setGravity(itemGravity);
            ItemBean itemBean = getItem(position);
            if (itemBean != null) {
                viewHolder.textView.setText(itemBean.text);
                viewHolder.textView.setCompoundDrawablesWithIntrinsicBounds(itemBean.iconId, 0, 0, 0);
            } else {
                viewHolder.textView.setText("");
                viewHolder.textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
            return convertView;
        }

        private void setBackground(TextView textView, int position) {
            if (position == 0 && getCount() == 1) {//唯一一个
                textView.setBackgroundResource(R.drawable.select_dialog_corners_item_back);
            } else {
                int resId = position == 0 ? R.drawable.select_dialog_top_item_back : position == getCount() - 1 ? R.drawable.select_dialog_bottom_item_back : R.drawable.select_dialog_middle_item_back;
                textView.setBackgroundResource(resId);
            }
        }

        private TextView getTextView(ViewGroup parent) {
            TextView textView = new TextView(parent.getContext());
            textView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));//要用AbsListView.LayoutParams,否则低版本有问题
            int padding = Utils.dp2px(parent.getContext(), 15);
            textView.setPadding(padding, padding, padding, padding);
            textView.setCompoundDrawablePadding(padding);
            textView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            textView.setTextColor(parent.getContext().getResources().getColor(R.color.light_black));
            textView.setSingleLine(true);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            return textView;
        }

        private static class ViewHolder {
            private TextView textView;
        }
    }
}
