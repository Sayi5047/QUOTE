package com.hustler.quote.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.hustler.quote.BuildConfig;
import com.hustler.quote.R;
import com.hustler.quote.ui.Executors.AppExecutor;
import com.hustler.quote.ui.ORM.AppDatabase;
import com.hustler.quote.ui.ORM.Tables.QuotesTable;
import com.hustler.quote.ui.ViewModels.SplashViewModel;
import com.hustler.quote.ui.activities.v1.OnBoardActivity;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.utils.TextUtils;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import java.util.ArrayList;


/**
 * Created by Sayi on 07-10-2017.
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
public class SplashActivity extends BaseActivity {
    private TextView tv_splash_name_new;
    private SharedPreferences sharedPreferences;
    private TextView mTvProgressUpdate2;
    private AppExecutor appExecutor;
    private AppDatabase appDatabase;
    private SplashViewModel splashViewModel;
    TextView tv;
    ImageView iv;
    TextView mTvProgressUpdate;
    ProgressBar progressBar;
    LinearLayout loading_layout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        handleBaseUi();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initialiseAds();
        initialiseEssentials();
        findViews();
        setViewAnimations(tv, iv, progressBar);
        setViewFonts(tv, mTvProgressUpdate);
        setupObservers();
        splashViewModel.getQuotesCount(appDatabase, appExecutor);

    }

    private void initialiseEssentials() {
        splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        appExecutor = AppExecutor.getInstance();
        appDatabase = AppDatabase.getmAppDatabaseInstance(SplashActivity.this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);

    }

    private void handleBaseUi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_rect));
            getWindow().setClipToOutline(true);
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.bg));
        }
    }

    private void setupObservers() {
        splashViewModel.loadingLayoutObserver.observe(this, shouldShowLoad ->
                loading_layout.setVisibility(shouldShowLoad ? View.VISIBLE : View.GONE));


        splashViewModel.isQuotesLoaded.observe(this, isLoaded -> {
            if (isLoaded) {
                setCounter_and_launch();
            } else {
                splashViewModel.loadQuotesToDataBase(appDatabase, appExecutor, load_from_Arrays());
            }
        });


        splashViewModel.launchNextActObserver.observe(this, canWeLaunch -> {
            if (canWeLaunch) setCounter_and_launch();
        });


        splashViewModel.quotesLoadingProgress.observe(this, progressMessage -> {

            if (BuildConfig.DEBUG) {
                Log.d("SPLASH_ACTIVITY", "Percentage Updated " + progressMessage);
            }
            mTvProgressUpdate2.setText(progressMessage);
        });


        splashViewModel.saveToSharedPrefs.observe(this, canWeSave -> {
            if (canWeSave) {
                sharedPreferences.edit()
                        .putBoolean(Constants.IS_QUOTES_LOADED_KEY, true)
                        .apply();
            }
        });
    }

    private void initialiseAds() {
        StartAppSDK.init(this, getString(R.string.ADS_ID_STARTAPPS), true);
        StartAppAd.disableSplash();
        if (true) {
            userAcceptedAds();
        } else {
            userRejectedAds();
        }
    }

    private void findViews() {
        tv = findViewById(R.id.tv_splash_name);
        loading_layout = findViewById(R.id.loading_layout);
        tv_splash_name_new = findViewById(R.id.tv_splash_name_new);
        iv = findViewById(R.id.iv_logo);
        progressBar = findViewById(R.id.mProgressBar);
        mTvProgressUpdate = findViewById(R.id.mTvProgressUpdate);
        mTvProgressUpdate2 = findViewById(R.id.mTvProgressUpdate2);
    }

    private void setViewFonts(TextView tv, TextView mTvProgressUpdate) {
        TextUtils.setFont(SplashActivity.this, tv, Constants.FONT_CIRCULAR);
        TextUtils.setFont(SplashActivity.this, mTvProgressUpdate, Constants.FONT_CIRCULAR);
        TextUtils.setFont(SplashActivity.this, mTvProgressUpdate2, Constants.FONT_CIRCULAR);
        TextUtils.setFont(SplashActivity.this, tv_splash_name_new, Constants.FONT_CIRCULAR);
    }

    private void setViewAnimations(TextView tv, ImageView iv, ProgressBar progressBar) {
        tv.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.slideup));
        iv.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.slideup));
        progressBar.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.slideup));
        iv.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.slideup));
        tv_splash_name_new.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.slideup));
    }

    private void userAcceptedAds() {
        StartAppSDK.setUserConsent(this,
                "pas",
                System.currentTimeMillis(),
                true);

    }

    private void userRejectedAds() {
        StartAppSDK.setUserConsent(this,
                "pas",
                System.currentTimeMillis(),
                false);

    }


    private ArrayList<QuotesTable> load_from_Arrays() {
        String[] bodies = getResources().getStringArray(R.array.quote_bodies);
        String[] authors = getResources().getStringArray(R.array.quote_authors);
        String[] categories = getResources().getStringArray(R.array.quote_categories);
        final String[] languages = getResources().getStringArray(R.array.quote_languages);
        ArrayList<QuotesTable> quotesTableArrayList = new ArrayList<>();
        for (int i = 0; i < bodies.length; i++) {
            QuotesTable quote = new QuotesTable(bodies[i], authors[i], categories[i], languages[0], false);
            quotesTableArrayList.add(quote);
        }
        return quotesTableArrayList;

    }

    private void setCounter_and_launch() {
        CountDownTimer countDownTimer = new CountDownTimer(600, 600) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                goToSecondMain();
            }
        };
        countDownTimer.start();
    }

    private void goToSecondMain() {
        if (sharedPreferences.getBoolean(Constants.IS_USER_SAW_INRODUCTION, false)) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, OnBoardActivity.class));
        }

    }

}
