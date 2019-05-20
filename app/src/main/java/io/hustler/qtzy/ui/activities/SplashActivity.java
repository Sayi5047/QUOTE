package io.hustler.qtzy.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import io.hustler.qtzy.R;
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
    TextView tv;
    ImageView iv;
    SharedPreferences sharedPreferences;

    ProgressDialog progressDialog;


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
        tv.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.slideup));
        iv.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.slideup));
        TextUtils.setFont(SplashActivity.this, tv, Constants.FONT_CIRCULAR);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);

        setCounter_and_launch();
//// TODO: 12-02-2019 check if google user already exists
//        PreferenceUtils preferenceUtils = new PreferenceUtils(SplashActivity.this);
//        if (preferenceUtils.isIS_USER_LOGGED_IN() && preferenceUtils.isIS_GOOGLE_LOGIN()) {
//        }
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

    private void gotoSecondmain() {
        if (sharedPreferences.getBoolean(Constants.IS_USER_SAW_INRODUCTION, false)) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, OnBoardActivity.class));
        }

    }

}
