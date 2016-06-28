package com.materialdesign;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_photo_view)
public class PhotoViewActivity extends RoboFragmentActivity {

    @InjectView(R.id.viewPager)
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<BigBitmapFragment> fragments = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            fragments.add(BigBitmapFragment.newInstance(i));
        }
        new MyAdapter(this, getSupportFragmentManager(), viewPager, fragments);
    }

    private class MyAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

        protected Context context;
        protected FragmentManager fm;
        protected ViewPager viewPager;
        protected List<BigBitmapFragment> fragments;

        public MyAdapter(Context context, FragmentManager fm, ViewPager viewPager, List<BigBitmapFragment> fragments) {
            this.context = context;
            this.fm = fm;
            this.viewPager = viewPager;
            this.fragments = fragments;
            this.viewPager.setAdapter(this);
            this.viewPager.addOnPageChangeListener(this);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = fragments.get(position);
            if (!fragment.isAdded()) {//第一次添加
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(fragment, fragment.getClass().getSimpleName());
                ft.commitAllowingStateLoss();//提交生效,立即执行的话会执行fragment的生命周期
                fm.executePendingTransactions();//立刻执行事务,commit只是提交,是否理科执行依赖于系统性能
            }
            //添加过的删除时只是把view移除了,再添加时只需把view加进来即可
            if (fragment.getView() != null && fragment.getView().getParent() == null) {
                container.addView(fragment.getView());
            }
            return fragment.getView();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Fragment fragment = fragments.get(position);
            if (fragment != null && fragment.getView() != null)
                container.removeView(fragment.getView());
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < fragments.size(); i++) {
                if (i == position)
                    continue;
                BigBitmapFragment fragment = fragments.get(i);
                if (fragment != null)//别的图片恢复原状(可能有放大的)
                    fragment.resetPhotoView();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
