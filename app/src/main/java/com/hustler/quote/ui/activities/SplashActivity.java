package com.hustler.quote.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.hustler.quote.R;
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
    ProgressDialog progressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv = findViewById(R.id.tv_splash_name);
        iv = findViewById(R.id.iv_logo);

        tv.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.slideup));
        iv.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.slideup));
        tv.setTypeface(App.applyFont(this, Constants.FONT_multicolore));
        progressDialog = new ProgressDialog(SplashActivity.this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().setStatusBarColor(ContextCompat.getColor(SplashActivity.this, android.R.color.white));
        }
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
        if (sharedPreferences.getBoolean(Constants.IS_DB_LOADED_PREFERENCE, false)) {
            setCounter_and_launch();
        } else {
            new QuoteLoadTask().execute();
        }

    }

    private void load_to_database() {
        if (progressDialog != null) {
            progressDialog.setProgress(55);
        }
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
        if (progressDialog != null) {
            progressDialog.setProgress(95);
        }
//        setCounter_and_launch();
    }

    private void load_from_Arrays() {
        if (progressDialog != null) {
            progressDialog.setProgress(25);
        }

        bodies = getResources().getStringArray(R.array.quote_bodies);
        authors = getResources().getStringArray(R.array.quote_authors);
        categories = getResources().getStringArray(R.array.quote_categories);

        Log.d("VALUES", String.valueOf(bodies.length));
        Log.d("VALUES", String.valueOf(authors.length));
        Log.d("VALUES", String.valueOf(categories.length));

        Log.d("BODIES LENGTH", String.valueOf(bodies.length));
        Log.d("AUTHORS LENGTH", String.valueOf(authors.length));
        Log.d("Categories LENGTH", String.valueOf(categories.length));


        final String[] languages = getResources().getStringArray(R.array.quote_languages);
        quotesFromdb = new Quote[bodies.length];
        for (int i = 0; i < bodies.length; i++) {
            Log.d("ADDED ELEMNT", String.valueOf(i));
            Quote quote = new Quote(bodies[i], authors[i], categories[i], languages[0]);
            quotesFromdb[i] = quote;
        }

    }


    private void gotoSecondmain() {
        if (sharedPreferences.getBoolean(Constants.IS_USER_SAW_INRODUCTION, false)) {
            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, LanderActivty.class));
        }

    }

    private void setCounter_and_launch() {
        CountDownTimer countDownTimer = new CountDownTimer(800, 800) {
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

    public class QuoteLoadTask extends AsyncTask<String, String, Void> {


        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected void onPreExecute() {
            progressDialog.setTitle(getString(R.string.First_setup));
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.setProgress(0);
            progressDialog.create();
            progressDialog.show();
        }


        @Override
        protected Void doInBackground(String... strings) {
            load_from_Arrays();
            load_to_database();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.setProgress(100);
            progressDialog.dismiss();
            setCounter_and_launch();
        }
    }
}
