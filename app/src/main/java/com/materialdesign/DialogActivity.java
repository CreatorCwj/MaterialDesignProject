package com.materialdesign;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.test_fragment.BaseActivity;
import com.utils.Utils;
import com.widget.dialog.BaseDialog;
import com.widget.dialog.MessageDialog;

public class DialogActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new FrameLayout(this));
        MessageDialog.Builder builder = new MessageDialog.Builder()
                .cancelable(true)
                .canceledOnTouchOutside(true)
                .title("公告")
                .titleGravity(Gravity.CENTER)
                .message("盘古CRM系统预计在10月9日22:00迁移数据，预计持续2个小时，届时盘古系统暂停访问。请各位一线同事做好准备！")
                .messageGravity(Gravity.CENTER)
                .addButton("知道了", new BaseDialog.OnButtonClickListener<MessageDialog>() {
                    @Override
                    public void onButtonClick(@NonNull MessageDialog dialog) {
                        DialogActivity.this.finish();
                    }
                })
                .addButton("去查看", new BaseDialog.OnButtonClickListener<MessageDialog>() {
                    @Override
                    public void onButtonClick(@NonNull MessageDialog dialog) {
                        Utils.showToast(dialog.getContext(), "gotoH5");
                        DialogActivity.this.finish();
                    }
                });
        builder.build().show(getSupportFragmentManager());
    }
}
