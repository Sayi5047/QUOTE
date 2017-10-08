package com.hustler.quote.ui.activities;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hustler.quote.R;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.pojo.QuotesFromFC;
import com.hustler.quote.ui.superclasses.App;
import com.hustler.quote.ui.superclasses.BaseActivity;

public class QuoteDetailsActivity extends BaseActivity {
    QuotesFromFC quote;
    LinearLayout root;
    TextView tv_Quote_Body, tv_Quote_Author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_details);
        setToolbar(this);
        initView();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            setExplodeAnimation();
//        }
        getIntentData();

    }

    private void initView() {
        root = (LinearLayout) findViewById(R.id.root);
        tv_Quote_Author = (TextView) findViewById(R.id.tv_Quote_Author);
        tv_Quote_Body = (TextView) findViewById(R.id.tv_Quote_Body);
        tv_Quote_Author.setTypeface(App.getZingCursive(QuoteDetailsActivity.this, Constants.FONT_ZINGCURSIVE));
        tv_Quote_Body.setTypeface(App.getZingCursive(QuoteDetailsActivity.this, Constants.FONT_ZINGSANS));
    }

    @Override
    public void setToolbar(Activity activity) {
        super.setToolbar(activity);

    }

    private void getIntentData() {
//        Bundle bundle=getIntent().getBundleExtra(Constants.BUNDLE_OBJECT);
        quote = (QuotesFromFC) getIntent().getSerializableExtra(Constants.INTENT_QUOTE_OBJECT);
        Toast.makeText(this, quote.getBody() + quote.getColor(), Toast.LENGTH_SHORT).show();
        int length=quote.getBody().length();
        root.setBackgroundColor(quote.getColor());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(quote.getColor()));
        if(length>230){
            tv_Quote_Body.setTextSize(20.0f);
        }else if(length<230 && length>150 ) {
            tv_Quote_Body.setTextSize(25.0f);

        }
        else if(length>100 && length<150 ){
            tv_Quote_Body.setTextSize(30.0f);

        }
        else if(length>50 && length<100){
            tv_Quote_Body.setTextSize(35.0f);

        }
        else if(length>2 && length<50){
            tv_Quote_Body.setTextSize(40.0f);

        }
        else {
            tv_Quote_Body.setTextSize(45.0f);

        }
        tv_Quote_Body.setText(quote.getBody());
        tv_Quote_Author.setText(quote.getAuthor());

    }
}
