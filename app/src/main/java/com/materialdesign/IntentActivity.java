package com.materialdesign;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_intent)
public class IntentActivity extends RoboActivity {

    public static final String ACTION = "com.materialdesign.action.TARGET";
    public static final String CAT1 = "com.materialdesign.category.CAT1";
    public static final String CAT2 = "com.materialdesign.category.CAT2";

    public static final String BR_ACTION = "com.materialdesign.action.BroadcastReceiver";

    public static final String TEST_PROJECT = "com.testproject.action.MAIN_ACTIVITY";

    @InjectView(R.id.btn1)
    private Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(TEST_PROJECT);
//                intent.addCategory(CAT1);
//                intent.addCategory(CAT2);
//                Uri uri = Uri.parse("cwj://blog.com/chenwenjie");
//                intent.setData(uri);
//                intent.setDataAndType(uri, "image/*");

                Uri.Builder builder = new Uri.Builder();
                Uri uri = builder.scheme("cwj").authority("blog.com").path("chenwenjie")
                        .appendPath("path1").appendEncodedPath("path2").appendQueryParameter("key", "value").build();
                System.out.println(uri.toString());

                PackageManager pm = getPackageManager();
                ResolveInfo ri = pm.resolveActivity(intent, 0);
                if (ri != null)
                    startActivity(intent);
//                Intent i = Intent.createChooser(intent,"select");
//                startActivity(i);
//                sendBroadcast(intent);
            }
        });
    }
}
