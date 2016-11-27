package com.materialdesign;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.adapter.ExpAdapter;
import com.adapter.LVAdapter;
import com.utils.Utils;
import com.widget.pinned2.BaseListPinnedLayout;
import com.widget.pinned2.GroupListPinnedLayout;

import java.util.ArrayList;
import java.util.List;

public class PinnedActivity extends AppCompatActivity {

    private static final int DATA_COUNT = 9;
    private static final int CHILD_DATA_COUNT = 9;

//    private PinnedScrollView psv;
//    private PinnedListView plv;

    private ListView lv;

    private ExpandableListView pelv;
    private GroupListPinnedLayout glpl;

    private ExpAdapter expAdapter;

    private LVAdapter lvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinned);
        //pelv
        pelv = (ExpandableListView) findViewById(R.id.pelv);
        pelv.addHeaderView(getHeaderButton());
        expAdapter = new ExpAdapter(getData(), getChildsData());
        expAdapter.addData(getData(), getChildsData());
        pelv.setAdapter(expAdapter);
        pelv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Utils.showToast(parent.getContext(), "groupClick:" + groupPosition);
                return true;
            }
        });
//        pelv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                expAdapter.setData(getNewData(), getNewChildsData());
//                return false;
//            }
//        });
        glpl = (GroupListPinnedLayout) findViewById(R.id.glpl);
        glpl.setOnListPinnedViewClickListener(new BaseListPinnedLayout.OnListPinnedViewClickListener() {
            @Override
            public void onListPinnedViewClick(@NonNull View headerView, int position) {
                Utils.showToast(headerView.getContext(), "headerClick:" + position);
            }
        });

        //lv
//        lv = (ListView) findViewById(R.id.lv);
////        lv.addHeaderView(getHeaderButton());
//        lvAdapter = new LVAdapter(getData());
//        lv.setAdapter(lvAdapter);
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                lvAdapter.resetData(true, getNewData());
//            }
//        });

        //SV
//        psv = (PinnedScrollView) findViewById(R.id.psv);
//        View headerView = psv.getHeaderView();
//        headerView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Utils.showToast(getApplicationContext(), "header-click");
//            }
//        });
//        View anchorView = psv.getAnchorView();
//        anchorView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Utils.showToast(getApplicationContext(), "anchor-click");
//            }
//        });
//        psv.postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                psv.setCanPinned(false);
//            }
//        }, 2000);
        //LV
//        plv = (PinnedListView) findViewById(R.id.plv);
//        plv.addHeaderView(getHeaderButton());
//        plv.setAdapter(new LVAdapter(getData()));
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
            datas.add("newItem" + i);
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

    private View getHeaderButton() {
        Button button = new Button(this);
        button.setText("heheda");
        button.setBackgroundColor(Color.RED);
        button.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dp2px(this, 100)));
        return button;
    }
}
