package com.test_fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.materialdesign.R;

public class
TextFragmentActivity extends Activity {

    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_fragment);
        iv = (ImageView) findViewById(R.id.iv);
//        Utils.showTaskInfo(this);
//        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(TextFragmentActivity.this, TestFragment2Activity.class));
//            }
//        });
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("organize failed");
//        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Utils.showToast(getApplicationContext(), "positive");
//            }
//        });
//        //取消对话框也重新扫描
//        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialogInterface) {
//                Utils.showToast(getApplicationContext(), "cancel");
//            }
//        });
//        builder.setCancelable(true);
//        builder.show();

        Context context;
        String str;

        //资源都可以获取
//        context = getBaseContext();
//        str = context.getResources().getString(R.string.app_name);
//        context = getApplicationContext();
//        str = context.getResources().getString(R.string.app_name);
//        context = getApplication();//=getApplicationContext()

        //加载布局UI,AppContext自定义Theme可能用不了
//        LayoutInflater.from(this).inflate(R.layout.activity_dialog, null);
//        LayoutInflater.from(getBaseContext()).inflate(R.layout.activity_dialog, null);
//        LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_dialog, null);

        //启动Activity、Service、BroadcastReceiver:都可以，只不过非Activity的context启动Activity时可能需要新建Task来启动

        //展示Dialog
//        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new AlertDialog.Builder(TextFragmentActivity.this)
//                        .setTitle("title1").create().show();
////                new AlertDialog.Builder(getBaseContext())
////                        .setTitle("title2").create().show();
//                new AlertDialog.Builder(getApplicationContext())
//                        .setTitle("title3").create().show();
//            }
//        });

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    Class<?> clazz = Class.forName("com.squareup.okhttp.OkHttpClient");
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//                Picasso.with(getApplicationContext()).load("http://pic44.nipic.com/20140717/12432466_121957328000_2.jpg").into((ImageView) findViewById(R.id.iv));
                Glide.with(getApplicationContext())
                        .load("http://pic44.nipic.com/20140717/12432466_121957328000_2.jpg")
                        .into(iv);
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable drawable = iv.getDrawable();
                if (drawable instanceof GlideBitmapDrawable) {
                    Bitmap bitmap = ((GlideBitmapDrawable) drawable).getBitmap();
                    bitmap.recycle();
                    iv.setImageBitmap(bitmap);
                }
            }
        });
    }
}
