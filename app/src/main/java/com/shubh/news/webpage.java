package com.shubh.news;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class webpage extends AppCompatActivity {


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        WebView mWebview = findViewById(R.id.webview);
        if (getIntent() != null) {
            String url = getIntent().getStringExtra("url");
            String title = getIntent().getStringExtra("title");
            if (title != null && supportActionBar != null) {
                supportActionBar.setTitle(title);
            }
            if (url != null) {
                final Activity activity = this;
                mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript
                mWebview.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @TargetApi(android.os.Build.VERSION_CODES.M)
                    @Override
                    public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                        // Redirect to deprecated method, so you can use it in all SDK versions
                        onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
                        finish();
                    }
                });
                mWebview.loadUrl(url);
                // An extremely Dumb Bug, removed setcontentview as it set content on top of already set content
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
