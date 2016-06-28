package com.materialdesign;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.utils.Utils;

import roboguice.fragment.RoboFragment;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class BigBitmapFragment extends RoboFragment {

    private PhotoView imageView;

    private PhotoViewAttacher attacher;//除了PhotoView,任何ImageView的子类均可,只需new一个Attacher对象与view绑定进行管理(改变图片后进行attacher.update())即可

    private int pos;

    public static BigBitmapFragment newInstance(int pos) {
        Bundle args = new Bundle();
        args.putInt("pos", pos);
        BigBitmapFragment fragment = new BigBitmapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pos = getArguments().getInt("pos");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        imageView = new PhotoView(getActivity());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return imageView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        attacher = (PhotoViewAttacher) imageView.getIPhotoViewImplementation();
        if (pos % 2 == 0)
            imageView.setImageResource(R.drawable.arrow);
        else imageView.setImageResource(R.drawable.ic_contact);
        attacher.setOnLongClickListener(new View.OnLongClickListener() {//处理了长按事件
            @Override
            public boolean onLongClick(View v) {
                Utils.showToast(getActivity(), "longClick");
                return true;
            }
        });
        attacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {//在图片矩阵范围内单击事件
                Utils.showToast(getActivity(), "photoTap");
            }

            @Override
            public void onOutsidePhotoTap() {//在图片矩阵范围外单击事件
                Utils.showToast(getActivity(), "outsideTap");
            }
        });
        attacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float v, float v1) {//单击view都会触发,只不过如果在图片展示区域单击,则触发完上述onPhotoTap方法后就返回了,如果没有触发,则会触发该方法
                Utils.showToast(getActivity(), "viewTap");
            }
        });
    }

    public void resetPhotoView() {
        if (imageView != null) {
            attacher.update();//重置矩阵
        }
    }

}
