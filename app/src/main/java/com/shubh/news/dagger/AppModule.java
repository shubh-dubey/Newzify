package com.shubh.news.dagger;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    @Provides
    static String API_KEY() {
        return "7876eea480474b59b25e82866d2f6374";
    }
/*
    @Provides
    static Drawable logo(Application application) {
        return ContextCompat.getDrawable(application, android.R.drawable.presence_away);
    }*/

    final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return application;
    }

}
