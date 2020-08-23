package com.shubh.news.dbase;

import androidx.lifecycle.LiveData;

import com.shubh.news.models.Article_Table;

import java.util.List;

public interface ArticleTableRepo {
    LiveData<List<Article_Table>> getAllArticles();

    void insert(Article_Table articleTable);

    void deleteAll();

}
