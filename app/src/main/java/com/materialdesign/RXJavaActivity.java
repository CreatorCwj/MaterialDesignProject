package com.materialdesign;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.adapter.TestSelectAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.retrofit.TestApiModel;
import com.retrofit.rxjava.RXJavaService;
import com.retrofit.rxjava.SsoidModel;
import com.retrofit.rxjava.TestCwjModel;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.test_fragment.BaseActivity;
import com.utils.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
        serial();
//        parallel();
//        testDynamic();
        tv1.setSelected(!tv1.isSelected());
    }

    private void testDynamic() {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.createAsync())
//                .build();
//
//        retrofit.create(RXJavaService.class)
//                .getDynamic("/getSsoid")
//                .enqueue(new Callback<Object>() {
//                    @Override
//                    public void onResponse(Call<Object> call, Response<Object> response) {
//                        Object obj = response.body();
//                        String json = new Gson().toJson(obj);
//                        Utils.showToast(getApplicationContext(), json);
//                    }
//
//                    @Override
//                    public void onFailure(Call<Object> call, Throwable t) {
//                        Utils.showToast(getApplicationContext(), t.getMessage());
//                    }
//                });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createAsync())
                .build();

        retrofit.create(RXJavaService.class)
                .testCwj("http://mock.sankuai.com/moma/cwjtest")
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Object res = response.body();
                        String json = getGson().toJson(res);
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });

        retrofit.create(RXJavaService.class)
                .testCwj2("http://mock.sankuai.com/moma/cwjtest")
                .enqueue(new Callback<TestCwjModel>() {
                    @Override
                    public void onResponse(Call<TestCwjModel> call, Response<TestCwjModel> response) {
                        TestCwjModel model = response.body();
                        String json = new Gson().toJson(model);
                    }

                    @Override
                    public void onFailure(Call<TestCwjModel> call, Throwable t) {

                    }
                });

        retrofit.create(RXJavaService.class)
                .testCwj3("http://mock.sankuai.com/moma/cwjtest")
                .enqueue(new Callback<Map<String, Double>>() {
                    @Override
                    public void onResponse(Call<Map<String, Double>> call, Response<Map<String, Double>> response) {
                        Map<String, Double> model = response.body();
                        String json = new Gson().toJson(model);
                        StringBuilder sb = new StringBuilder("");
                        for (Map.Entry<String, Double> entry : model.entrySet()) {
                            sb.append(entry.getValue()).append("  ");
                        }
                        Utils.showToast(getApplicationContext(), sb.toString());
                    }

                    @Override
                    public void onFailure(Call<Map<String, Double>> call, Throwable t) {

                    }
                });
    }

    private Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
                    @Override
                    public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                        if (src == src.longValue()) {
                            return new JsonPrimitive(src.longValue());
                        }
                        return new JsonPrimitive(src);
                    }
                }).create();
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
                        //hihihi
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

    private void test() {
//        Picasso.with(this)
//                .load(PHOTO_URL)
//                .placeholder(R.drawable.placeHolder)
//                .error(R.drawable.error)
//                .memoryPolicy(MemoryPolicy.NO_STORE)
//                .networkPolicy(NetworkPolicy.NO_STORE)
//                .centerCrop()
//                .fit()
//                .noFade()
//                .into(imageView);

        //test revert1

        //test revert2
    }

}
