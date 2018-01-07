package com.hustler.quote.ui.superclasses;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.hustler.quote.R;
import com.hustler.quote.ui.apiRequestLauncher.Constants;

/**
 * Created by Sayi on 07-10-2017.
 */

public class BaseActivity extends AppCompatActivity {
    public Activity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        activity = this;
        MobileAds.initialize(activity, Constants.ADS_APP_ID);

    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public int getMatColor(String typeColor) {
        int returnColor = Color.BLACK;
        int arrayId = getResources().getIdentifier("mdcolor_" + typeColor, "array", getApplicationContext().getPackageName());

        if (arrayId != 0) {
            TypedArray colors = getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.BLACK);
            colors.recycle();
        }
        return returnColor;
    }

    public void setToolbar(Activity activity) {
        android.support.v7.widget.Toolbar toolbar1 = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar1);
//        toolbar1.setAnimation(AnimationUtils.loadAnimation(activity,R.anim.slideup));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setExplodeAnimation() {
        Explode explode = new Explode();
        explode.setDuration(500);
        getWindow().setEnterTransition(explode);
    }
}
