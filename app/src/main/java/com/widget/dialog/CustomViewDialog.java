package com.widget.dialog;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by cwj on 16/10/13.
 * 外部直接传入自定义view的对话框
 */
public class CustomViewDialog extends BaseDialog<CustomViewDialog, CustomViewDialog.Builder> {

    //attrs
    private View customView;//自定义view

    private void setCustomView(View customView) {
        this.customView = customView;
    }

    @Override
    protected void createCustomAttrs(@NonNull Builder builder) {
        setCustomView(builder.customView);
    }

    @Override
    protected void onCustomViewCreated(@NonNull View view) {
        //do nothing
    }

    @Override
    protected View onCreateCustomView(@NonNull ViewGroup container) {
        return customView;
    }

    public static class Builder extends BaseDialog.Builder<CustomViewDialog, Builder> {

        private View customView;//自定义view,必填参数

        public Builder(View customView) {
            this.customView = customView;
        }

        @NonNull
        @Override
        protected CustomViewDialog getDialog() {
            return new CustomViewDialog();
        }
    }
}
