package com.materialdesign;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;

import com.TestModel;
import com.test_fragment.BaseActivity;
import com.test_fragment.TestFragment2Activity;
import com.utils.Constants;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentStackActivity extends BaseActivity {

    @BindView(R.id.btn)
    Button btn;

    @BindView(R.id.random)
    Button random;

    @BindColor(R.color.color500)
    int color;

    private boolean isStart;

    @OnClick(R.id.btn)
    public void onBtnClick(View view) {
        Intent intent = new Intent(FragmentStackActivity.this, TestFragment2Activity.class);
        intent.putExtra(Constants.KEY_BOOLEAN, true);
        intent.putExtra(Constants.KEY_MODEL, new TestModel(2));
        intent.putExtra(Constants.KEY_SERIALIZABLE_URIS, getUris());
        intent.putParcelableArrayListExtra(Constants.KEY_PARCEL_URIS, getUris());
        startActivityForResult(intent, 100);
//                startActivity(new Intent(FragmentStackActivity.this, DialogActivity.class));
//                new AlertDialog.Builder(FragmentStackActivity.this)
//                        .setTitle("title")
//                        .setMessage("message")
//                        .setCancelable(true)
//                        .create().show();
    }

    private ArrayList<Uri> getUris() {
        ArrayList<Uri> uris = new ArrayList<>();
        uris.add(Uri.parse("http://www.baidu.com"));
        uris.add(Uri.parse("content://media/external/images/media"));
        return uris;
    }

    @OnClick(R.id.random)
    public void onRandomClick() {
        CookieSyncManager.createInstance(getApplicationContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        System.out.println("cookie1:" + cookieManager.getCookie("url"));

        cookieManager.setCookie("url", "key1=value1");
        System.out.println("cookie2:" + cookieManager.getCookie("url"));

        cookieManager.setCookie("url", "key2=value2");
        System.out.println("cookie3:" + cookieManager.getCookie("url"));

        cookieManager.setCookie("url", "key2=newValue2");
        System.out.println("cookie4:" + cookieManager.getCookie("url"));

        cookieManager.setCookie("url", null);
        System.out.println("cookie5:" + cookieManager.getCookie("url"));

        System.out.println("instant-run2");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_fragment_stack);
//        if (savedInstanceState != null) {//恢复时一定不能replace,否则会因为恢复的stack错乱
//            return;
//        }
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.container, FirstLevelFragment.getInstance(this))
//                .commitAllowingStateLoss();

        setContentView(R.layout.activity_test_fragment2);
        ButterKnife.bind(this);
        if (!isStart) {
            startService(TestService.getIntent(this, getClass().getSimpleName()));
            isStart = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            if (intent.hasExtra(Constants.KEY_MODEL)) {
                TestModel testModel = intent.getParcelableExtra(Constants.KEY_MODEL);
            }
            if (intent.hasExtra(Constants.KEY_BOOLEAN)) {
                boolean b = intent.getBooleanExtra(Constants.KEY_BOOLEAN, false);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isStart) {
            stopService(TestService.getIntent(this, getClass().getSimpleName()));
            isStart = false;
        }
    }
}
