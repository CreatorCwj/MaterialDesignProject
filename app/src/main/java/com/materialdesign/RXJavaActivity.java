package com.materialdesign;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.adapter.TestSelectAdapter;
import com.retrofit.TestApiModel;
import com.retrofit.rxjava.RXJavaService;
import com.retrofit.rxjava.SsoidModel;
import com.test_fragment.BaseActivity;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by cwj on 17/5/16.
 */

public class RXJavaActivity extends BaseActivity {

    private static final String BASE_URL = "http://mock.sankuai.com/moma/";

    @BindView(R.id.tv1)
    TextView tv1;

    @BindView(R.id.lv)
    ListView lv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);
        ButterKnife.bind(this);
        lv.setAdapter(new TestSelectAdapter(getData()));

        String url = "http://develop.api.ms.test.sankuai.info/api/moma/user/get?tenantId=1&id=6019";
        Uri uri = Uri.parse(url);
        String schema = uri.getScheme();
        String host = uri.getHost();
        String path = uri.getPath();
        String query = uri.getQuery();
    }

    private List<String> getData() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            data.add("item" + i);
        }
        return data;
    }

    @OnClick(R.id.btn1)
    void onClick(View view) {
//        serial();
        parallel();
        tv1.setSelected(!tv1.isSelected());
    }

    private void parallel() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createAsync())
                .build();

        final RXJavaService service = retrofit.create(RXJavaService.class);

        Observable.from(new Integer[]{1, 2, 3, 4, 5})
                .flatMap(new Func1<Integer, Observable<SsoidModel>>() {
                    @Override
                    public Observable<SsoidModel> call(Integer integer) {
                        return service.getSsoid();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SsoidModel>() {
                    @Override
                    public void onCompleted() {
                        Utils.showToast(getApplicationContext(), "onSuccess");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.showToast(getApplicationContext(), "onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(SsoidModel ssoidModel) {
                        Utils.showToast(getApplicationContext(), "onNext:" + ssoidModel.data);
                    }
                });
    }

    private void serial() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createAsync())
                .build();

        final RXJavaService service = retrofit.create(RXJavaService.class);

        service.getSsoid()
                .flatMap(new Func1<SsoidModel, Observable<TestApiModel>>() {
                    @Override
                    public Observable<TestApiModel> call(SsoidModel ssoidModel) {
                        return service.getTestData(ssoidModel.data);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TestApiModel>() {
                    @Override
                    public void onCompleted() {
                        Utils.showToast(getApplicationContext(), "success!!!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        tv1.setText("error:" + e.toString());
                    }

                    @Override
                    public void onNext(TestApiModel testApiModel) {
                        tv1.setText("result:" + testApiModel.toString());
                    }
                });
    }

}
