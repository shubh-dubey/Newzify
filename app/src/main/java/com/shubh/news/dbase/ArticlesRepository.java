package com.shubh.news.dbase;

public class ArticlesRepository {
    /*private ArticlesDAO articlesDAO;
    private LiveData<List<Article_Table>> allArticles;

    public ArticlesRepository(Application application) {
        ArticleDatabase articleDatabase = ArticleDatabase.getDatabase(application);
        articlesDAO = articleDatabase.getArticlesDAO();
        allArticles = articlesDAO.getArticlesSorted();
    }*/

    /*//LIVEDATA
    public LiveData<List<Article_Table>> getAllArticles() {
        return allArticles;
    }

    public void insert(Article_Table articleTable) {
        ArticleDatabase.databaseWriteExecutor.execute(() ->
        {
            articlesDAO.insert(articleTable);
        });
    }

    public void deleteAll() {
        ArticleDatabase.databaseWriteExecutor.execute(() -> {
            articlesDAO.deleteAll();
        });
    }*/
}
