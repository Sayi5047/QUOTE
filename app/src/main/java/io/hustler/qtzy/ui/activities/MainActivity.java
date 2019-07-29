package io.hustler.qtzy.ui.activities;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.hustler.qtzy.R;
import io.hustler.qtzy.ui.ORM.Tables.QuotesTable;
import io.hustler.qtzy.ui.ViewModels.SearchQuotesViewModel;
import io.hustler.qtzy.ui.adapters.LocalAdapter;
import io.hustler.qtzy.ui.adapters.MainPagerAdapter;
import io.hustler.qtzy.ui.adapters.SearchWallpaperAdapter;
import io.hustler.qtzy.ui.apiRequestLauncher.Constants;
import io.hustler.qtzy.ui.apiRequestLauncher.Restutility;
import io.hustler.qtzy.ui.fragments.HomeHolderFragments.QuotesHolderFragment;
import io.hustler.qtzy.ui.pojo.ResGetSearchResultsDto;
import io.hustler.qtzy.ui.pojo.listeners.SearchImagesResponseListener;
import io.hustler.qtzy.ui.pojo.unspalsh.Unsplash_Image;
import io.hustler.qtzy.ui.utils.AdUtils;
import io.hustler.qtzy.ui.utils.IntentConstants;
import io.hustler.qtzy.ui.utils.PermissionUtils;
import io.hustler.qtzy.ui.utils.TextUtils;
import io.hustler.qtzy.ui.utils.Toast_Snack_Dialog_Utils;

import static android.view.View.GONE;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {


    private static final int MY_PERMISSION_REQUEST_ = 9007;
    private final String IMAGES = "images";
    private final String QUOTES = "quotes";
    @Nullable
    LocalAdapter adapter;

    FragmentTransaction transaction;

    TextView header_name;
    LinearLayout linearLayout;
    RelativeLayout main_view;

    @Nullable
    @BindView(R.id.root)
    FrameLayout root;
    @Nullable
    @BindView(R.id.fab)
    public FloatingActionButton fab;
    @Nullable
    @BindView(R.id.quotes_iv)
    ImageView quotesIv;
    @Nullable
    @BindView(R.id.wallpaer_iv)
    ImageView wallpaerIv;
    @Nullable
    @BindView(R.id.create_iv)
    ImageView createIv;
    @Nullable
    @BindView(R.id.like_iv)
    ImageView likeIv;
    @Nullable
    @BindView(R.id.works_iv)
    ImageView worksIv;
    @Nullable
    @BindView(R.id.bottom)
    RelativeLayout bottom;
    @BindView((R.id.scalelayout))
    LinearLayout scaleLayout;

    @Nullable
    @BindView(R.id.topCrown)
    ImageView crownView;
    @BindView(R.id.myViewPager)
    ViewPager myViewPager;

    private SearchQuotesViewModel searchQuotesViewModel;

    private int previousPixles;
    private ImageView previousImageView;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_rect));
            getWindow().setClipToOutline(true);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.WHITE));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        MobileAds.initialize(MainActivity.this, Constants.ADS_APP_ID);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        main_view = findViewById(R.id.main_view);
        linearLayout = findViewById(R.id.header_ll);
        header_name = findViewById(R.id.header_name);
        fab = findViewById(R.id.fab);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        checkPermission_and_proceed();
        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getApplicationContext(), getSupportFragmentManager());
        launchFragmentAndAnimate(previousPixles, quotesIv);

        myViewPager.setAdapter(mainPagerAdapter);
        myViewPager.setNestedScrollingEnabled(true);
        header_name.setVisibility(GONE);
        myViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        launchFragmentAndAnimate(previousPixles, quotesIv);
                        break;
                    case 1:
                        launchFragmentAndAnimate(previousPixles, wallpaerIv);
                        break;
                    case 2:
                        launchFragmentAndAnimate(previousPixles, likeIv);
                        break;
                    case 3:
                        launchFragmentAndAnimate(previousPixles, worksIv);
                        break;


                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (fab != null) {
            fab.setVisibility(GONE);
        }


        TextUtils.setFont(MainActivity.this, header_name, Constants.FONT_CIRCULAR);
        header_name.postDelayed(new Runnable() {
            @Override
            public void run() {
                header_name.setVisibility(View.VISIBLE);
            }
        }, 1000);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                main_view.setTranslationX(slideOffset * drawerView.getWidth());
//                main_view.setTranslationY(slideOffset * drawerView.getWidth());
//                main_view.setTranslationZ(-slideOffset * drawerView.getWidth() * 20);
                Log.d("SLIDEOFFSET", String.valueOf(slideOffset));
                navigationView.bringChildToFront(scaleLayout);
                Float val = 1 - Math.abs(Float.valueOf(new DecimalFormat("0.00").format(slideOffset)));

                if (val > 0.75) {
                    main_view.setScaleX(val);
                    main_view.setScaleY(val);
                }
                Log.d("SLIVAL", String.valueOf(val));
                main_view.setX(navigationView.getWidth() * slideOffset);


                int color = ColorUtils.blendARGB(getResources().getColor(R.color.colorPrimary1), getResources().getColor(R.color.WHITE), val);
                getWindow().setStatusBarColor(color
                );
                navigationView.setBackgroundColor(color);
                navigationView.setAlpha(1 - val);
                scaleLayout.setBackgroundColor(color);
                main_view.bringToFront();
                drawer.requestLayout();

            }
        };
        navigationView.setElevation(0);
        drawer.setScrimColor(Color.TRANSPARENT);
        drawer.addDrawerListener(toggle);
        drawer.setScrimColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));
        drawer.setElevation(0f);

        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        TextUtils.setMenu_Font(navigationView.getMenu(), MainActivity.this);
        for (int i = 0; i < navigationView.getHeaderCount(); i++) {
            TextUtils.findText_and_applyTypeface(((LinearLayout) navigationView.getHeaderView(i)), MainActivity.this);
        }
        launchFragment(new QuotesHolderFragment());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        TextUtils.setMenu_Font(menu, MainActivity.this);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search: {
                buildDialog_and_search(searchQuotesViewModel);

            }
            break;
            case R.id.action_rate: {
                taketoRate();
            }
            break;
            case R.id.action_Pro_features: {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slideup, R.anim.slidedown);
            }
            break;

            case R.id.action_Pro_about: {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slideup, R.anim.slidedown);

            }
            break;
            case R.id.action_Pro_credits: {
                launch_credits_dialog();

            }
            break;
            case R.id.action_Pro_write: {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "quotzyapp@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "From Quotzy User");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));

            }
            break;
            case R.id.action_Pro_rate: {
                taketoRate();

            }
            break;

        }
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_quotes) {
            Objects.requireNonNull(quotesIv).performClick();

        } else if (id == R.id.nav_quote_category) {
            assert quotesIv != null;
            sendToNextActivity(1);
        } else if (id == R.id.nav_collections) {
            assert quotesIv != null;
            sendToNextActivity(2);
        } else if (id == R.id.nav_wallpapers) {
            assert wallpaerIv != null;
            wallpaerIv.performClick();
        } else if (id == R.id.nav_favourites) {
            assert likeIv != null;
            likeIv.performClick();
        } else if (id == R.id.nav_works) {
            assert worksIv != null;
            worksIv.performClick();
        } else if (id == R.id.nav_rate) {
            taketoRate();
        } else if (id == R.id.nav_settings) {
            takeToSettings();
        } else if (id == R.id.nav_about) {
            teakeToAbout();
        } else if (id == R.id.nav_send) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "quotzyapp@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "From Quotzy User");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @OnClick({R.id.fab, R.id.quotes_iv, R.id.wallpaer_iv, R.id.create_iv, R.id.like_iv, R.id.works_iv})
    public void onViewClicked(@NonNull View view) {
        switch (view.getId()) {
            case R.id.quotes_iv:
                myViewPager.setCurrentItem(0);
                launchFragmentAndAnimate(previousPixles, quotesIv);
                break;
            case R.id.wallpaer_iv:
                myViewPager.setCurrentItem(1);
                launchFragmentAndAnimate(previousPixles, wallpaerIv);
                break;
            case R.id.like_iv:
                myViewPager.setCurrentItem(2);
                launchFragmentAndAnimate(previousPixles, likeIv);
                break;
            case R.id.works_iv:
                myViewPager.setCurrentItem(3);
                launchFragmentAndAnimate(previousPixles, worksIv);
                break;
            case R.id.create_iv:
                startActivity(new Intent(MainActivity.this, EditorActivity.class).putExtra(Constants.INTENT_IS_FROM_EDIT_KEY, 1));
                overridePendingTransition(R.anim.slideup, R.anim.slidedown);
                break;
        }
    }


    private void takeToSettings() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slideup, R.anim.slidedown);

    }

    private void teakeToAbout() {

        launch_credits_dialog();
    }

    private void sendToNextActivity(int i) {
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra(Constants.INTENT_SECONDACTIVITY_CONSTANT, i);
        startActivity(intent);
    }

    public void launchFragment(Fragment fragment) {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.root, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();

    }

    public void taketoRate() {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity  object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private int[] getXlocationOfView(final ImageView imageView) {

        crownView.setVisibility(View.VISIBLE);
        imageView.setTranslationY(-10);
        int[] locationValues = new int[2];
        imageView.getLocationOnScreen(locationValues);
        return locationValues;

    }


    private void scaleIcon(final ImageView worksIv, final float i) {
        Handler scaleHandler = new Handler();
        Runnable scaleTask = new Runnable() {
            @Override
            public void run() {

                if (i <= 1) {
                    worksIv.setColorFilter(null);
                } else {
                    worksIv.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.primary));
                }
                worksIv.animate().scaleX(i).scaleY(i).setDuration(300).start();
            }
        };
        scaleHandler.removeCallbacks(scaleTask);
        scaleHandler.post(scaleTask);
    }


    private void launchFragmentAndAnimate(final float start, final ImageView imageView) {

        if (previousImageView == null) {
            previousImageView = imageView;
            return;
        } else {
            scaleIcon(previousImageView, 1);
            previousImageView = imageView;
        }
        scaleIcon(imageView, 1.8f);
        int currentPixels = getXlocationOfView(imageView)[0];
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(start, currentPixels - 36);
//        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                assert crownView != null;
                crownView.setTranslationX((Float) valueAnimator.getAnimatedValue());
            }
        });
        previousPixles = currentPixels;
        valueAnimator.start();


    }

    private void launch_credits_dialog() {
        startActivity(new Intent(MainActivity.this, CreditsActivity.class));
    }

    public void buildDialog_and_search(SearchQuotesViewModel searchQuotesViewModel) {

        final Dialog dialog = new Dialog(MainActivity.this, R.style.EditTextDialog_non_floater_2);
        dialog.setContentView(R.layout.search_chooser_layout);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.EditTextDialog_non_floater_2;
        dialog.getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, android.R.color.white));

        dialog.setCancelable(true);


        final RelativeLayout root;
        final EditText search_Query;
        final ImageView search;
        final RadioGroup radioGroup;
        ImageView close;
        final RecyclerView result_rv;
        final LinearLayout search_header;
        final ProgressBar loader;
        final String[] selected_type = new String[1];
        selected_type[0] = IMAGES;
        AdView adView;
        final TextView search_term;


        search_Query = dialog.findViewById(R.id.header_name);
        search = dialog.findViewById(R.id.search);
        radioGroup = dialog.findViewById(R.id.rd_group);
        close = dialog.findViewById(R.id.search_btn);
        search_header = dialog.findViewById(R.id.search_header);
        root = dialog.findViewById(R.id.root);
        result_rv = dialog.findViewById(R.id.result_rv);
        loader = dialog.findViewById(R.id.loader);
        loader.setVisibility(GONE);

        adView = dialog.findViewById(R.id.adView);
        search_term = dialog.findViewById(R.id.search_term);
        AdUtils.loadBannerAd(adView, MainActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            result_rv.setClipToOutline(true);
        }
        TextUtils.findText_and_applyTypeface(root, MainActivity.this);
        TextUtils.findText_and_applyamim_slideup(root, MainActivity.this);

        search_Query.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    search_Query.clearFocus();
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(search_Query.getWindowToken(), 0);
                    search.performClick();
                    return true;
                }
                return false;
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_images: {
                        selected_type[0] = IMAGES;
                        search.performClick();
                    }
                    break;
                    case R.id.rb_quotes: {
                        selected_type[0] = QUOTES;
                        search.performClick();

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
                            getImagesForQuery(result_rv, search_Query.getText().toString(), loader, radioGroup, search_header, search_term);
                        }
                        break;
                        case QUOTES: {
                            setQuotesForSearchQuery(result_rv, search_Query.getText().toString(), loader, radioGroup, search_header, search_term);
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
                TextUtils.findText_and_applyamim_slidedown(root, MainActivity.this);

                dialog.dismiss();

            }
        });

        search_term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioGroup.setVisibility(View.VISIBLE);
                search_header.setVisibility(View.VISIBLE);
                search_term.setVisibility(GONE);

                radioGroup.setVisibility(View.VISIBLE);
                search_header.setVisibility(View.VISIBLE);
                search_header.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup));
                radioGroup.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup));

                search_term.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slidedown));
                search_term.setVisibility(GONE);
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(@NonNull DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
                    TextUtils.findText_and_applyamim_slidedown(root, MainActivity.this);
                    dialog.dismiss();
                    return true;
                }
                return false;

            }
        });
        dialog.show();


    }

    private void getImagesForQuery(final RecyclerView rv, final String query,
                                   final ProgressBar loader, @NonNull final RadioGroup radioGroup,
                                   @NonNull final LinearLayout search_header,
                                   @NonNull final TextView search_term) {
        rv.setAdapter(null);
        loader.setVisibility(View.VISIBLE);
        final String request = Constants.UNSPLASH_SEARCH_IMAGES_API + "&query=" + query;
        new Restutility(MainActivity.this).getUnsplashImagesForSearchQuery(MainActivity.this, new SearchImagesResponseListener() {
            @Override
            public void onSuccess(@NonNull final ResGetSearchResultsDto response) {

                loader.setVisibility(GONE);
                rv.setAdapter(null);
                rv.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

                Log.i("VALUE FROM UNSPLASH", String.valueOf(response.getResults()));
                if (response.getResults().length <= 0) {
                    Toast_Snack_Dialog_Utils.show_ShortToast(MainActivity.this, getString(R.string.Currently_no_wallpaper));
                } else {
                    rv.setAdapter(new SearchWallpaperAdapter(MainActivity.this, response.getResults(), new SearchWallpaperAdapter.OnWallpaperClickListener() {
                        @Override
                        public void onWallpaperClicked(int position, ArrayList<Unsplash_Image> unsplash_images, ImageView itemView) {
                            Intent intent = new Intent(MainActivity.this, WallpapersPagerActivity.class);

                            intent.putExtra(Constants.Pager_position, position);
                            intent.putExtra(Constants.PAGER_LIST_WALL_OBKHECTS, unsplash_images);
                            intent.putExtra(Constants.is_from_fav, false);

                            startActivity(intent);
                        }
                    }, 3));

                    radioGroup.setVisibility(GONE);
                    search_header.setVisibility(GONE);
                    search_header.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup));
                    radioGroup.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup));

                    search_term.setText(query);
                    search_term.setVisibility(View.VISIBLE);
                    search_term.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup));

                }
            }

            @Override
            public void onError(String error) {
                Log.e("ERROR FROM UNSPLASH", error);
                loader.setVisibility(GONE);

//                dataView.setVisibility(View.GONE);
                Toast_Snack_Dialog_Utils.show_ShortToast(MainActivity.this, getString(R.string.Failed));
            }
        }, request);

    }

    private void setQuotesForSearchQuery(final RecyclerView result_rv, final String query, final ProgressBar loader, @NonNull RadioGroup radioGroup, @NonNull LinearLayout search_header, @NonNull TextView searchTerm) {
        searchQuotesViewModel = new SearchQuotesViewModel(getApplication(), query);
        loader.setVisibility(View.VISIBLE);
        result_rv.setAdapter(null);
        result_rv.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));


        final ArrayList<QuotesTable> finalArrayList = new ArrayList<>();

        final LiveData<List<QuotesTable>> quotesLiveData1 = searchQuotesViewModel.getQuotesbyCategorySearchResultLiveData();
        quotesLiveData1.observe(this, new Observer<List<QuotesTable>>() {
            @Override
            public void onChanged(@Nullable List<QuotesTable> quotesTables) {
                finalArrayList.addAll(quotesTables);

            }
        });
        final LiveData<List<QuotesTable>> quotesLiveData2 = searchQuotesViewModel.getQuotesbyQuerySearchResultLiveData();
        quotesLiveData2.observe(this, new Observer<List<QuotesTable>>() {
            @Override
            public void onChanged(@Nullable List<QuotesTable> quotesTables) {
                finalArrayList.addAll(quotesTables);
                if (finalArrayList.size() <= 0) {
                    loader.setVisibility(GONE);
                    Toast_Snack_Dialog_Utils.show_ShortToast(MainActivity.this, getString(R.string.no_quotes_available));
                } else {
                    loader.setVisibility(GONE);
                    adapter = (new LocalAdapter(MainActivity.this, null, new LocalAdapter.OnQuoteClickListener() {
                        @Override
                        public void onQuoteClicked(int position, @NonNull GradientDrawable color, QuotesTable quote, View view) {
                            Intent intent = new Intent(MainActivity.this, QuoteDetailsActivity.class);
                            Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, new Pair<View, String>(view, getString(R.string.root_quote))).toBundle();
                            intent.putExtra(Constants.INTENT_QUOTE_OBJECT_KEY, quote.getId()
                            );
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                intent.putExtra(IntentConstants.GRADIENT_COLOR1, color.getColors());

                            } else {

                            }
                            startActivity(intent, bundle);
                        }
                    }));
                    adapter.addData(finalArrayList);
                    adapter.notifyDataSetChanged();
                    result_rv.setAdapter(adapter);
                }
            }
        });


        radioGroup.setVisibility(GONE);
        search_header.setVisibility(GONE);
        search_header.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup));
        radioGroup.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup));
        result_rv.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup));

        searchTerm.setText(query);
        searchTerm.setVisibility(View.VISIBLE);
        searchTerm.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup));


    }

    private void checkPermission_and_proceed() {
        if (PermissionUtils.isPermissionAvailable(MainActivity.this)) {
            return;
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSION_REQUEST_);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {

                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStack();

            } else {
                Toast_Snack_Dialog_Utils.createDialog(MainActivity.this, getString(R.string.warning), getString(R.string.are_you_sure_close), getString(R.string.cancel), getString(R.string.close), new Toast_Snack_Dialog_Utils.Alertdialoglistener() {
                    @Override
                    public void onPositiveselection() {
                        finishAffinity();
                        System.exit(0);
                    }

                    @Override
                    public void onNegativeSelection() {

                    }
                });
            }
        }
    }
}
