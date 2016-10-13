package com.widget.dialog;

import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.materialdesign.R;
import com.utils.Utils;

/**
 * Created by cwj on 16/10/12.
 * 可输入文本对话框
 */
public class EditDialog extends BaseDialog<EditDialog,EditDialog.Builder> {

    //view
    private EditText editText;

    //attrs
    private String hint;//提示文本
    private String text;//显示文本
    private boolean singleLine;//单行显示
    private int minLines;//最少显示几行
    private int maxLines;//最多显示几行
    private int lines;//指定显示几行
    private int maxChars;//最多输入的字符

    @Override
    protected void createCustomAttrs(@NonNull Builder builder) {
        setHint(builder.hint);
        setText(builder.text);
        setSingleLine(builder.singleLine);
        setMinLines(builder.minLines);
        setMaxLines(builder.maxLines);
        setLines(builder.lines);
        setMaxChars(builder.maxChars);
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        if (editText != null) {
            return editText.getText().toString();
        }
        return "";
    }

    private void setSingleLine(boolean singleLine) {
        this.singleLine = singleLine;
    }

    private void setMinLines(int minLines) {
        this.minLines = minLines;
    }

    private void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }

    private void setLines(int lines) {
        this.lines = lines;
    }

    private void setMaxChars(int maxChars) {
        this.maxChars = maxChars;
    }

    @Override
    protected void onCustomViewCreated(@NonNull View view) {
        editText.setHint(hint);
        editText.setText(text);
        editText.setSelection(text == null ? 0 : text.length());
        editText.setSingleLine(singleLine);
        if (lines > 0) {
            editText.setLines(lines);
        }
        if (minLines > 0) {
            editText.setMinLines(minLines);
        }
        if (maxLines > 0) {
            editText.setMaxLines(maxLines);
        }
        if (maxChars > 0) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxChars)});
        }
    }

    @Override
    protected View onCreateCustomView(@NonNull ViewGroup container) {
        editText = new EditText(getContext());
        editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        editText.setHintTextColor(getResources().getColor(R.color.light_gray));
        editText.setTextColor(getResources().getColor(R.color.light_black));
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        editText.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        editText.setBackground(null);
        int padding = Utils.dp2px(container.getContext(), 15);
        editText.setPadding(padding, hasTitleView() ? 0 : padding, padding, hasButtonView() ? 0 : padding);
        return editText;
    }

    public static class Builder extends BaseDialog.Builder<EditDialog, Builder> {

        private String hint;//提示文本
        private String text;//显示文本
        private boolean singleLine = true;//单行显示
        private int minLines = -1;//最少显示几行
        private int maxLines = -1;//最多显示几行
        private int lines = -1;//指定显示几行
        private int maxChars = -1;//最多输入的字符

        public Builder() {
        }

        public Builder hint(String hint) {
            this.hint = hint;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder singleLine(boolean singleLine) {
            this.singleLine = singleLine;
            return this;
        }

        public Builder minLines(int minLines) {
            this.minLines = minLines;
            singleLine(false);
            return this;
        }

        public Builder maxLines(int maxLines) {
            this.maxLines = maxLines;
            singleLine(false);
            return this;
        }

        public Builder lines(int lines) {
            this.lines = lines;
            singleLine(false);
            return this;
        }

        public Builder maxChars(int maxChars) {
            this.maxChars = maxChars;
            return this;
        }

        @NonNull
        @Override
        protected EditDialog getDialog() {
            return new EditDialog();
        }
    }
}
