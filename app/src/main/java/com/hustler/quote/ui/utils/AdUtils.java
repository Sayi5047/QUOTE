package com.hustler.quote.ui.utils;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by Sayi on 07-01-2018.
 */

public class AdUtils {

    public static void loadBannerAd( AdView adView){
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("A5B1E467FD401973F9F69AD2CCC13C30").build();
        adView.loadAd(adRequest);
    }
}
