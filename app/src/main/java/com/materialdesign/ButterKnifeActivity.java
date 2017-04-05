package com.materialdesign;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.retrofit.BookModel;
import com.retrofit.BookSearch;
import com.retrofit.BookSearchModel;
import com.retrofit.TestApi;
import com.retrofit.TestApiModel;
import com.test_fragment.BaseActivity;
import com.utils.Utils;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ButterKnifeActivity extends BaseActivity {

    private static final String MODEL_A7 = "SM-A7000";

    @Nullable
    @BindView(R.id.custom_btn)
    Button customBtn;

    @BindString(R.string.butter_knife_text)
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_butter_knife);
        ButterKnife.bind(this);
        customBtn.setText(text);
    }

    private void addFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new ButterKnifeFragment())
                .commitAllowingStateLoss();
    }

    @Optional
    @OnClick(R.id.custom_btn)
    public void onClick(Button btn) {
//        testApi();
//        test();
//        addFragment();

        //https://api.douban.com/v2/book/search?q=%E5%B0%8F%E7%8E%8B%E5%AD%90&start=0&count=3
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.douban.com/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        BookSearch bookSearch = retrofit.create(BookSearch.class);
        Call<BookSearchModel> call = bookSearch.getSearchBooks("小王子", null, 0, 3);

        call.enqueue(new Callback<BookSearchModel>() {
            @Override
            public void onResponse(Call<BookSearchModel> call, Response<BookSearchModel> response) {
                String msg;
                List<BookModel> books = response.body().books;
                if (books != null && books.size() > 0 && books.get(0) != null && books.get(0).author != null && books.get(0).author.length > 0) {
                    msg = books.get(0).author[0];
                } else {
                    msg = "no data";
                }
                Utils.showToast(getApplicationContext(), msg);
            }

            @Override
            public void onFailure(Call<BookSearchModel> call, Throwable t) {
                Utils.showToast(getApplicationContext(), t.getMessage());
            }
        });
//        call.cancel();
    }

    private void testApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://mock.sankuai.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        TestApi testApi = retrofit.create(TestApi.class);
        Call<TestApiModel> call = testApi.getResult();

        call.enqueue(new Callback<TestApiModel>() {
            @Override
            public void onResponse(Call<TestApiModel> call, Response<TestApiModel> response) {
                TestApiModel model = response.body();
                if (model != null) {
                    Utils.showToast(getApplicationContext(), model.toString());
                } else {
                    Utils.showToast(getApplicationContext(), "model is null");
                }
            }

            @Override
            public void onFailure(Call<TestApiModel> call, Throwable t) {
                Utils.showToast(getApplicationContext(), t.getMessage());
            }
        });
    }

    private void test() {
        String model = Build.MODEL;
        if (model.contains(MODEL_A7)) {
            Utils.showToast(this, "isA7   " + model);
        } else {
            Utils.showToast(this, model);
        }
    }

}
