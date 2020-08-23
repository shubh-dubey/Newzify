package com.shubh.news;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.shubh.news.adapters.ArticleListAdapter;
import com.shubh.news.api.ApiClient;
import com.shubh.news.api.NewsApiInterface;
import com.shubh.news.dagger.AppModule;
import com.shubh.news.dagger.DaggerAppComponent;
import com.shubh.news.dagger.RoomModule;
import com.shubh.news.dbase.ArticleTableRepo;
import com.shubh.news.models.Article_Table;
import com.shubh.news.models.OnlineArticleModel;
import com.shubh.news.models.ResponseModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    Button US, IN;
    public static boolean showAD = true;

    @Inject
    String getApiKey;
    //----------------------------using dagger to get api key------------------------------------
    /*
    @Inject
    Drawable drawable;
    */
    public static final String TAG = "news_app_log";

    @Inject
    public ArticleTableRepo articleTableRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: key " + TAG + " ");


        DaggerAppComponent.builder()
                .appModule(new AppModule(getApplication()))
                .roomModule(new RoomModule(getApplication()))
                .build()
                .inject(this);

        articleTableRepo.getAllArticles().observe(this, new Observer<List<Article_Table>>() {
            @Override
            public void onChanged(List<Article_Table> article_tables) {
                for (Article_Table a :
                        article_tables) {
                    Log.d(TAG, "onChanged: " + a.getId() + " " + a.getTitle());
                }
            }
        });

        RecyclerView recyclerView = findViewById(R.id.rv);
        US = findViewById(R.id.usa);
        IN = findViewById(R.id.india);
        final ArticleListAdapter adapter = new ArticleListAdapter(this, null);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(60)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
        articleTableRepo.getAllArticles().observe(this, new Observer<List<Article_Table>>() {
            @Override
            public void onChanged(List<Article_Table> articles) {
                mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(MainActivity.this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();
                        }
                        Log.d(TAG, "onComplete: " + mFirebaseRemoteConfig.getBoolean("showAD"));
                        showAD = mFirebaseRemoteConfig.getBoolean("showAD");
                        adapter.setArticles(articles, MainActivity.this);
                    }
                });
            }
        });
        final NewsApiInterface apiInterface = ApiClient.getClient().create(NewsApiInterface.class);
        SharedPreferences preferences = getSharedPreferences("data", 0);
        String string = preferences.getString("country", "in");
        final String[] Country = {string};
        String locale = getResources().getConfiguration().locale.getCountry();
        //-------------------The Location Section-----------------------
        if (locale.equals("IN") || locale.equals("US")) {
            Country[0] = locale;
        }
        callAPI(apiInterface, Country[0]);
        US.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                articleTableRepo.deleteAll();
                Country[0] = "US";
                preferences.edit().putString("country", Country[0]).apply();
                callAPI(apiInterface, Country[0]);
            }
        });

        IN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                articleTableRepo.deleteAll();
                Country[0] = "IN";
                preferences.edit().putString("country", Country[0]).apply();
                callAPI(apiInterface, Country[0]);
            }
        });
        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    boolean updated = task.getResult();
                }
                Log.d(TAG, "onComplete: " + mFirebaseRemoteConfig.getBoolean("showAD"));
                showAD = mFirebaseRemoteConfig.getBoolean("showAD");
                callAPI(apiInterface, Country[0]);
            }
        });
    }

    private void callAPI(NewsApiInterface apiInterface, String country) {
        //RXJAVA
        if (getApiKey != null) {
            apiInterface.getLatestNews2(country, getApiKey)
                    .toObservable()
                    .subscribeOn(Schedulers.io())
                    .subscribe(new io.reactivex.Observer<ResponseModel>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ResponseModel responseModel) {
                            updateArticles(responseModel.getOnlineArticleModels());
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    private void updateArticles(List<OnlineArticleModel> a) {
        for (OnlineArticleModel b : a) {
            Article_Table word = new Article_Table(b.getTitle().hashCode(), b.getTitle(), b.getPublishedAt(), b.getUrlToImage(), b.getUrl());
            articleTableRepo.insert(word);
        }
    }

}