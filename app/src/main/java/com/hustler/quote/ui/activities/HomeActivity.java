package com.hustler.quote.ui.activities;

import android.animation.Animator;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.IdRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.hustler.quote.R;
import com.hustler.quote.ui.Recievers.AlarmReciever;
import com.hustler.quote.ui.adapters.LocalAdapter;
import com.hustler.quote.ui.adapters.TabsFragmentPagerAdapter;
import com.hustler.quote.ui.adapters.WallpaperAdapter;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.apiRequestLauncher.Restutility;
import com.hustler.quote.ui.database.QuotesDbHelper;
import com.hustler.quote.ui.pojo.Quote;
import com.hustler.quote.ui.pojo.UnsplashImages_Collection_Response;
import com.hustler.quote.ui.pojo.Unsplash_Image_collection_response_listener;
import com.hustler.quote.ui.pojo.unspalsh.Unsplash_Image;
import com.hustler.quote.ui.utils.AdUtils;
import com.hustler.quote.ui.utils.ColorUtils;
import com.hustler.quote.ui.utils.TextUtils;
import com.hustler.quote.ui.utils.Toast_Snack_Dialog_Utils;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private AppBarLayout appBar;
    private FloatingActionButton floatingActionButton;
    private ViewPager mainPager;
    private TabLayout tab_layout;
    Window window;
    Animator anim;
    TextView header_name;
    CoordinatorLayout rootView;
    int cx, cy;
    float finalRadius;
    int[] colors;
    Toolbar toolbar;
    final String IMAGES = "images";
    final String QUOTES = "quotes";
    private AdView mAdView;
    String query;
    Intent alarm_intent;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_main);
        MobileAds.initialize(HomeActivity.this, Constants.ADS_APP_ID);

        findViews();
        cx = appBar.getWidth() / 2;
        cy = appBar.getHeight() / 2;
        finalRadius = (float) StrictMath.hypot(cx, cy);
        window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        colors = new int[]{
                ContextCompat.getColor(HomeActivity.this, R.color.pink_400),
                ContextCompat.getColor(HomeActivity.this, R.color.colorAccent),
                ContextCompat.getColor(HomeActivity.this, R.color.green_300),
                ContextCompat.getColor(HomeActivity.this, R.color.orange_300),
                ContextCompat.getColor(HomeActivity.this, R.color.textColor)};

        editTabLayout();

    }

    private void editTabLayout() {
        ViewGroup vg = (ViewGroup) tab_layout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();

            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    TextView textView = (TextView) tabViewChild;
                    textView.setAllCaps(false);
//                    setAnimation(textView);
                    TextUtils.setFont(HomeActivity.this, textView, Constants.FONT_ZINGCURSIVE);
                }
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
//        setAnimation(appBar);
        setAnimation(floatingActionButton);
        setAnimation(mainPager);
        setAnimation(tab_layout);
    }

    private void findViews() {

        appBar = findViewById(R.id.app_bar);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        mainPager = (ViewPager) findViewById(R.id.main_pager);
        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        rootView = (CoordinatorLayout) findViewById(R.id.root);
        tab_layout.setupWithViewPager(mainPager);
        mAdView = (AdView) findViewById(R.id.adView);
        header_name = (TextView) findViewById(R.id.header_name);
        TextUtils.setFont(HomeActivity.this, header_name, Constants.FONT_ZINGCURSIVE);
        loadAds();
        mainPager.setAdapter(new TabsFragmentPagerAdapter(this, getSupportFragmentManager()));
        mainPager.setCurrentItem(1);
        mainPager.setOnPageChangeListener(this);
        floatingActionButton.setOnClickListener(this);

        getIntentData(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        getIntentData(intent);
    }

    private void getIntentData(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            Toast_Snack_Dialog_Utils.show_ShortToast(HomeActivity.this, query);
            buildDialog_and_search(query);
        }
    }


    private void buildDialog_and_search(final String query) {

        final Dialog dialog = new Dialog(HomeActivity.this, R.style.EditTextDialog_non_floater);
        dialog.setContentView(R.layout.search_chooser_layout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog_non_floater;
//        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(andro));
        dialog.setCancelable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setStatusBarColor(ContextCompat.getColor(HomeActivity.this, R.color.colorPrimary));
        }


        final RelativeLayout root;
        final EditText search_Query;
        ImageView search;
        RadioGroup radioGroup;
        FloatingActionButton close;
        final RecyclerView result_rv;
        final ProgressBar loader;
        final String[] selected_type = new String[1];
        selected_type[0] = IMAGES;
        AdView adView;


        search_Query = (EditText) dialog.findViewById(R.id.header_name);
        search = (ImageView) dialog.findViewById(R.id.search);
        radioGroup = (RadioGroup) dialog.findViewById(R.id.rd_group);
        close = (FloatingActionButton) dialog.findViewById(R.id.search_btn);
        root = (RelativeLayout) dialog.findViewById(R.id.root);
        result_rv = (RecyclerView) dialog.findViewById(R.id.result_rv);
        loader = (ProgressBar) dialog.findViewById(R.id.loader);
        adView = (AdView) dialog.findViewById(R.id.adView);
        AdUtils.loadBannerAd(adView, HomeActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            result_rv.setClipToOutline(true);
        }
        setImages(result_rv, query, loader);
//        setQuotes(result_rv, query, loader);

        TextUtils.findText_and_applyTypeface(root, HomeActivity.this);
        TextUtils.findText_and_applyamim_slideup(root, HomeActivity.this);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_images: {
//                        setImages(result_rv, query, loader);
                        selected_type[0] = IMAGES;
                    }
                    break;
                    case R.id.rb_quotes: {
//                        setQuotes(result_rv, query, loader);
                        selected_type[0] = QUOTES;
                    }
                    break;

                }
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_Query.getText().length() <= 0 || search_Query.getText() == null) {
                    search_Query.setError(getString(R.string.enter_something));
                } else {
                    switch (selected_type[0]) {
                        case IMAGES: {
                            setImages(result_rv, search_Query.getText().toString(), loader);
                        }
                        break;
                        case QUOTES: {
                            setQuotes(result_rv, search_Query.getText().toString(), loader);
                        }
                        break;

                    }
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result_rv.setAdapter(null);
                TextUtils.findText_and_applyamim_slidedown(root, HomeActivity.this);

                dialog.dismiss();

            }
        });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_BACK || keyCode == event.KEYCODE_HOME) {
                    TextUtils.findText_and_applyamim_slidedown(root, HomeActivity.this);

                    dialog.dismiss();
                    return true;
                } else {
                    return false;
                }
            }
        });
        dialog.show();


    }

    private void setImages(final RecyclerView rv, String query, final ProgressBar loader) {
        rv.setAdapter(null);
        loader.setVisibility(View.VISIBLE);
        final String request = Constants.API_GET_Collections_FROM_UNSPLASH + "&query=" + query + "&per_page=30";
        new Restutility(HomeActivity.this).getUnsplash_Collections_Images(HomeActivity.this, new Unsplash_Image_collection_response_listener() {
            @Override
            public void onSuccess(final UnsplashImages_Collection_Response response) {

                loader.setVisibility(View.GONE);
                rv.setAdapter(null);
                rv.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

                Log.i("VALUE FROM UNSPLASH", String.valueOf(response.getResults().length));
                if (response.getResults().length <= 0) {
//                    dataView.setVisibility(View.GONE);
                    Toast_Snack_Dialog_Utils.show_ShortToast(HomeActivity.this, getString(R.string.Currently_no_wallpaper));
                } else {
//                    dataView.setVisibility(View.VISIBLE);
                    rv.setAdapter(new WallpaperAdapter(HomeActivity.this, response.getResults(), new WallpaperAdapter.OnWallpaperClickListener() {
                        @Override
                        public void onWallpaperClicked(int position, ArrayList<Unsplash_Image> unsplash_images) {
//                            Toast_Snack_Dialog_Utils.show_ShortToast(HomeActivity.this, wallpaper.getUser().getFirst_name());
                            Intent intent = new Intent(HomeActivity.this, WallpapersPagerActivity.class);

                            intent.putExtra(Constants.Pager_position, position);
                            intent.putExtra(Constants.PAGER_LIST_WALL_OBKHECTS, unsplash_images);
                            intent.putExtra(Constants.is_from_fav, false);

                            startActivity(intent);
                        }
                    }, 3));

                }
            }

            @Override
            public void onError(String error) {
                Log.e("ERROR FROM UNSPLASH", error);
                loader.setVisibility(View.GONE);

//                dataView.setVisibility(View.GONE);
                Toast_Snack_Dialog_Utils.show_ShortToast(HomeActivity.this, getString(R.string.Failed));
            }
        }, request);

    }

    private void setQuotes(final RecyclerView result_rv, final String query, final ProgressBar loader) {
        loader.setVisibility(View.VISIBLE);
        result_rv.setAdapter(null);
        final ArrayList<Quote>[] quoteslisttemp = new ArrayList[]{new ArrayList<>(), new ArrayList<>()};

        new Thread(new Runnable() {
            @Override
            public void run() {
                quoteslisttemp[0] = (ArrayList<Quote>) new QuotesDbHelper(HomeActivity.this).getQuotesBystring(query);
                quoteslisttemp[1] = (ArrayList<Quote>) new QuotesDbHelper(HomeActivity.this).getQuotesByCategory(query);
                ArrayList<Quote> finalArrayList = new ArrayList<Quote>();
                quoteslisttemp[1].remove(quoteslisttemp[0]);
                finalArrayList.addAll(quoteslisttemp[0]);
                finalArrayList.addAll(quoteslisttemp[1]);
                if (finalArrayList.size() <= 0) {
                    loader.setVisibility(View.GONE);

                    Toast_Snack_Dialog_Utils.show_ShortToast(HomeActivity.this, getString(R.string.no_quotes_available));
                } else {
                    loader.setVisibility(View.GONE);
                    result_rv.setAdapter(new LocalAdapter(HomeActivity.this, finalArrayList, new LocalAdapter.OnQuoteClickListener() {
                        @Override
                        public void onQuoteClicked(int position, int color, Quote quote, View view) {
                            Intent intent = new Intent(HomeActivity.this, QuoteDetailsActivity.class);
                            intent.putExtra(Constants.INTENT_QUOTE_OBJECT_KEY, quote);
                            startActivity(intent);
                        }
                    }));
                }
            }
        }).run();

    }

    private void loadAds() {
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("A5B1E467FD401973F9F69AD2CCC13C30").build();
        mAdView.loadAd(adRequest);

    }

    private void setAnimation(View view) {
        view.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search: {

            }
            case R.id.action_Pro_features: {


                alarm_intent = new Intent(getApplicationContext(), AlarmReciever.class);
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarm_intent, 0);
                alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 60 * 1000, pendingIntent);
                Log.i("ALARM SET", "SET");

            }
            case R.id.action_Pro_about: {
                alarm_intent = new Intent(getApplicationContext(), AlarmReciever.class);
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarm_intent, 0);
                alarmManager.cancel(pendingIntent);
                Log.i("ALARM Canceled", "Cancel");


            }
        }
        return true;
    }

    @Override
    public void onClick(final View v) {
////        DIALOG FOR EDITORS
//        if (v == floatingActionButton) {
//            Button quote, meme, close;
//            TextView quote_tv, meme_tv, close_tv, select_tv;
//
//
//            // Handle clicks for floatingActionButton
//            final Dialog dialog = new Dialog(this, R.style.EditTextDialog_scaled);
//            final View view = View.inflate(this, R.layout.edit_chooser, null);
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog.setContentView(view);
//
//            quote = (Button) dialog.findViewById(R.id.Quotes);
//            meme = (Button) dialog.findViewById(R.id.meme);
//            close = (Button) dialog.findViewById(R.id.close);
//
//            quote_tv = (TextView) dialog.findViewById(R.id.Quotes_tv);
//            meme_tv = (TextView) dialog.findViewById(R.id.meme_tv);
//            close_tv = (TextView) dialog.findViewById(R.id.close_tv);
//            select_tv = (TextView) dialog.findViewById(R.id.select_tv);
//
//            TextUtils.setFont(HomeActivity.this, quote_tv, Constants.FONT_CIRCULAR);
//            TextUtils.setFont(HomeActivity.this, meme_tv, Constants.FONT_CIRCULAR);
//            TextUtils.setFont(HomeActivity.this, close_tv, Constants.FONT_CIRCULAR);
//            TextUtils.setFont(HomeActivity.this, select_tv, Constants.FONT_CIRCULAR);
//
//            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
//                @Override
//                public void onShow(DialogInterface dialog) {
////                    AnimUtils.revealCircular(floatingActionButton, dialog, view, true);
//                }
//            });
//
//            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//                @Override
//                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                    if (keyCode == event.KEYCODE_BACK) {
//                        dialog.dismiss();
////                        AnimUtils.revealCircular(floatingActionButton, dialog, view, false);
//                        return true;
//                    }
//                    return false;
//                }
//            });
//
//
//            quote.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // TODO: 02-12-2017 implement to go quotes editor
//                    dialog.dismiss();
//                    Intent intent = new Intent(HomeActivity.this, EditorActivity.class);
//                    intent.putExtra(Constants.INTENT_IS_FROM_EDIT_KEY, 1);
//                    startActivity(intent);
//                }
//            });
//
//            meme.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // TODO: 02-12-2017 implement to go to meme Editor
//                }
//            });
//            close.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
////                    AnimUtils.revealCircular(floatingActionButton, dialog, view, false);
//
//                }
//            });
//            view.setBackgroundColor(getResources().getColor(android.R.color.white));
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog_scaled;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                dialog.getWindow().setStatusBarColor(ContextCompat.getColor(HomeActivity.this, android.R.color.white));
//            }
//            dialog.show();
//        }

        Intent intent = new Intent(HomeActivity.this, EditorActivity.class);
        intent.putExtra(Constants.INTENT_IS_FROM_EDIT_KEY, 1);
        startActivity(intent);
    }


    /*VIEW PAGER METHODS*/
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        appBar.setBackgroundColor(ColorUtils.getHEaderColor(colors, position, positionOffset, HomeActivity.this));
        mainPager.setBackgroundColor(ColorUtils.getHEaderColor(colors, position, positionOffset, HomeActivity.this));
        tab_layout.setBackgroundColor(ColorUtils.getHEaderColor(colors, position, positionOffset, HomeActivity.this));
//        floatingActionButton.setBackgroundColor((getHEaderColor(position, positionOffset)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ColorUtils.getHEaderColor(colors, position, positionOffset, HomeActivity.this));
        }


//        switch (position){
//            case 0:{
//                tab_layout.setBackgroundColor(this.getResources().getColor(R.color.colorPrimaryDark));
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    anim = ViewAnimationUtils.createCircularReveal(tab_layout, cx, cy, 0, finalRadius);
//
//                }
//                appBar.setVisibility(View.VISIBLE);
//
//                anim.start();
//
//            }break;case 1:{
//                tab_layout.setBackgroundColor(this.getResources().getColor(R.color.colorAccent));
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    anim = ViewAnimationUtils.createCircularReveal(tab_layout, cx, cy, 0, finalRadius);
//                }
//                appBar.setVisibility(View.VISIBLE);
//
//                anim.start();
//
//            }break;case 2:{
//                tab_layout.setBackgroundColor(this.getResources().getColor(R.color.textColor));
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    anim = ViewAnimationUtils.createCircularReveal(tab_layout, cx, cy, 0, finalRadius);
//                }
//                appBar.setVisibility(View.VISIBLE);
//
//                anim.start();
//
//
//            }break;
//        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onBackPressed() {
        Toast_Snack_Dialog_Utils.createDialog(HomeActivity.this, getString(R.string.warning), getString(R.string.are_you_sure_close), getString(R.string.cancel), getString(R.string.close), new Toast_Snack_Dialog_Utils.Alertdialoglistener() {
            @Override
            public void onPositiveselection() {
                finishAffinity();
                System.exit(0);
            }

            @Override
            public void onNegativeSelection() {
                return;
            }
        });
//        super.onBackPressed();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            this.finishAndRemoveTask();
//        }
//        else {
//            this.finishAffinity();
//        }
//        System.exit(0);
    }
}
