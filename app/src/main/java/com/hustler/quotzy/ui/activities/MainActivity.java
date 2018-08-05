package com.hustler.quotzy.ui.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
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
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.hustler.quotzy.R;
import com.hustler.quotzy.ui.adapters.LocalAdapter;
import com.hustler.quotzy.ui.adapters.WallpaperAdapter;
import com.hustler.quotzy.ui.apiRequestLauncher.Constants;
import com.hustler.quotzy.ui.apiRequestLauncher.Restutility;
import com.hustler.quotzy.ui.database.QuotesDbHelper;
import com.hustler.quotzy.ui.fragments.CategoriesFragment;
import com.hustler.quotzy.ui.fragments.MainFragment;
import com.hustler.quotzy.ui.fragments.UserFavuritesFragment;
import com.hustler.quotzy.ui.fragments.UserWorkFragment;
import com.hustler.quotzy.ui.fragments.WallpaperFragment;
import com.hustler.quotzy.ui.pojo.Quote;
import com.hustler.quotzy.ui.pojo.UnsplashImages_Collection_Response;
import com.hustler.quotzy.ui.pojo.Unsplash_Image_collection_response_listener;
import com.hustler.quotzy.ui.pojo.unspalsh.Unsplash_Image;
import com.hustler.quotzy.ui.utils.AdUtils;
import com.hustler.quotzy.ui.utils.IntentConstants;
import com.hustler.quotzy.ui.utils.TextUtils;
import com.hustler.quotzy.ui.utils.Toast_Snack_Dialog_Utils;

import java.util.ArrayList;

import static android.view.View.GONE;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, com.hustler.quotzy.ui.fragments.MainFragment.OnFragmentInteractionListener {


    final String IMAGES = "images";
    final String QUOTES = "quotes";
    LocalAdapter adapter;
    private static final long MOVE_DEFAULT_TIME = 1000;
    private static final long FADE_DEFAULT_TIME = 300;
    FragmentTransaction transaction;
    android.support.v4.app.FragmentManager fragmentManager;
    Fragment initialFragment;
    TextView header_name;
    LinearLayout linearLayout;
    ActionBarDrawerToggle drawerToggle;
    CoordinatorLayout main_view;
    QuotesFragment quotesFragment;
    public FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_rect));
            getWindow().setClipToOutline(true);
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.WHITE));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        main_view = findViewById(R.id.main_view);

        setSupportActionBar(toolbar);
        linearLayout = findViewById(R.id.header_ll);

        header_name = findViewById(R.id.header_name);
        TextUtils.setFont(MainActivity.this, header_name, Constants.FONT_CIRCULAR);

        header_name.setVisibility(GONE);
        new CountDownTimer(2000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                header_name.setVisibility(View.VISIBLE);
            }
        }.start();
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                intent.putExtra(Constants.INTENT_IS_FROM_EDIT_KEY, 1);
                startActivity(intent);
                overridePendingTransition(R.anim.slideup, R.anim.slidedown);
            }
        });

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                main_view.setTranslationX(slideOffset * drawerView.getWidth());
//                main_view.setTranslationY(slideOffset * drawerView.getWidth());
                main_view.setTranslationZ(slideOffset * drawerView.getWidth() * 20);
                drawer.bringChildToFront(drawerView);
                drawerView.setBackgroundResource(R.color.white_apple);
                drawerView.bringToFront();
                main_view.setElevation(16);
                drawer.requestLayout();

            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextUtils.setMenu_Font(navigationView.getMenu(), MainActivity.this);
//        TextUtils.findText_and_applyTypeface(linearLayout, MainActivity.this);
//        navigationView.getHeaderView(0);
        loadInitialFragment();
        for (int i = 0; i < navigationView.getHeaderCount(); i++) {

            TextUtils.findText_and_applyTypeface(((LinearLayout) navigationView.getHeaderView(i)), MainActivity.this);
        }

        MobileAds.initialize(this, Constants.ADS_APP_ID);


    }

    private void loadInitialFragment() {
        initialFragment = MainFragment.newInstance();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slideup, R.anim.slidedown);
        transaction.replace(R.id.root, initialFragment);
        transaction.commit();
        fab.setVisibility(GONE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        if (this != null) {
            TextUtils.setMenu_Font(menu, MainActivity.this);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search: {
                buildDialog_and_search();
            }
            break;
            case R.id.action_rate: {
                taketoRate();

            }
            break;
            case R.id.action_Pro_features: {
                Intent intent = new Intent(MainActivity.this, ProfeaturesActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slideup, R.anim.slidedown);
            }
            break;

            case R.id.action_Pro_about: {
                Intent intent = new Intent(MainActivity.this, ProfeaturesActivity.class);
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

    private void launch_credits_dialog() {
        final Dialog dialog = new Dialog(MainActivity.this, R.style.EditTextDialog_non_floater);
        dialog.setContentView(R.layout.credits_dialog_layout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog_non_floater;
//        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(andro));
        dialog.setCancelable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
        }
        final RelativeLayout root = dialog.findViewById(R.id.root_Rl);
        final Button bt_cose = dialog.findViewById(R.id.bt_close);
        TextUtils.findText_and_applyTypeface(root, MainActivity.this);
        dialog.show();
        TextUtils.findText_and_applyamim_slideup(root, MainActivity.this);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
                    TextUtils.findText_and_applyamim_slidedown(root, MainActivity.this);
                    dialog.dismiss();
                    return true;
                } else {
                    return false;
                }
            }
        });
        bt_cose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextUtils.findText_and_applyamim_slidedown(root, MainActivity.this);
                dialog.dismiss();
            }
        });

    }

    public void buildDialog_and_search() {

        final Dialog dialog = new Dialog(MainActivity.this, R.style.EditTextDialog_non_floater_2);
        dialog.setContentView(R.layout.search_chooser_layout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog_non_floater_2;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(andro));
        dialog.setCancelable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, android.R.color.white));
        }


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
//        setImages(result_rv, query, loader, radioGroup, search_header, search_term);
//        setQuotes(result_rv, query, loader);
//        search_Query.setText(query);
        TextUtils.findText_and_applyTypeface(root, MainActivity.this);
        TextUtils.findText_and_applyamim_slideup(root, MainActivity.this);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_images: {
//                        setImages(result_rv, query, loader);
                        selected_type[0] = IMAGES;
                        search.performClick();
                    }
                    break;
                    case R.id.rb_quotes: {
//                        setQuotes(result_rv, query, loader);
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
                            setImages(result_rv, search_Query.getText().toString(), loader, radioGroup, search_header, search_term);
                        }
                        break;
                        case QUOTES: {
                            setQuotes(result_rv, search_Query.getText().toString(), loader, radioGroup, search_header, search_term);
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
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
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

    private void setImages(final RecyclerView rv, final String query, final ProgressBar loader, final RadioGroup radioGroup, final LinearLayout search_header, final TextView search_term) {
        rv.setAdapter(null);
        loader.setVisibility(View.VISIBLE);
        final String request = Constants.API_GET_Collections_FROM_UNSPLASH + "&query=" + query + "&per_page=30";
        new Restutility(MainActivity.this).getUnsplash_Collections_Images(MainActivity.this, new Unsplash_Image_collection_response_listener() {
            @Override
            public void onSuccess(final UnsplashImages_Collection_Response response) {

                loader.setVisibility(GONE);
                rv.setAdapter(null);
                rv.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

                Log.i("VALUE FROM UNSPLASH", String.valueOf(response.getResults().length));
                if (response.getResults().length <= 0) {
//                    dataView.setVisibility(View.GONE);
                    Toast_Snack_Dialog_Utils.show_ShortToast(MainActivity.this, getString(R.string.Currently_no_wallpaper));
                } else {
//                    dataView.setVisibility(View.VISIBLE);
                    rv.setAdapter(new WallpaperAdapter(MainActivity.this, response.getResults(), new WallpaperAdapter.OnWallpaperClickListener() {
                        @Override
                        public void onWallpaperClicked(int position, ArrayList<Unsplash_Image> unsplash_images, ImageView itemView) {
//                            Toast_Snack_Dialog_Utils.show_ShortToast(MainActivity.this, wallpaper.getUser().getFirst_name());
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

    private void setQuotes(final RecyclerView result_rv, final String query, final ProgressBar loader, RadioGroup radioGroup, LinearLayout search_header, TextView searchTerm) {
        loader.setVisibility(View.VISIBLE);
        result_rv.setAdapter(null);
        result_rv.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));

        final ArrayList<Quote>[] quoteslisttemp = new ArrayList[]{new ArrayList<>(), new ArrayList<>()};
        final ArrayList<Quote> finalArrayList;
        finalArrayList = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                quoteslisttemp[0] = (ArrayList<Quote>) new QuotesDbHelper(MainActivity.this).getQuotesBystring(query);
                quoteslisttemp[1] = (ArrayList<Quote>) new QuotesDbHelper(MainActivity.this).getQuotesByCategory(query);
                quoteslisttemp[1].remove(quoteslisttemp[0]);
                finalArrayList.addAll(quoteslisttemp[0]);
                finalArrayList.addAll(quoteslisttemp[1]);

            }
        }).run();
        if (finalArrayList.size() <= 0) {
            loader.setVisibility(GONE);
            Toast_Snack_Dialog_Utils.show_ShortToast(MainActivity.this, getString(R.string.no_quotes_available));
        } else {
            loader.setVisibility(GONE);
            adapter = (new LocalAdapter(MainActivity.this, null, new LocalAdapter.OnQuoteClickListener() {
                @Override
                public void onQuoteClicked(int position, GradientDrawable color, Quote quote, View view) {
                    Intent intent = new Intent(MainActivity.this, QuoteDetailsActivity.class);
                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, new Pair<View, String>(view, getString(R.string.root_quote))).toBundle();
                    intent.putExtra(Constants.INTENT_QUOTE_OBJECT_KEY, quote);
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
            radioGroup.setVisibility(GONE);
            search_header.setVisibility(GONE);
            search_header.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup));
            radioGroup.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup));
            result_rv.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup));

            searchTerm.setText(query);
            searchTerm.setVisibility(View.VISIBLE);
            searchTerm.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup));

        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            launchFragment(new QuotesFragment());
        } else if (id == R.id.nav_camera_2) {
            launchFragment(new CategoriesFragment());
        } else if (id == R.id.nav_gallery) {
            launchFragment(new WallpaperFragment());
        } else if (id == R.id.nav_slideshow) {
            launchFragment(new UserFavuritesFragment());
        } else if (id == R.id.nav_manage) {
            launchFragment(new UserWorkFragment());
        } else if (id == R.id.nav_share) {
            taketoRate();
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

    public void launchFragment(Fragment fragment) {

//        Fade exitFade = new Fade();
//        exitFade.setDuration(FADE_DEFAULT_TIME);
////        Fragment prevFrag = getSupportFragmentManager().findFragmentById(R.id.root);
//        initialFragment.setExitTransition(exitFade);

        transaction = getSupportFragmentManager().beginTransaction();
//        Fade enterFade = new Fade();
//        enterFade.setStartDelay(2 * FADE_DEFAULT_TIME);
//        enterFade.setDuration(FADE_DEFAULT_TIME);
//        fragment.setEnterTransition(fragment);
        transaction.setCustomAnimations(R.anim.slideup, R.anim.slidedown);
        transaction.replace(R.id.root, fragment);

        transaction.addToBackStack("my_fragment");
        transaction.commitAllowingStateLoss();
        fab.setVisibility(View.VISIBLE);

    }

    public void taketoRate() {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity  object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
                if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                    fab.setVisibility(View.GONE);
                } else {
                    fab.setVisibility(View.VISIBLE);
                }
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
