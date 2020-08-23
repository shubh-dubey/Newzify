package com.shubh.news.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.shubh.news.R;
import com.shubh.news.models.Article_Table;
import com.shubh.news.webpage;

import java.util.List;

import static com.google.android.gms.ads.formats.NativeAdOptions.NATIVE_MEDIA_ASPECT_RATIO_PORTRAIT;
import static com.shubh.news.MainActivity.TAG;
import static com.shubh.news.MainActivity.showAD;


public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ArticleViewHolder> {

    private final Drawable drawable;
    private Activity activity;
    private UnifiedNativeAd nativeAd;


    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        private final TextView date_tv, title_tv;
        private final ImageView articleIMV;
        private final LinearLayout web;
        private final FrameLayout frameLayout;

        private ArticleViewHolder(View itemView) {
            super(itemView);
            date_tv = itemView.findViewById(R.id.date_tv);
            title_tv = itemView.findViewById(R.id.title_tv);
            articleIMV = itemView.findViewById(R.id.imageView);
            web = itemView.findViewById(R.id.webOPEN);
            frameLayout = itemView.findViewById(R.id.adholder);
        }
    }

    private final LayoutInflater mInflater;
    private List<Article_Table> article; // Cached copy of words

    public ArticleListAdapter(Context context, Drawable drawable) {
        mInflater = LayoutInflater.from(context);
        this.drawable = drawable;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ArticleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        if (article != null) {
            Article_Table current = article.get(position);
            String[] split = current.getDate().replaceAll("[a-zA-Z]", " ").split(" ");
            holder.date_tv.setText(String.format("Date - %s at %s", split[0], split[1]));
            //holder.date_tv.append(String.format("\nURL - %s", current.getUrl()));
            holder.title_tv.setText(current.getTitle());
            Log.d(TAG, "onBindViewHolder: " + drawable);

            //GLIDE USED
            Glide.with(activity).load(current.getImageUrl()).into(holder.articleIMV);
            holder.web.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(activity, webpage.class);
                        intent.putExtra("url", current.getUrl());
                        intent.putExtra("title", current.getTitle());
                        activity.startActivity(intent);
                    } catch (Exception e) {
                        Log.e(TAG, "onClick: ", e);
                        FirebaseCrashlytics.getInstance().log("On click " + e.getLocalizedMessage());
                        Toast.makeText(activity, "No Url", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            if (showAD && (position + 1) % 2 == 0) {
                holder.frameLayout.setVisibility(View.VISIBLE);
                //Native Ads
                refreshAd(holder.frameLayout);
            } else {
                holder.frameLayout.setVisibility(View.GONE);
            }
        }
    }

    private void refreshAd(FrameLayout frameLayout) {

        AdLoader.Builder builder = new AdLoader.Builder(activity, activity.getString(R.string.nativeId));
        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            // OnUnifiedNativeAdLoadedListener implementation.

            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                // You must call destroy on old ads when you are done with them,
                // otherwise you will have a memory leak.
                if (nativeAd != null) {
                    nativeAd.destroy();
                }
                nativeAd = unifiedNativeAd;
                @SuppressLint("InflateParams") UnifiedNativeAdView adView = (UnifiedNativeAdView) activity.getLayoutInflater()
                        .inflate(R.layout.nativeadlayout, null);
                populateUnifiedNativeAdView(unifiedNativeAd, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }

        });


        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setMediaAspectRatio(NATIVE_MEDIA_ASPECT_RATIO_PORTRAIT)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }


    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Set the media view.
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.

        // Updates the UI to say whether or not this ad has a video asset.
    }

    public void setArticles(List<Article_Table> articles, Activity activity) {
        article = articles;
        this.activity = activity;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (article != null)
            return article.size();
        else return 0;
    }
}