package io.hustler.qtzy.ui.activities.v1;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import io.hustler.qtzy.R;
import io.hustler.qtzy.ui.fragments.v1.SplashFragment;

public class OnBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.black_overlay));
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);
//        getSupportFragmentManager().beginTransaction().add(R.id.frame, SplashFragment.getInstance()).commit();
    }
}
