package com.shubh.news.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.inject.Singleton;

@Singleton
public class ResponseModel {
    @SerializedName("status")
    private String status;
    @SerializedName("totalResults")
    private int totalResults;
    @SerializedName("articles")
    private List<OnlineArticleModel> onlineArticleModels = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<OnlineArticleModel> getOnlineArticleModels() {
        return onlineArticleModels;
    }

    public void setOnlineArticleModels(List<OnlineArticleModel> onlineArticleModels) {
        this.onlineArticleModels = onlineArticleModels;
    }
}