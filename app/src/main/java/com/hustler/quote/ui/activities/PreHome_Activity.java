package com.hustler.quote.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hustler.quote.R;
import com.hustler.quote.ui.adapters.MainFeatureADapter;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.utils.Toast_Snack_Dialog_Utils;

/**
 * Created by Sayi on 22-04-2018.
 */
@Deprecated
public class PreHome_Activity extends BaseActivity {
    RecyclerView recyclerView;
    MainFeatureADapter mainFeatureADapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_rect));
            getWindow().setClipToOutline(true);
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
        }
        setContentView(R.layout.pre_home_layout);
        recyclerView = findViewById(R.id.features_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mainFeatureADapter = new MainFeatureADapter(PreHome_Activity.this, new MainFeatureADapter.FeatureClick_Listener() {
            @Override
            public void onFeatureClciked(int position, String name) {
                Toast_Snack_Dialog_Utils.show_ShortToast(PreHome_Activity.this, position + name);
                switch (position) {
                    case 0:
                        callHome(0);
                        break;
                    case 1:
                        callHome(1);

                        break;
                    case 2:
                        callHome(2);

                        break;
                    case 3:
                        callHome(3);

                        break;
                    case 4:
                        callHome(4);

                        break;
                    case 5:
                        callHome(5);

                        break;
                }
            }
        });
        recyclerView.setAdapter(mainFeatureADapter);
    }

    private void callHome(int i) {
        Intent intent = new Intent(PreHome_Activity.this, MainActivity.class);
        intent.putExtra(Constants.HOME_SCREEN_NUMBER, i);
        startActivity(intent);

    }
}
