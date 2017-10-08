package com.hustler.quote.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class QuoteDetailsActivity extends BaseActivity implements View.OnClickListener{
    QuotesFromFC quote;
    LinearLayout root,quote_layout;
    TextView tv_Quote_Body, tv_Quote_Author;
    FloatingActionButton fab_save,fab_edit,fab_share;
    File savedFile;

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
        quote_layout=(LinearLayout) findViewById(R.id.quote_layout);

        tv_Quote_Author.setTypeface(App.getZingCursive(QuoteDetailsActivity.this, Constants.FONT_ZINGCURSIVE));
        tv_Quote_Body.setTypeface(App.getZingCursive(QuoteDetailsActivity.this, Constants.FONT_ZINGSANS));

        fab_edit=(FloatingActionButton) findViewById(R.id.fab_edit);
        fab_save=(FloatingActionButton) findViewById(R.id.fab_download);
        fab_share=(FloatingActionButton) findViewById(R.id.fab_share);

        fab_edit.setOnClickListener(this);
        fab_save.setOnClickListener(this);
        fab_share.setOnClickListener(this);

//        For building the image
        quote_layout.setDrawingCacheEnabled(true);
        quote_layout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        quote_layout.layout(0,0,quote_layout.getMeasuredWidth(),quote_layout.getMeasuredHeight());
        quote_layout.buildDrawingCache(true);
    }

    @Override
    public void setToolbar(Activity activity) {
        super.setToolbar(activity);

    }

    private void getIntentData() {
//        Bundle bundle=getIntent().getBundleExtra(Constants.BUNDLE_OBJECT);
        quote = (QuotesFromFC) getIntent().getSerializableExtra(Constants.INTENT_QUOTE_OBJECT);
        Toast.makeText(this, quote.getBody() + quote.getColor(), Toast.LENGTH_SHORT).show();
        int length = quote.getBody().length();
        root.setBackgroundColor(quote.getColor());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(quote.getColor()));
        if (length > 230) {
            tv_Quote_Body.setTextSize(20.0f);
        } else if (length < 230 && length > 150) {
            tv_Quote_Body.setTextSize(25.0f);

        } else if (length > 100 && length < 150) {
            tv_Quote_Body.setTextSize(30.0f);

        } else if (length > 50 && length < 100) {
            tv_Quote_Body.setTextSize(35.0f);

        } else if (length > 2 && length < 50) {
            tv_Quote_Body.setTextSize(40.0f);

        } else {
            tv_Quote_Body.setTextSize(45.0f);

        }
        tv_Quote_Body.setText(quote.getBody());
        tv_Quote_Author.setText(quote.getAuthor());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_download:
                savetoDevice();

                break;
            case R.id.fab_edit:
                changeFont();
                break;
            case R.id.fab_share:
                share();
                break;
        }
    }

    private void share() {
        Intent shareIntent=new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,"SUBJECT");
        shareIntent.putExtra(Intent.EXTRA_TITLE,"Title");
        Uri uri=null;
        if(savedFile!=null){
            uri=Uri.fromFile(savedFile);
        }
        else {
            savetoDevice();
            if(savedFile!=null){
                uri=Uri.fromFile(savedFile);
            }else {
                App.showToast(this,getString(R.string.Unable_to_save_share_image));
                return;
            }
        }
        shareIntent.putExtra(Intent.EXTRA_STREAM,uri);
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "send"));

    }

    private void changeFont() {
        tv_Quote_Body.setTypeface(App.getZingCursive(this,Constants.FONT_NEVIS));
    }

    private void savetoDevice() {
        new Thread(){
            @Override
            public void run() {
                Bitmap bitmap=quote_layout.getDrawingCache();
//        quote_layout.setDrawingCacheEnabled(false);

                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);

                File file=new File(new StringBuilder().append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)).append(File.separator).append(quote.getBody().substring(0,10)).append(quote.getAuthor()).append(System.currentTimeMillis()).append(".jpeg").toString());
                savedFile=file;
                Log.d("ImageLocation -->",file.toString());
                try
                {
                    file.createNewFile();
                    FileOutputStream fileOutputStream=new FileOutputStream(file);
                    fileOutputStream.write(byteArrayOutputStream.toByteArray());
                    fileOutputStream.close();
//                    App.showToast(QuoteDetailsActivity.this,getString(R.string.image_saved));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();


    }
}
