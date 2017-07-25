package com.materialdesign;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.test_fragment.BaseActivity;
import com.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cwj on 17/7/11.
 * 动画测试:一定要cancel掉之前的，否则可能会有浪费资源、动画效果错乱等问题
 */

public class AnimatorActivity extends BaseActivity {

    private static final String TAG = "Animation";

    @BindView(R.id.container)
    LinearLayout container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.iv1)
    public void onIv1Click(final View view) {
        //值动画
        ValueAnimator animator = ValueAnimator.ofInt(0, Utils.dp2px(this, 100));
        animator.setInterpolator(new OvershootInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setStartDelay(1000);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.width = params.height = value;
                view.setLayoutParams(params);
                log("update:" + value + "---" + animation);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {

            private int count = 0;

            @Override
            public void onAnimationStart(Animator animation) {
                log("start");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                log("end");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                log("cancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if (count > 5) {
                    animation.cancel();//cancel停留在当前位置
                }
                ++count;
                log("repeat");
            }
        });
        animator.start();
    }

    @OnClick(R.id.iv2)
    public void onIv2Click(View view) {
        //对象动画(属性)
        //或用"translationX"代替，确保有setTranslationX(float)方法
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0, 500);
        animator.setInterpolator(new OvershootInterpolator());
        animator.setDuration(1000);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(1);
        animator.setAutoCancel(true);//API 18以上，当有其他处理该target的相应property的animator时，自动cancel掉这个
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                log(animation.getAnimatedValue().toString());
            }
        });
        animator.start();
    }

    @OnClick(R.id.iv3)
    public void onIv3Click(View view) {
        //组合动画
        //1.通过多个Holder组合(同时执行)
        PropertyValuesHolder tranXHolder = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0, 500);
        PropertyValuesHolder rotationXHolder = PropertyValuesHolder.ofFloat(View.ROTATION_X, 0, 360);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, tranXHolder, rotationXHolder);
        animator.setDuration(1000);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(1);
        animator.setAutoCancel(true);
        animator.start();
    }

    private AnimatorSet animatorSet;

    @OnClick(R.id.iv4)
    public void onIv4Click(View view) {
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        //组合动画
        //2.通过AnimatorSet(控制顺序)
        ObjectAnimator transAnim = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0, 500);
        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(view, View.ROTATION, 0, 360);
        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(view, View.ALPHA, 1, 0);
        alphaAnim.setRepeatMode(ValueAnimator.REVERSE);
        alphaAnim.setRepeatCount(1);

        animatorSet = new AnimatorSet();
        //同步执行
        //animatorSet.playTogether(transAnim, rotationAnim, alphaAnim);
        //顺序执行
        //animatorSet.playSequentially(transAnim, rotationAnim, alphaAnim);
        //流程控制(在alpha执行完成后,trans和rotation一起执行)
        //animatorSet.play(transAnim).with(rotationAnim).after(alphaAnim);
        //一个链式调用以play为基点展开，假如如果需要多个after或before，可以使用多个链式调用(同一个animator对象会当作一个)
        animatorSet.play(transAnim).after(rotationAnim);
        animatorSet.play(rotationAnim).after(alphaAnim);
        //AnimatorSet的duration会覆盖每个Animator的duration
        animatorSet.setDuration(1000);
        animatorSet.start();
    }

    @OnClick(R.id.iv5)
    public void onIv5Click(View view) {
        view.animate().translationX(500).rotation(360).setDuration(1000).start();
    }

    private int count = 0;

    @OnClick(R.id.iv6)
    public void onIv6Click(View view) {
        if (count % 2 == 0) {
            //View动画
            RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
            TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, 500, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
            //AnimationSet集合控制多个动画(无法控制顺序，而且执行效果和add顺序还有关系...)
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.setDuration(1000);
            animationSet.setFillAfter(true);//动画结束后保持位置(并不影响实际measure和layout)
            animationSet.addAnimation(rotateAnimation);
            animationSet.addAnimation(alphaAnimation);
            animationSet.addAnimation(translateAnimation);
            //view开启动画
            view.startAnimation(animationSet);
        } else {
            view.requestLayout();
        }
        ++count;
    }

    @OnClick(R.id.iv7)
    public void onIv7Click(View view) {
        //animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_in);
        view.startAnimation(animation);
    }

    public void test() {
        //xml方式加载动画
        //animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_in);

        //LayoutAnimation
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_translate_in);
        container.setLayoutAnimation(controller);
        container.startLayoutAnimation();

        //animator
        Animator animator = AnimatorInflater.loadAnimator(this, R.animator.alpha_animator);
    }

    private void log(String msg) {
        Log.i(TAG, msg);
    }

}
