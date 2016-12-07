package com.test_fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.materialdesign.R;
import com.utils.Utils;

public class TextFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_fragment);
//        Utils.showTaskInfo(this);
//        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(TextFragmentActivity.this, TestFragment2Activity.class));
//            }
//        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("organize failed");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Utils.showToast(getApplicationContext(), "positive");
            }
        });
        //取消对话框也重新扫描
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Utils.showToast(getApplicationContext(), "cancel");
            }
        });
        builder.setCancelable(true);
        builder.show();
    }
}
