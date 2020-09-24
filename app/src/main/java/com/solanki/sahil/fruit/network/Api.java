package com.solanki.sahil.fruit.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface Api {
    @GET("search/repositories")
    Call<ResponseBody> searchRepo(@Query("q") String query);

    @GET()
    Call<ResponseBody> searchCommits(@Url String url);

}
