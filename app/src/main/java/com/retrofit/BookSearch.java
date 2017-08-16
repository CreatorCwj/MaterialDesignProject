package com.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by cwj on 17/5/3.
 */

public interface BookSearch {

    @GET("book/search")
    Call<BookSearchModel> getSearchBooks(@Query("q") String name, @Query("tag") String tag
            , @Query("start") int start, @Query("count") int count);
}
