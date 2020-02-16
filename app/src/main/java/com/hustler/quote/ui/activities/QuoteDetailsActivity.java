package com.hustler.quote.ui.activities;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.crash.FirebaseCrash;
import com.hustler.quote.R;
import com.hustler.quote.ui.Executors.AppExecutor;
import com.hustler.quote.ui.ORM.AppDatabase;
import com.hustler.quote.ui.ORM.Tables.QuotesTable;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.superclasses.BaseActivity;
import com.hustler.quote.ui.utils.AdUtils;
import com.hustler.quote.ui.utils.FileUtils;
import com.hustler.quote.ui.utils.IntentConstants;
import com.hustler.quote.ui.utils.PermissionUtils;
import com.hustler.quote.ui.utils.TextUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

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
public class QuoteDetailsActivity extends BaseActivity implements View.OnClickListener {
    private static final int MY_PERMISSION_REQUEST_STORAGE = 1001;
    private static final int MY_PERMISSION_REQUEST_STORAGE_WALLPAPER = 1003;
    int quoteId;
    private LinearLayout quote_layout;
    private LinearLayout quote_bottom;
    private TextView tv_Quote_Body;
    private TextView tv_Quote_Author;
    private FloatingActionButton fab_set_like;
    private File savedFile;
    boolean IS_LIKED_FLAG;
    private final int MY_PERMISSION_REQUEST_STORAGE_FIRST = 1002;
    int[] color1;
    QuotesTable quoteFromTable;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private AppDatabase appDatabase;
    private AppExecutor appExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_details);
        appDatabase = AppDatabase.getmAppDatabaseInstance(this);
        appExecutor = AppExecutor.getInstance();
        ButterKnife.bind(this);
        initView();
        getIntentData();
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(ContextCompat.getDrawable(getApplicationContext(), (R.drawable.ic_keyboard_backspace_black_24dp)));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setTitle("");
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary1));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (PermissionUtils.isPermissionAvailable(QuoteDetailsActivity.this)) {
        } else {
            requestFirstAppPermissions();
        }
    }

    interface OnImageSaveListner {
        void onImageSaved(Uri uri);
    }

    private void initView() {
        RelativeLayout root = findViewById(R.id.root);
        AdView mAdView = findViewById(R.id.adView);
        tv_Quote_Author = findViewById(R.id.tv_Quote_Author);
        tv_Quote_Body = findViewById(R.id.tv_Quote_Body);

        quote_layout = findViewById(R.id.quote_layout);
        quote_bottom = findViewById(R.id.quote_bottom);
        TextUtils.findText_and_applyTypeface(root, QuoteDetailsActivity.this);

        FloatingActionButton fab_edit = findViewById(R.id.fab_edit);
        FloatingActionButton fab_save = findViewById(R.id.fab_download);
        FloatingActionButton fab_share = findViewById(R.id.fab_share);
        FloatingActionButton fab_set_wall = findViewById(R.id.fab_set_wall);
        fab_set_like = findViewById(R.id.fab_set_like);

        fab_edit.setOnClickListener(this);
        fab_save.setOnClickListener(this);
        fab_share.setOnClickListener(this);
        fab_set_wall.setOnClickListener(this);
        fab_set_like.setOnClickListener(this);

        AdUtils.loadBannerAd(mAdView, QuoteDetailsActivity.this);

    }

    @NonNull
    private GradientDrawable createDrawable(int[] colors) {
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BR_TL, colors);
        gradientDrawable.setGradientRadius(135);
        return gradientDrawable;
    }

    private void getIntentData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            quote_bottom.setElevation(getResources().getDimension(R.dimen.elevation4));
        }
        quoteId = getIntent().getIntExtra(Constants.INTENT_QUOTE_OBJECT_KEY, -1);
        appExecutor.getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                color1 = getIntent().getIntArrayExtra(IntentConstants.GRADIENT_COLOR1);
                if (color1 != null) {
                    quote_layout.setBackground(createDrawable(color1));
                } else {
                    int color1 = TextUtils.getMatColor(QuoteDetailsActivity.this, "mdcolor_500");
                    int color2 = TextUtils.getMatColor(QuoteDetailsActivity.this, "mdcolor_500");
                    quote_layout.setBackground(createDrawable(new int[]{color1, color2}));

                }
            }
        });
        LiveData<QuotesTable> quotesTableLiveData = appDatabase.quotesDao().getQuotesById(quoteId);
        quoteFromTable = quotesTableLiveData.getValue();
        quotesTableLiveData.observe(this, new Observer<QuotesTable>() {
            @Override
            public void onChanged(@Nullable QuotesTable quotesTable) {
                quoteFromTable = quotesTable;
                setUiData(quoteFromTable);

            }
        });


    }

    private void setUiData(QuotesTable quoteId) {
        int length = quoteId.getQuotes().length();


        if (length > 230) {
            tv_Quote_Body.setTextSize(25.0f);
        } else if (length < 230 && length > 150) {
            tv_Quote_Body.setTextSize(28.0f);

        } else if (length > 100 && length < 150) {
            tv_Quote_Body.setTextSize(32.0f);

        } else if (length > 50 && length < 100) {
            tv_Quote_Body.setTextSize(38.0f);

        } else if (length > 2 && length < 50) {
            tv_Quote_Body.setTextSize(42.0f);

        } else {
            tv_Quote_Body.setTextSize(48.0f);

        }
        tv_Quote_Body.setText(quoteId.getQuotes());
        tv_Quote_Author.setText(quoteId.getAuthor());
        tv_Quote_Body.setTextColor(Color.WHITE);
        tv_Quote_Author.setTextColor(Color.WHITE);
        if (quoteId.isIsliked()) {
            IS_LIKED_FLAG = true;
            fab_set_like.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_black_24dp));
        } else {
            IS_LIKED_FLAG = false;
            fab_set_like.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_black_24dp));

        }
    }


    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.fab_set_like: {
                if (IS_LIKED_FLAG) {
                    removeFavourite();
                } else {
                    addFavourite();

                }
            }
            break;
            case R.id.fab_download:
                checkpermissions_and_proceed();
                break;
            case R.id.fab_edit:
                edit();
                break;
            case R.id.fab_share:
                share();
                break;
            case R.id.fab_set_wall:
                if (PermissionUtils.isPermissionAvailable(QuoteDetailsActivity.this)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        setWallPaper();
                    } else {
                        setCompatWallpaper();
                    }
                } else {
                    requestFirstAppPermissions_Wall();
                }

                break;
        }
//        root.setBackgroundDrawable(gradientDrawable);

    }

    private void removeFavourite() {

        appExecutor.getDiskExecutor().execute(new Runnable() {
            @Override
            public void run() {
                quoteFromTable.setIsliked(false);
                appDatabase.quotesDao().updateUser(quoteFromTable);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fab_set_like.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_black_24dp));
                        IS_LIKED_FLAG = false;
                    }
                });
            }
        });

    }

    private void addFavourite() {
        appExecutor.getDiskExecutor().execute(() -> {
            quoteFromTable.setIsliked(true);
            appDatabase.quotesDao().updateUser(quoteFromTable);
            runOnUiThread(() -> {
                fab_set_like.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_black_24dp));
                IS_LIKED_FLAG = true;
            });
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setWallPaper() {
        checkandRetrieveUri(quote_layout, uri -> {
            try {
                File file = new File(uri.getPath());
                Intent intent = new Intent(WallpaperManager.
                        getInstance(getApplicationContext()).
                        getCropAndSetWallpaperIntent(FileUtils.getImageContentUri(getApplicationContext(), file)));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                FirebaseCrash.log(e.getMessage());
            }
        });

    }

    private void setCompatWallpaper() {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        try {

            wallpaperManager.setBitmap(FileUtils.returnBitmap(quote_layout, true).first);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkpermissions_and_proceed() {
        if (PermissionUtils.isPermissionAvailable(QuoteDetailsActivity.this)) {
            FileUtils.saveProjectToDevice(quote_layout, QuoteDetailsActivity.this, new FileUtils.onSaveComplete() {
                @Override
                public void onImageSaveListener(File file) {
                    savedFile = file;
                    FileUtils.showPostSaveDialog(QuoteDetailsActivity.this, savedFile);
                }
            }, 0);
        } else {
            requestAppPermissions();
        }
    }


    private void requestAppPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                (MY_PERMISSION_REQUEST_STORAGE));
    }

    private void requestFirstAppPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                (MY_PERMISSION_REQUEST_STORAGE_FIRST));
    }

    private void requestFirstAppPermissions_Wall() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                (MY_PERMISSION_REQUEST_STORAGE_WALLPAPER));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    FileUtils.saveProjectToDevice(quote_layout, QuoteDetailsActivity.this, new FileUtils.onSaveComplete() {
                        @Override
                        public void onImageSaveListener(File file) {
                            savedFile = file;
                            FileUtils.showPostSaveDialog(QuoteDetailsActivity.this, savedFile);

                        }
                    }, 0);
                }
            }
            break;
            case MY_PERMISSION_REQUEST_STORAGE_FIRST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
            }
            break;
            case MY_PERMISSION_REQUEST_STORAGE_WALLPAPER: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        setWallPaper();
                    } else {
                        setCompatWallpaper();
                    }
                }
            }


        }
    }

    private void share() {
        final Uri[] uri = new Uri[1];
        final Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "SUBJECT");
        shareIntent.putExtra(Intent.EXTRA_TITLE, "Title");
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (savedFile == null) {
            checkandRetrieveUri(quote_layout, new OnImageSaveListner() {
                @Override
                public void onImageSaved(Uri val) {
                    uri[0] = val;
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri[0]);
                    startActivity(Intent.createChooser(shareIntent, "send"));
                }
            });

        } else {

            Uri uriValue = null;
            if (Build.VERSION.SDK_INT >= 24) {
                uriValue = FileProvider.getUriForFile(getApplicationContext(), getString(R.string.file_provider_authority), savedFile);
            } else {
                uriValue = Uri.fromFile(savedFile);
            }
            shareIntent.putExtra(Intent.EXTRA_STREAM, uriValue);
            startActivity(Intent.createChooser(shareIntent, "send"));
        }
    }

    private Uri checkandRetrieveUri(@NonNull ViewGroup rootview, @NonNull final OnImageSaveListner OnImageSaveListner) {
        final Uri[] uri = {null};
        if (savedFile != null) {
            if (Build.VERSION.SDK_INT >= 24) {
                uri[0] = FileProvider.getUriForFile(getApplicationContext(), getString(R.string.file_provider_authority), savedFile);
            } else {
                uri[0] = Uri.fromFile(savedFile);
            }
        } else {
            FileUtils.saveProjectToDevice(rootview, QuoteDetailsActivity.this, new FileUtils.onSaveComplete() {
                @Override
                public void onImageSaveListener(File file) {
                    savedFile = file;
                    if (Build.VERSION.SDK_INT >= 24) {
                        uri[0] = FileProvider.getUriForFile((getApplicationContext()), getString(R.string.file_provider_authority), savedFile);
                    } else {
                        uri[0] = Uri.fromFile(savedFile);
                    }
                    OnImageSaveListner.onImageSaved(uri[0]);
                    FileUtils.showPostSaveDialog(QuoteDetailsActivity.this, savedFile);


                }
            }, 0);
        }
        return uri[0];
    }

    private void edit() {
        Intent intent = new Intent(this, EditorActivity.class);
        intent.putExtra(Constants.INTENT_QUOTE_OBJECT_KEY, quoteId);
        intent.putExtra(Constants.INTENT_IS_FROM_EDIT_KEY, 1);
        startActivity(intent);
    }
}
