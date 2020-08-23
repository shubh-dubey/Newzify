package com.shubh.news.dagger;

import androidx.lifecycle.LiveData;

import com.shubh.news.dbase.ArticleTableRepo;
import com.shubh.news.dbase.ArticlesDAO;
import com.shubh.news.models.Article_Table;

import java.util.List;

import javax.inject.Inject;

public class ArticleDataSource implements ArticleTableRepo {

    private final ArticlesDAO articlesDAO;

    @Inject
    public ArticleDataSource(ArticlesDAO articlesDAO) {
        this.articlesDAO = articlesDAO;
    }

    @Override
    public LiveData<List<Article_Table>> getAllArticles() {
        return articlesDAO.getArticlesSorted();
    }

    @Override
    public void insert(Article_Table articleTable) {
        articlesDAO.insert(articleTable);
    }

    @Override
    public void deleteAll() {
        articlesDAO.deleteAll();
    }
}
