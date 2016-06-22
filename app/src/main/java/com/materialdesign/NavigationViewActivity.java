package com.materialdesign;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.utils.Utils;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView((R.layout.activity_navigation_view))
public class NavigationViewActivity extends RoboActivity {

    @InjectView(R.id.drawerLayout)
    private DrawerLayout drawerLayout;

    @InjectView(R.id.openDrawer)
    private Button openDrawer;

    @InjectView(R.id.navigation_view)
    private NavigationView navigation_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDrawer();
        setNavigation();
    }

    private void setNavigation() {
        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            private MenuItem preItem;

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                if (preItem != null)
                    preItem.setChecked(false);
                item.setChecked(true);
                preItem = item;

                switch (item.getItemId()) {
                    case R.id.item1:
                    case R.id.item2:
                    case R.id.item3:
                    case R.id.sub_item1:
                    case R.id.sub_item2:
                        showMsg(item.getTitle().toString());
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });
        navigation_view.setCheckedItem(R.id.item1);//init first tab
    }

    private void setDrawer() {
        openDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawerLayout.isDrawerOpen(navigation_view)) {
                    drawerLayout.openDrawer(navigation_view);
                } else {
                    drawerLayout.closeDrawer(navigation_view);
                }
            }
        });
    }

    private void showMsg(String msg) {
        Utils.showToast(this, msg);
    }
}
