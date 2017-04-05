package com.test_fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import com.TestModel;
import com.materialdesign.R;
import com.materialdesign.TestService;
import com.utils.Constants;
import com.utils.Utils;

import java.util.ArrayList;

public class TestFragment2Activity extends BaseActivity {

    private boolean isConnected;
    private TestService service;

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            service = ((TestService.TestBinder) iBinder).getService();
            isConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnected = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fragment2);
        Utils.showTaskInfo(this);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(TestFragment2Activity.this, TestFragment3Activity.class);
////                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);

                setResult(RESULT_OK);
                finish();
//                startActivity(new Intent(TestFragment2Activity.this, TestFragment2Activity.class));
            }
        });
        findViewById(R.id.random).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected && service != null) {
                    String text = TestFragment2Activity.this.getClass().getSimpleName() + " random is : " + service.getRandomInt();
                    Utils.showToast(getApplicationContext(), text);
                }
            }
        });
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.KEY_MODEL)) {
            TestModel testModel = intent.getParcelableExtra(Constants.KEY_MODEL);
        }
        if (intent.hasExtra(Constants.KEY_BOOLEAN)) {
            boolean b = intent.getBooleanExtra(Constants.KEY_BOOLEAN, false);
        }
        ArrayList<Uri> uris = (ArrayList<Uri>) intent.getSerializableExtra(Constants.KEY_PARCEL_URIS);
        Utils.showToast(this, uris.toString());

        ArrayList<Uri> p_uris = intent.getParcelableArrayListExtra(Constants.KEY_SERIALIZABLE_URIS);
        Utils.showToast(this, p_uris.toString());
    }

    @Override
    public void finish() {
        Intent intent = new Intent(this, TestFragment2Activity.class);
//        intent.putExtra(Constants.KEY_BOOLEAN, true);
        intent.putExtra(Constants.KEY_MODEL, new TestModel(3));
        setResult(RESULT_OK, intent);
        super.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isConnected) {
            bindService(TestService.getIntent(this, getClass().getSimpleName()), connection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isConnected) {
            unbindService(connection);
            isConnected = false;
        }
    }
}
