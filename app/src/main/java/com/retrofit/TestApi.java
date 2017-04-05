package com.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by cwj on 17/5/4.
 */

public interface TestApi {

    @GET("moma/test")
    Call<TestApiModel> getResult();
}
