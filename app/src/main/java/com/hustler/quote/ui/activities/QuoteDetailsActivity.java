package com.hustler.quote.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hustler.quote.R;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.pojo.QuotesFromFC;
import com.hustler.quote.ui.superclasses.App;
import com.hustler.quote.ui.superclasses.BaseActivity;
import com.hustler.quote.ui.utils.FileUtils;

import java.io.File;

import static com.hustler.quote.ui.utils.FileUtils.savetoDevice;

public class QuoteDetailsActivity extends BaseActivity implements View.OnClickListener {
    private static final int MY_PERMISSION_REQUEST_STORAGE = 1001;
    QuotesFromFC quote;
    RelativeLayout root;
    LinearLayout quote_layout;
    LinearLayout quote_bottom;
    TextView tv_Quote_Body, tv_Quote_Author, image_saved_message;
    FloatingActionButton fab_save, fab_edit, fab_share, fab_set_wall;
    ImageView quote_anim;
    File savedFile;
    Window window;
    int val = 1001;
    private RelativeLayout wallpaper_layout;

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
        root = (RelativeLayout) findViewById(R.id.root);
        tv_Quote_Author = (TextView) findViewById(R.id.tv_Quote_Author);
        tv_Quote_Body = (TextView) findViewById(R.id.tv_Quote_Body);

        quote_layout = (LinearLayout) findViewById(R.id.quote_layout);
        wallpaper_layout = (RelativeLayout) findViewById(R.id.wallpaper_layout);
        quote_bottom = (LinearLayout) findViewById(R.id.quote_bottom);
        quote_anim = (ImageView) findViewById(R.id.quote_anim);
        Drawable drawable = quote_anim.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }

        tv_Quote_Author.setTypeface(App.getZingCursive(QuoteDetailsActivity.this, Constants.FONT_ZINGCURSIVE));
        tv_Quote_Body.setTypeface(App.getZingCursive(QuoteDetailsActivity.this, Constants.FONT_ZINGSANS));

        fab_edit = (FloatingActionButton) findViewById(R.id.fab_edit);
        fab_save = (FloatingActionButton) findViewById(R.id.fab_download);
        fab_share = (FloatingActionButton) findViewById(R.id.fab_share);
        fab_set_wall = (FloatingActionButton) findViewById(R.id.fab_set_wall);

        fab_edit.setOnClickListener(this);
        fab_save.setOnClickListener(this);
        fab_share.setOnClickListener(this);
        fab_set_wall.setOnClickListener(this);


//        For building the image
        quote_layout.setDrawingCacheEnabled(true);
        quote_layout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        quote_layout.layout(0, 0, quote_layout.getMeasuredWidth(), quote_layout.getMeasuredHeight());
        quote_layout.buildDrawingCache(true);
    }

    @Override
    public void setToolbar(Activity activity) {
        super.setToolbar(activity);
        window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    private void getIntentData() {
//        Bundle bundle=getIntent().getBundleExtra(Constants.BUNDLE_OBJECT);
        quote = (QuotesFromFC) getIntent().getSerializableExtra(Constants.INTENT_QUOTE_OBJECT_KEY);
//        Toast.makeText(this, quote.getBody() + quote.getColor(), Toast.LENGTH_SHORT).show();
        int length = quote.getBody().length();
        root.setBackgroundColor(quote.getColor());
        fab_share.setBackgroundColor(quote.getColor());
        quote_bottom.setBackgroundColor(quote.getColor());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            quote_bottom.setElevation(getResources().getDimension(R.dimen.elevation4));
            window.setStatusBarColor(quote.getColor());
        }
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
        switch (v.getId()) {
            case R.id.fab_download:
                checkpermissions_and_proceed();
                break;
            case R.id.fab_edit:
                changeFont();
                break;
            case R.id.fab_share:
                share();
                break;
            case R.id.fab_set_wall:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    setWallPaer();
                } else {
                    setWallPaerCompat();
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setWallPaer() {
        // TODO: 13/12/2017 ask permissions
        Intent intent = new Intent(WallpaperManager.getInstance(this).
                getCropAndSetWallpaperIntent(FileUtils.getImageContentUri(this, new File(checkandRetrieveUri(wallpaper_layout).getPath()))));

        startActivity(intent);

    }

    private void setWallPaerCompat() {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        try {

            wallpaperManager.setBitmap(FileUtils.returnBitmap(quote_layout));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkpermissions_and_proceed() {
        if (isPermissionAvailable()) {
            savedFile = savetoDevice(quote_layout);
        } else {
            requestAppPermissions();
        }
    }

    private boolean isPermissionAvailable() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            return false;
        } else {
            return true;
        }
    }

    private void requestAppPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                (MY_PERMISSION_REQUEST_STORAGE));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    savedFile = savetoDevice(quote_layout);
                }
            }
        }
    }

    private void share() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "SUBJECT");
        shareIntent.putExtra(Intent.EXTRA_TITLE, "Title");
        Uri uri = checkandRetrieveUri(quote_layout);
        if (uri != null) {
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/jpeg");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "send"));
        } else {
            return;
        }


    }

    private Uri checkandRetrieveUri(ViewGroup rootview) {
        Uri uri = null;
        if (savedFile != null) {
            uri = Uri.fromFile(savedFile);
        } else if (savedFile == null) {
            savedFile = savetoDevice(rootview);
            uri = Uri.fromFile(savedFile);
        }
        return uri;
    }

    private void changeFont() {
        tv_Quote_Body.setTypeface(App.getZingCursive(this, Constants.FONT_NEVIS));
//        int cx=image_saved_message.getWidth()/2;
//        int cy=image_saved_message.getHeight()/2;
//         float finalradius=((float) StrictMath.hypot(cx,cy));
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            Animator anim =
//                    ViewAnimationUtils.createCircularReveal(image_saved_message, cx, cy, 0, finalradius);
////            anim.
//            anim.start();
//        }

        Intent intent = new Intent(this, EditorActivity.class);

        intent.putExtra(Constants.INTENT_QUOTE_OBJECT_KEY, quote);
        intent.putExtra(Constants.INTENT_IS_FROM_EDIT_KEY, true);
        startActivity(intent);
    }

//    private void savetoDevice(ViewGroup layout) {
//
//
//        new Thread() {
//            @Override
//            public void run() {
//                Bitmap bitmap = quote_layout.getDrawingCache();
////        quote_layout.setDrawingCacheEnabled(false);
//
//                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//
//                File file = new File(new StringBuilder().append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES))
//                        .append(File.separator)
//                        .append(quote.getBody().substring(0, 10))
//                        .append(quote.getAuthor())
//                        .append(System.currentTimeMillis())
//                        .append(".jpeg")
//                        .toString());
//                savedFile = file;
//                Log.d("ImageLocation -->", file.toString());
//                try {
//                    file.createNewFile();
//                    FileOutputStream fileOutputStream = new FileOutputStream(file);
//                    fileOutputStream.write(byteArrayOutputStream.toByteArray());
//                    fileOutputStream.close();
////                    App.showToast(QuoteDetailsActivity.this,getString(R.string.image_saved));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//
//
//    }
}
