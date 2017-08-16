package com.widget.dialog;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.materialdesign.R;
import com.utils.Utils;

/**
 * Created by cwj on 16/10/11.
 * TextView信息提示对话框
 */
public class MessageDialog extends BaseDialog<MessageDialog, MessageDialog.Builder> {

    //view
    private TextView textView;

    //attrs
    private String message;//内容信息
    private int messageGravity;//内容对齐方式

    private void setMessageGravity(int messageGravity) {
        this.messageGravity = messageGravity;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    protected void createCustomAttrs(@NonNull Builder builder) {
        setMessage(builder.message);
        setMessageGravity(builder.messageGravity);
    }

    @Override
    protected void onCustomViewCreated(@NonNull View view) {
        textView.setText(message);
        textView.setVisibility(TextUtils.isEmpty(message) ? View.GONE : View.VISIBLE);
        textView.setGravity(messageGravity);
    }

    @Override
    protected View onCreateCustomView(@NonNull ViewGroup container) {
        textView = new TextView(getContext());
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setSingleLine(false);
        textView.setTextColor(getResources().getColor(R.color.light_black));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        int padding = Utils.dp2px(container.getContext(), 15);
        textView.setPadding(padding, hasTitleView() ? 0 : padding, padding, hasButtonView() ? 0 : padding);
        return textView;
    }

    public static class Builder extends BaseDialog.Builder<MessageDialog, Builder> {

        private String message;//内容信息
        private int messageGravity = Gravity.START;//内容对齐方式

        public Builder() {
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder messageGravity(int messageGravity) {
            this.messageGravity = messageGravity;
            return this;
        }

        @NonNull
        @Override
        protected MessageDialog getDialog() {
            return new MessageDialog();
        }
    }
}
