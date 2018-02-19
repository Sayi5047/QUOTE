package com.hustler.quote.ui.utils;

import android.app.Activity;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.hustler.quote.R;

/**
 * Created by Sayi on 07-01-2018.
 */

public class AdUtils {

    public static void loadBannerAd(final AdView adView, final Activity activity) {
        adView.setVisibility(View.GONE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                AdRequest adRequest = new AdRequest.Builder().build();
                adView.loadAd(adRequest);
                adView.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        adView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
//                        Toast_Snack_Dialog_Utils.show_ShortToast(activity,"AD CLICKED");
                    }

                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
//                        Toast_Snack_Dialog_Utils.show_ShortToast(activity,"AD CLOSED");
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
//                        Toast_Snack_Dialog_Utils.show_ShortToast(activity,"Ad Failed TO Load");
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
//                        Toast_Snack_Dialog_Utils.show_ShortToast(activity,"Ad IMPRESSION");

                    }

                });
            }
        }).run();

    }

    public static void loadRewardAd(final RewardedVideoAd adView, final Activity activity) {

        adView.loadAd(activity.getString(R.string.rewardAdId), new AdRequest.Builder().build());

    }
}
