package com.retrofit.rxjava;

import com.retrofit.TestApiModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by cwj on 17/5/22.
 */

public interface RXJavaService {

    @GET("{relateUrl}")
    Call<Object> getDynamic(@Path("relateUrl") String relateUrl);

    @GET("getSsoid")
    Observable<SsoidModel> getSsoid();

    @GET("test")
    Observable<TestApiModel> getTestData(@Query("ssoid") String ssoid);

    @GET
    Call<Object> testCwj(@Url String url);

    @GET
    Call<TestCwjModel> testCwj2(@Url String url);

    @GET
    Call<Map<String, Double>> testCwj3(@Url String url);
}
