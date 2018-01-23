package com.hustler.quote.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.hustler.quote.R;
import com.hustler.quote.ui.QuoteStrings.QuoteCategories;
import com.hustler.quote.ui.QuoteStrings.QuotesAuthorsClass;
import com.hustler.quote.ui.QuoteStrings.QuotesClass;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.database.QuotesDbHelper;
import com.hustler.quote.ui.pojo.Quote;
import com.hustler.quote.ui.superclasses.App;

import java.util.ArrayList;


/**
 * Created by Sayi on 07-10-2017.
 */

public class SplashActivity extends BaseActivity {
    TextView tv;
    ImageView iv;
    Quote[] quotesFromdb;
    SharedPreferences sharedPreferences;
    String[] bodies;
    String[] authors;
    String[] categories;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv = (TextView) findViewById(R.id.tv_splash_name);
        iv = (ImageView) findViewById(R.id.iv_logo);

        tv.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.slideup));
        iv.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.slideup));
        tv.setTypeface(App.applyFont(this, Constants.FONT_multicolore));

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
        if (sharedPreferences.getBoolean(Constants.IS_DB_LOADED_PREFERENCE, false)) {
            setCounter_and_launch();
        } else {
            load_from_Arrays();
            load_to_database();
        }

    }

    private void load_to_database() {
        ArrayList<Quote> quotes = new ArrayList<>();
        for (int i = 0; i < quotesFromdb.length; i++) {
            quotes.add(i, quotesFromdb[i]);
        }
        if (quotes != null) {
            for (int i = 0; i < quotes.size(); i++) {
                new QuotesDbHelper(SplashActivity.this).addQuote(quotes.get(i));
            }
            Log.d("Quotes list length", quotes.size() + "");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Constants.IS_DB_LOADED_PREFERENCE, true);
            editor.apply();
        }
        setCounter_and_launch();
    }

    private void load_from_Arrays() {

        bodies = QuotesClass.AGE_QUOTES.split("\n");
        authors = QuotesAuthorsClass.AGE_AUTHORS.split("\n");
        categories = QuoteCategories.QUOTE_CATEGORY_AGE.split("\n");
        for (String body : bodies) {
            Log.d("VALUES", body);

        }
        for (String body : authors) {
            Log.d("authors", body);

        }
        for (String body : categories) {
            Log.d("categories", body);

        }



        Log.d("BODIES LENGTH",String.valueOf(bodies.length));
        Log.d("AUTHORS LENGTH",String.valueOf(authors.length));
        Log.d("Categories LENGTH",String.valueOf(categories.length));

//        final String[] bodies = getResources().getStringArray(R.array.quote_bodies);
//        final String[] authors = getResources().getStringArray(R.array.quote_authors);
//        final String[] categories = getResources().getStringArray(R.array.quote_categories);
        final String[] languages = getResources().getStringArray(R.array.quote_languages);
        quotesFromdb=new Quote[bodies.length];
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < bodies.length; i++) {
                    Log.d("ADDED ELEMNT",String.valueOf(i));
                    Quote quote = new Quote(bodies[i], authors[i], categories[i], languages[0]);
                    quotesFromdb[i]=quote;
                }
            }
        }).run();

    }

    private void convertQuotes() {

    }

    private void gotoSecondmain() {
        if (sharedPreferences.getBoolean(Constants.IS_USER_SAW_INRODUCTION, false)) {
            startActivity(new Intent(SplashActivity.this, HomeActivity.class));

        } else {
            startActivity(new Intent(SplashActivity.this, LanderActivty.class));

        }

    }

    private void setCounter_and_launch() {
        CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    gotoFirstMain();
//                }
                gotoSecondmain();
            }
        };
        countDownTimer.start();
    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private void gotoFirstMain() {
//        getWindow().setEnterTransition(new Explode());
//        Intent intent=new Intent(SplashActivity.this,MainActivity.class);
//        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this).toBundle());
//    }
}
