package com.widget.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.materialdesign.R;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwj on 16/10/11.
 * 统一对话框基类
 */
public abstract class BaseDialog<DialogType extends BaseDialog, BuilderType extends BaseDialog.Builder> extends AppCompatDialogFragment {

    //view
    private TextView titleTextView;
    private FrameLayout customContainer;
    private LinearLayout buttonsContainer;

    //attrs
    private int iconId;//图标
    private String title;//标题
    private int titleGravity;//标题对齐方式---Gravity.xxx
    private boolean clickDismiss;//按钮点击后对话框消失
    private boolean cancelable;//是否可以取消对话框
    private boolean canceledOnTouchOutside;//触摸外界是否取消对话框
    private List<ButtonBean<DialogType>> buttonBeen;//buttons

    private void create(@NonNull BuilderType b) {
        Builder builder = b;
        setIconId(builder.iconId);
        setTitle(builder.title);
        setTitleGravity(builder.titleGravity);
        setClickDismiss(builder.clickDismiss);
        setCancelable(builder.cancelable);
        setCanceledOnTouchOutside(builder.canceledOnTouchOutside);
        //noinspection unchecked
        setButtonBeen(builder.buttonBeen);
        createCustomAttrs(b);
    }

    /**
     * 设置子类自己的属性
     */
    protected abstract void createCustomAttrs(@NonNull BuilderType builder);

    private void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private void setTitleGravity(int titleGravity) {
        this.titleGravity = titleGravity;
    }

    private void setClickDismiss(boolean clickDismiss) {
        this.clickDismiss = clickDismiss;
    }

    private void setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        this.canceledOnTouchOutside = canceledOnTouchOutside;
    }

    private void setButtonBeen(List<ButtonBean<DialogType>> buttonBeen) {
        this.buttonBeen = buttonBeen;
    }

    @Override
    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        super.setCancelable(cancelable);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {//恢复时直接销毁即可
            dismiss();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.base_dialog_layout, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleTextView = (TextView) view.findViewById(R.id.dialog_title_textView);
        customContainer = (FrameLayout) view.findViewById(R.id.dialog_custom_container);
        buttonsContainer = (LinearLayout) view.findViewById(R.id.dialog_buttons_container);
        //根据属性设置view
        initCancelable();
        initTitle();
        initButtons();
        initCustomView();
    }

    private void initCustomView() {
        View view = onCreateCustomView(customContainer);
        if (view == null) {
            throw new IllegalArgumentException("custom view can not be null");
        }
        if (view.getParent() != customContainer) {//需要加入到container中
            if (view.getParent() instanceof ViewGroup) {//有parent先移除
                ((ViewGroup) view.getParent()).removeView(view);
            }
            customContainer.addView(view);
        }
        onCustomViewCreated(view);
    }

    /**
     * 自定义view创建完成
     */
    protected abstract void onCustomViewCreated(@NonNull View view);

    /**
     * 获取子类的自定义内容view
     */
    protected abstract View onCreateCustomView(@NonNull ViewGroup container);

    public void show(FragmentManager manager) {
        if (!isAdded()) {
            super.show(manager, null);
        }
    }

    public int show(FragmentTransaction transaction) {
        if (!isAdded()) {
            return super.show(transaction, null);
        }
        return -1;
    }

    private void initCancelable() {
        setCancelable(cancelable);
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        }
    }

    private void initButtons() {
        if (!hasButtonView()) {
            buttonsContainer.setVisibility(View.GONE);
            return;
        }
        int size = buttonBeen.size();
        ButtonBean<DialogType> buttonBean;
        TextView textView;
        for (int i = 0; i < size; ++i) {
            buttonBean = buttonBeen.get(i);
            if (buttonBean == null) {
                continue;
            }
            textView = getButton(buttonBean);
            if (i == size - 1 && textView.getLayoutParams() instanceof LinearLayout.LayoutParams) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
                params.rightMargin = Utils.dp2px(getContext(), 10);
                if (Build.VERSION.SDK_INT >= 17) {
                    params.setMarginEnd(params.rightMargin);
                }
            }
            buttonsContainer.addView(textView);
        }
    }

    private TextView getButton(@NonNull final ButtonBean<DialogType> buttonBean) {
        TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.base_dialog_btn, buttonsContainer, false);
        textView.setText(buttonBean.text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickDismiss) {//点击自动取消
                    dismissAllowingStateLoss();
                }
                if (buttonBean.onButtonClickListener != null) {
                    //noinspection unchecked
                    buttonBean.onButtonClickListener.onButtonClick((DialogType) BaseDialog.this);
                }
            }
        });
        return textView;
    }

    private void initTitle() {
        if (!hasTitleView()) {//不应该显示titleView
            titleTextView.setVisibility(View.GONE);
        } else {//应该显示titleView
            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setCompoundDrawablesWithIntrinsicBounds(iconId, 0, 0, 0);
            titleTextView.setText(title);
            titleTextView.setGravity(titleGravity);
        }
    }

    /**
     * 是否显示标题栏(用于子类设置view)
     */
    protected boolean hasTitleView() {
        return iconId != 0 || !TextUtils.isEmpty(title);
    }

    /**
     * 是否显示按钮栏(用于子类设置view)
     */
    protected boolean hasButtonView() {
        return buttonBeen != null && buttonBeen.size() > 0;
    }

    public abstract static class Builder<DialogType extends BaseDialog, BuilderType extends Builder> {

        private int iconId;//图标
        private String title;//标题
        private int titleGravity = Gravity.START;//标题对齐方式---Gravity.xxx
        private boolean clickDismiss = true;//按钮点击后对话框消失
        private boolean cancelable = true;//是否可以取消对话框
        private boolean canceledOnTouchOutside = true;//触摸外界是否取消对话框
        private List<ButtonBean<DialogType>> buttonBeen;//buttons

        public Builder() {
        }

        private BuilderType castBuilder() {
            //noinspection unchecked
            return (BuilderType) this;
        }

        public BuilderType iconId(int iconId) {
            this.iconId = iconId;
            return castBuilder();
        }

        public BuilderType title(String title) {
            this.title = title;
            return castBuilder();
        }

        public BuilderType titleGravity(int titleGravity) {
            this.titleGravity = titleGravity;
            return castBuilder();
        }

        public BuilderType clickDismiss(boolean clickDismiss) {
            this.clickDismiss = clickDismiss;
            return castBuilder();
        }

        public BuilderType cancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return castBuilder();
        }

        public BuilderType canceledOnTouchOutside(boolean canceledOnTouchOutside) {
            this.canceledOnTouchOutside = canceledOnTouchOutside;
            return castBuilder();
        }

        public BuilderType addButton(String text, OnButtonClickListener<DialogType> onClickListener) {
            checkList();
            buttonBeen.add(new ButtonBean<>(text, onClickListener));
            return castBuilder();
        }

        public BuilderType addButton(String text) {
            checkList();
            buttonBeen.add(new ButtonBean<>(text, (OnButtonClickListener<DialogType>) null));
            return castBuilder();
        }

        private void checkList() {
            if (buttonBeen == null) {
                buttonBeen = new ArrayList<>();
            }
        }

        /**
         * 获取子类的具体dialog对象
         */
        @NonNull
        protected abstract DialogType getDialog();

        /**
         * 构建dialog对象,设置属性,不能不被子类重写
         */
        @NonNull
        final public DialogType build() {
            DialogType dialog = getDialog();
            //noinspection unchecked
            ((BaseDialog) dialog).create(this);
            return dialog;
        }

    }

    public interface OnButtonClickListener<DialogType extends BaseDialog> {
        void onButtonClick(@NonNull DialogType dialog);
    }

    private static class ButtonBean<DialogType extends BaseDialog> {

        private final String text;
        private final OnButtonClickListener<DialogType> onButtonClickListener;

        public ButtonBean(String text, OnButtonClickListener<DialogType> onButtonClickListener) {
            this.text = text;
            this.onButtonClickListener = onButtonClickListener;
        }

    }

}
