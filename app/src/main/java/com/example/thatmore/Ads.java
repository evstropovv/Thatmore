package com.example.thatmore;

import android.app.Activity;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdListener;

/**
 * Created by в on 19.12.2016.
 */

public class Ads {
    public static void showBanner (final Activity activity){
        final AdView banner = (AdView) activity.findViewById(R.id.banner);
        AdRequest adRequest = new AdRequest.Builder().build();

        banner.loadAd(adRequest);
        banner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                setupContentViewPadding(activity, banner.getHeight());
            }
        });
    }

    public static void setupContentViewPadding(Activity activity, int padding){
        View view = activity.findViewById(R.id.coordinator);
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), padding);
    }
}
