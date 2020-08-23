package com.shubh.news.dagger;

import android.app.Application;

import com.shubh.news.MainActivity;
import com.shubh.news.dbase.ArticleDatabase;
import com.shubh.news.dbase.ArticleTableRepo;
import com.shubh.news.dbase.ArticlesDAO;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {AndroidInjectionModule.class, AppModule.class, RoomModule.class})
public interface AppComponent {

    void inject(MainActivity mainActivity);

    ArticlesDAO productDao();

    ArticleDatabase demoDatabase();

    ArticleTableRepo productRepository();

    Application application();

}