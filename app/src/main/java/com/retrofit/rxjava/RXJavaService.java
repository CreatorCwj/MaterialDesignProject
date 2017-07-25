package com.retrofit.rxjava;

import com.retrofit.TestApiModel;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by cwj on 17/5/22.
 */

public interface RXJavaService {

    @GET("getSsoid")
    Observable<SsoidModel> getSsoid();

    @GET("test")
    Observable<TestApiModel> getTestData(@Query("ssoid") String ssoid);
}
