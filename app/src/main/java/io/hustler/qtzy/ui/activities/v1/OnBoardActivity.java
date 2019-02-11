package io.hustler.qtzy.ui.activities.v1;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import io.hustler.qtzy.R;
import io.hustler.qtzy.ui.fragments.v1.SplashFragment;

public class OnBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);
        getSupportFragmentManager().beginTransaction().add(R.id.frame, SplashFragment.getInstance()).commit();
    }
}
