package io.hustler.qtzy.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;

import io.hustler.qtzy.R;
import io.hustler.qtzy.ui.Executors.AppExecutor;
import io.hustler.qtzy.ui.ORM.AppDatabase;
import io.hustler.qtzy.ui.ORM.Tables.QuotesTable;
import io.hustler.qtzy.ui.activities.v1.OnBoardActivity;
import io.hustler.qtzy.ui.apiRequestLauncher.Constants;
import io.hustler.qtzy.ui.utils.TextUtils;


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
    private final String TAG = this.getClass().getSimpleName();
    TextView tv;
    ImageView iv;
    SharedPreferences sharedPreferences;

    ProgressBar progressBar;
    TextView mTvProgressUpdate;
    TextView mTvProgressUpdate2;
    private AppExecutor appExecutor;
    private AppDatabase appDatabase;
    private String[] bodies;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_rect));
            getWindow().setClipToOutline(true);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv = findViewById(R.id.tv_splash_name);
        iv = findViewById(R.id.iv_logo);
        progressBar = findViewById(R.id.mProgressBar);
        mTvProgressUpdate = findViewById(R.id.mTvProgressUpdate);
        mTvProgressUpdate2 = findViewById(R.id.mTvProgressUpdate2);
        appExecutor = AppExecutor.getInstance();
        appDatabase = AppDatabase.getmAppDatabaseInstance(SplashActivity.this);
        tv.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.slideup));
        iv.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.slideup));
        progressBar.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.slideup));
        iv.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.slideup));
        TextUtils.setFont(SplashActivity.this, tv, Constants.FONT_CIRCULAR);
        TextUtils.setFont(SplashActivity.this, mTvProgressUpdate, Constants.FONT_CIRCULAR);
        TextUtils.setFont(SplashActivity.this, mTvProgressUpdate2, Constants.FONT_CIRCULAR);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);

        if (sharedPreferences.getBoolean(Constants.IS_QUOTES_LOADED_KEY, false)) {
            setCounter_and_launch();
            progressBar.setVisibility(View.GONE);
            mTvProgressUpdate.setVisibility(View.GONE);
            mTvProgressUpdate2.setVisibility(View.GONE);
        } else {
            loadQuotesToDatabase();
        }
//        TODO: 12-02-2019 check if google user already exists
//        PreferenceUtils preferenceUtils = new PreferenceUtils(SplashActivity.this);
//        if (preferenceUtils.isIS_USER_LOGGED_IN() && preferenceUtils.isIS_GOOGLE_LOGIN()) {
//        }
    }

    private ArrayList<QuotesTable> quotesTableArrayList;

    private void loadQuotesToDatabase() {
        appExecutor.getDiskExecutor().execute(new Runnable() {
            @Override
            public void run() {
                load_from_Arrays();
                int filledSize = 0;
                int loadedSize = quotesTableArrayList.size();
                for (QuotesTable quotesTable : quotesTableArrayList) {
                    appDatabase.quotesDao().insertUser(quotesTable);
                    filledSize++;
                    final String percentage = new DecimalFormat("0.00").format((filledSize * 100 / (double) loadedSize));
                    Log.d(TAG, "run: Completed Percentage " + percentage);
                    mTvProgressUpdate2.setText(MessageFormat.format("Completed {0}  %", percentage));

//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                        }
//                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(Constants.IS_QUOTES_LOADED_KEY, true);
                        editor.apply();
                        setCounter_and_launch();
                    }
                });


            }
        });

    }


    private void load_from_Arrays() {

        bodies = getResources().getStringArray(R.array.quote_bodies);
        String[] authors = getResources().getStringArray(R.array.quote_authors);
        String[] categories = getResources().getStringArray(R.array.quote_categories);
        final String[] languages = getResources().getStringArray(R.array.quote_languages);
        quotesTableArrayList = new ArrayList<>();
        for (int i = 0; i < bodies.length; i++) {
            QuotesTable quote = new QuotesTable(bodies[i], authors[i], categories[i], languages[0], false);
            quotesTableArrayList.add(quote);
        }


    }

    private void setCounter_and_launch() {
        CountDownTimer countDownTimer = new CountDownTimer(600, 600) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                gotoSecondmain();
            }
        };
        countDownTimer.start();
    }

    private void gotoSecondmain() {
        if (sharedPreferences.getBoolean(Constants.IS_USER_SAW_INRODUCTION, false)) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, OnBoardActivity.class));
        }

    }

}
