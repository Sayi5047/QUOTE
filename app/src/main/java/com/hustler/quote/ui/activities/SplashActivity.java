package com.hustler.quote.ui.activities;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.hustler.quote.R;
import com.hustler.quote.ui.superclasses.*;


/**
 * Created by Sayi on 07-10-2017.
 */

public class SplashActivity extends BaseActivity {
    TextView tv;
    ImageView iv;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv=(TextView) findViewById(R.id.tv_splash_name);
        iv=(ImageView) findViewById(R.id.iv_logo);
        tv.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this,R.anim.slideup));
        iv.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this,R.anim.slideup));
        CountDownTimer countDownTimer=new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
            }
        };
        countDownTimer.start();



    }
}
