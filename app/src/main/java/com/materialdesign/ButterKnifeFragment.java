package com.materialdesign;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemSelected;
import butterknife.Optional;

/**
 * Created by cwj on 17/4/24.
 */

public class ButterKnifeFragment extends Fragment {

    @Nullable
    @BindView(R.id.fragment_bk_btn)
    Button btn;

    @Nullable
    @BindView(R.id.listView)
    ListView listView;

    @BindString((R.string.butter_knife_text))
    String bkText;

    @BindColor(R.color.color50)
    int color;

    @Nullable
    @BindViews({R.id.btns1, R.id.btns2})
    List<Button> buttons;

    static final ButterKnife.Action<Button> ACTION_DOWN = new ButterKnife.Action<Button>() {
        @Override
        public void apply(@NonNull Button view, int index) {
            view.setText("ACTION_DOWN");
        }
    };

    static final ButterKnife.Setter<Button, Integer> TEXT_COLOR = new ButterKnife.Setter<Button, Integer>() {
        @Override
        public void set(@NonNull Button button, Integer value, int index) {
            button.setTextColor(value);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_butter_knife, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn.setText(bkText);
        listView.setAdapter(new MyAdapter(getDatas()));
    }

    @Optional
    @OnClick(R.id.fragment_bk_btn)
    public void onBtnClick() {
        ButterKnife.apply(buttons, ACTION_DOWN);
        ButterKnife.apply(buttons, TEXT_COLOR, Color.RED);
        ButterKnife.apply(buttons, View.ALPHA, 0.5f);
    }

    @Optional
    @OnClick({R.id.btns1, R.id.btns2})
    public void onBtnsClick(Button button) {
        Utils.showToast(getContext(), button.getText().toString());
    }

    @Optional
    @OnItemClick(R.id.listView)
    public void onItemClick(int pos, long id) {
        Utils.showToast(getContext(), "" + pos);
    }

    @Optional
    @OnItemSelected(R.id.listView)
    public void onItemSelected(int pos, long id) {

    }

    @Optional
    @OnItemSelected(value = R.id.listView, callback = OnItemSelected.Callback.NOTHING_SELECTED)
    public void onNothingSelected() {

    }

    @NonNull
    private List<String> getDatas() {
        List<String> dataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            dataList.add("btn" + i);
        }
        return dataList;
    }

    static class MyAdapter extends BaseAdapter {

        private final List<String> dataList;

        private MyAdapter(@NonNull List<String> dataList) {
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
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_butter_knife, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.setData(getItem(position));
            return convertView;
        }

        static class ViewHolder {

            @BindString(R.string.butter_knife_text)
            String bkText;

            @BindView(R.id.item_btn)
            Button button;

            private ViewHolder(@NonNull View view) {
                ButterKnife.bind(this, view);
//                button = ButterKnife.findById(view,R.id.item_btn);
            }

            private void setData(String text) {
                button.setText(text + bkText);
                button.setTextColor(Color.BLACK);
            }
        }
    }
}
