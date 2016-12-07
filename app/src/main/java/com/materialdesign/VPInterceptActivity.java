package com.materialdesign;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class VPInterceptActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        List<VPInterceptFragment> fragments = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            fragments.add(VPInterceptFragment.newInstance(i));
        }
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager(), fragments));
    }

    private static class MyAdapter extends FragmentStatePagerAdapter {

        protected List<VPInterceptFragment> fragments;

        public MyAdapter(FragmentManager supportFragmentManager, List<VPInterceptFragment> fragments) {
            super(supportFragmentManager);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

    }

}
