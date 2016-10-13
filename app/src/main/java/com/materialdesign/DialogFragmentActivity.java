package com.materialdesign;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;

import com.utils.Utils;
import com.widget.dialog.BaseDialog;
import com.widget.dialog.CustomViewDialog;
import com.widget.dialog.EditDialog;
import com.widget.dialog.ListDialog;
import com.widget.dialog.MessageDialog;

public class DialogFragmentActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;

    private MessageDialog messageDialog;
    private EditDialog editDialog;
    private CustomViewDialog customViewDialog;
    private ListDialog listDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_fragment);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        initListener();
        initDialog();
    }

    private void initDialog() {
        messageDialog = new MessageDialog.Builder()
                .iconId(R.drawable.linkedin)
                .title("俺是标题").titleGravity(Gravity.START)
                .message("是否保存草稿").messageGravity(Gravity.START)
                .addButton("取消").addButton("确定", new BaseDialog.OnButtonClickListener<MessageDialog>() {
                    @Override
                    public void onButtonClick(@NonNull MessageDialog dialog) {
                        Utils.showToast(dialog.getContext(), dialog.getMessage());
                    }
                }).clickDismiss(true)
                .cancelable(true).canceledOnTouchOutside(true)
                .build();
        editDialog = new EditDialog.Builder()
                .title("菜品名称").titleGravity(Gravity.CENTER)
                .hint("请输入菜品名称")
                .text("默认文本").maxLines(5)
                .addButton("取消").addButton("完成", new BaseDialog.OnButtonClickListener<EditDialog>() {
                    @Override
                    public void onButtonClick(@NonNull EditDialog dialog) {
                        Utils.showToast(dialog.getContext(), dialog.getText());
                    }
                })
                .canceledOnTouchOutside(false)
                .build();
        customViewDialog = new CustomViewDialog.Builder(getCustomView())
                .title("自定义view").addButton("这很可以").addButton("硬")
                .build();
        listDialog = new ListDialog.Builder()
                .title("选择方式")
                .items(new String[]{"item0", "item1"}, new int[]{R.drawable.linkedin, R.drawable.linkedin})
                .addItem("item2",R.drawable.linkedin)
//                .itemGravity(Gravity.CENTER)
                .onItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Utils.showToast(parent.getContext(), "item" + position);
                    }
                })
                .addButton("取消")
                .build();
    }

    private View getCustomView() {
        Button button = new Button(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int padding = Utils.dp2px(this, 15);
        params.leftMargin = params.rightMargin = padding;
        button.setLayoutParams(params);
        button.setText("点我啊!");
        return button;
    }

    private void initListener() {
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                testMessageDialog();
                break;
            case R.id.btn2:
                testEditDialog();
                break;
            case R.id.btn3:
                testCustomViewDialog();
                break;
            case R.id.btn4:
                testListDialog();
                break;
        }
    }

    private void testListDialog() {
        listDialog.show(getSupportFragmentManager());
    }

    private void testCustomViewDialog() {
        customViewDialog.show(getSupportFragmentManager());
    }

    private void testEditDialog() {
        editDialog.show(getSupportFragmentManager());
    }

    private void testMessageDialog() {
        messageDialog.show(getSupportFragmentManager());
    }
}
