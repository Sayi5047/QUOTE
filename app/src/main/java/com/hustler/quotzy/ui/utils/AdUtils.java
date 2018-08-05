package com.hustler.quotzy.ui.utils;

import android.app.Activity;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.hustler.quotzy.R;

/**
 * Created by Sayi on 07-01-2018.
 */
/*   Copyright [2018] [Sayi Manoj Sugavasi]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.*/
public class AdUtils {

    public static void loadBannerAd(final AdView adView, final Activity activity) {
        adView.setVisibility(View.GONE);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice("A5B1E467FD401973F9F69AD2CCC13C30").build();
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
//                Toast_Snack_Dialog_Utils.show_ShortToast(activity, "AD CLICKED");
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
//                Toast_Snack_Dialog_Utils.show_ShortToast(activity, "AD CLOSED");
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
//                Toast_Snack_Dialog_Utils.show_ShortToast(activity, "Ad Failed TO Load");
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
//                Toast_Snack_Dialog_Utils.show_ShortToast(activity, "Ad IMPRESSION");

            }

        });
        adView.loadAd(adRequest);

    }


    public static void loadRewardAd(final RewardedVideoAd adView, final Activity activity) {

        adView.loadAd(activity.getString(R.string.rewardAdId), new AdRequest.Builder().build());

    }
}
