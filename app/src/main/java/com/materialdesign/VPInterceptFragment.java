package com.materialdesign;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class VPInterceptFragment extends Fragment {

    private static final String KEY_POS = "pos";

    public static VPInterceptFragment newInstance(int pos) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_POS, pos);
        VPInterceptFragment fragment = new VPInterceptFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.vp_inter_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        List<VPInterIVFragment> fragments = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            fragments.add(VPInterIVFragment.newIntance(i));
        }
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager(), fragments));

        Bundle bundle = getArguments();
        int pos = 0;
        if (bundle != null) {
            pos = bundle.getInt(KEY_POS);
        }
        view.setBackgroundColor(pos == 0 ? Color.BLACK : Color.WHITE);
    }

    private static class MyAdapter extends FragmentStatePagerAdapter {

        protected List<VPInterIVFragment> fragments;

        public MyAdapter(FragmentManager supportFragmentManager, List<VPInterIVFragment> fragments) {
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
