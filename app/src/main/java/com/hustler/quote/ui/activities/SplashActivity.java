package com.hustler.quote.ui.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.transition.Explode;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.hustler.quote.R;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.database.QuotesDbHelper;
import com.hustler.quote.ui.pojo.Quote;
import com.hustler.quote.ui.superclasses.*;

import java.util.ArrayList;


/**
 * Created by Sayi on 07-10-2017.
 */

public class SplashActivity extends BaseActivity {
    TextView tv;
    ImageView iv;
    ArrayList<Quote> quotesFromdb=new ArrayList<>();
    SharedPreferences sharedPreferences;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv=(TextView) findViewById(R.id.tv_splash_name);
        iv=(ImageView) findViewById(R.id.iv_logo);
        tv.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this,R.anim.slideup));
        iv.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this,R.anim.slideup));
        tv.setTypeface(App.getZingCursive(this, Constants.FONT_multicolore));
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
        if (sharedPreferences.getBoolean(Constants.IS_DB_LOADED_PREFERENCE, false)) {
            gotoSecondmain();
        } else {
            load_from_Arrays();
            load_to_database();
        }
        CountDownTimer countDownTimer=new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    gotoFirstMain();
//                }
//                gotoSecondmain();
            }
        };
        countDownTimer.start();
    }
    private void load_to_database() {
        if (quotesFromdb != null) {
            for (int i = 0; i < quotesFromdb.size(); i++) {
                new QuotesDbHelper(SplashActivity.this).addQuote(quotesFromdb.get(i));
            }
            Log.d("Quotes list length",quotesFromdb.size()+"");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Constants.IS_DB_LOADED_PREFERENCE, true);
            editor.apply();
        }
        gotoSecondmain();
    }

    private void load_from_Arrays() {
        String[] bodies = getResources().getStringArray(R.array.quote_bodies);
        String[] authors = getResources().getStringArray(R.array.quote_authors);
        String[] categories = getResources().getStringArray(R.array.quote_categories);
        String[] languages = getResources().getStringArray(R.array.quote_languages);
        for (int i = 0; i < bodies.length; i++) {
            Quote quote = new Quote(bodies[i], authors[i], categories[i], languages[i]);
            quotesFromdb.add(i, quote);
        }
    }

    private void gotoSecondmain() {
        startActivity(new Intent(SplashActivity.this,HomeActivity.class));

    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private void gotoFirstMain() {
//        getWindow().setEnterTransition(new Explode());
//        Intent intent=new Intent(SplashActivity.this,MainActivity.class);
//        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this).toBundle());
//    }
}
