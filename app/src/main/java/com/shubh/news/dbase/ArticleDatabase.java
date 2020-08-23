package com.shubh.news.dbase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.shubh.news.models.Article_Table;

@Database(entities = {Article_Table.class}, version = 4, exportSchema = false)
public abstract class ArticleDatabase extends RoomDatabase {

    public abstract ArticlesDAO getArticlesDAO();
/*

    private static volatile ArticleDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
*/

/*    public static ArticleDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ArticleDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ArticleDatabase.class, "articles_database")
                            .addCallback(callback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }*/

//    private static RoomDatabase.Callback callback = new Callback() {
//        @Override
//        public void onOpen(@NonNull SupportSQLiteDatabase db) {
//            super.onOpen(db);
//        }
//    };
}
