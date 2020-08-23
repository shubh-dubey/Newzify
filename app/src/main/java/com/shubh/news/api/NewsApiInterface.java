package com.shubh.news.api;

import com.shubh.news.models.ResponseModel;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiInterface {

    @GET("top-headlines")
    Flowable<ResponseModel> getLatestNews2(@Query("country") String country, @Query("apiKey") String apiKey);
    //--------------------------The RxJava Call Section on Retrofit
}
