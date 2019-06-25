package io.hustler.qtzy.ui.activities;

import android.animation.Animator;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.Calendar;

import io.hustler.qtzy.R;
import io.hustler.qtzy.ui.Recievers.NotifAlarmReciever;
import io.hustler.qtzy.ui.Services.DailyNotificationService;
import io.hustler.qtzy.ui.adapters.LocalAdapter;
import io.hustler.qtzy.ui.adapters.TabsFragmentPagerAdapter;
import io.hustler.qtzy.ui.adapters.WallpaperAdapter;
import io.hustler.qtzy.ui.apiRequestLauncher.Constants;
import io.hustler.qtzy.ui.apiRequestLauncher.Restutility;
import io.hustler.qtzy.ui.apiRequestLauncher.Shared_prefs_constants;
import io.hustler.qtzy.ui.pojo.UnsplashImages_Collection_Response;
import io.hustler.qtzy.ui.pojo.listeners.SearchImagesResponseListener;
import io.hustler.qtzy.ui.pojo.unspalsh.Unsplash_Image;
import io.hustler.qtzy.ui.utils.AdUtils;
import io.hustler.qtzy.ui.utils.ColorUtils;
import io.hustler.qtzy.ui.utils.TextUtils;
import io.hustler.qtzy.ui.utils.Toast_Snack_Dialog_Utils;

import static android.view.View.GONE;

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
@Deprecated
/*V!*/
public class HomeActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private AppBarLayout appBar;
    private FloatingActionButton floatingActionButton;
    private ViewPager mainPager;
    private TabLayout tab_layout;
    Window window;
    Animator anim;
    TextView header_name;
    CoordinatorLayout rootView;
    int adapterPos;

    int[] colors;
    Toolbar toolbar;
    final String IMAGES = "images";
    final String QUOTES = "quotes";
    private AdView mAdView;
    String query;
    @Nullable
    LocalAdapter adapter;
    Menu menu;
    Intent alarm_intent, notif_alarm_intent;
    @Nullable
    AlarmManager alarmManager;
    boolean service_Started;
    PendingIntent pendingIntent, notif_pending_intent;
    @NonNull
    int[] icons = new int[]{R.drawable.ic_library, R.drawable.ic_launcher, R.drawable.ic_picture,

            R.drawable.ic_lover, R.drawable.ic_canvas2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_main);
        MobileAds.initialize(HomeActivity.this, Constants.ADS_APP_ID);
        findViews();
        window = this.getWindow();


        colors = new int[]{ContextCompat.getColor(HomeActivity.this, R.color.pink_400), ContextCompat.getColor(HomeActivity.this, R.color.colorAccent), ContextCompat.getColor(HomeActivity.this, R.color.green_300), ContextCompat.getColor(HomeActivity.this, R.color.orange_300), ContextCompat.getColor(HomeActivity.this, R.color.textColor)};

        editTabLayout();
        alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

//        setUpNotifications();
//        if(!service_Started){
//            stratServices();
//        }


    }

    private void stratServices() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Log.d(this.getClass().getSimpleName(), String.valueOf(sharedPreferences.getBoolean(Shared_prefs_constants.SHARED_PREFS_NOTIFICATION_SERVICES_RUNNING_KEY, false)));

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 30);
        notif_alarm_intent = new Intent(getApplicationContext(), NotifAlarmReciever.class);
        notif_alarm_intent.putExtra(Constants.ALARM_INTENT__IS_DOWNLOAD_INTENT_FLAG, false);
        notif_pending_intent = PendingIntent.getBroadcast(getApplicationContext(), 1, notif_alarm_intent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, notif_pending_intent);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Shared_prefs_constants.SHARED_PREFS_NOTIFICATION_SERVICES_RUNNING_KEY, true);
        editor.apply();
        Log.i("ALARM  NOTIF BOOT R", "SET");
    }

    private void setTabColors(final TabLayout tabLayout) {
        tabLayout.getTabAt(0).getIcon().setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.black_overlay), PorterDuff.Mode.DST_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.black_overlay), PorterDuff.Mode.DST_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.black_overlay), PorterDuff.Mode.DST_IN);
        tabLayout.getTabAt(3).getIcon().setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.black_overlay), PorterDuff.Mode.DST_IN);
        tabLayout.getTabAt(4).getIcon().setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.black_overlay), PorterDuff.Mode.DST_IN);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab) {
                tab.getIcon().clearColorFilter();
//                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(60, 60); //set new width & Height
//                params.gravity = Gravity.CENTER;
//                tab_layout.getChildAt(tab.getPosition()).setLayoutParams(params);
//                tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ffffff"));
            }

            @Override
            public void onTabUnselected(@NonNull TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.black_overlay), PorterDuff.Mode.DST_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private void setUpNotifications() {

        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : am.getRunningServices(Integer.MAX_VALUE)) {
            String serv = service.service.getClassName();
            if (serv.equals(DailyNotificationService.class)) {
                service_Started = true;
                break;
            } else {
                service_Started = false;

            }
        }
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
                    TextUtils.setFont(HomeActivity.this, textView, Constants.FONT_CIRCULAR);
                }
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
//        setAnimation(appBar);
//        setAnimation(floatingActionButton);
//        setAnimation(mainPager);
//        setAnimation(tab_layout);

//        setUpNotifications();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        invalidateOptionsMenu();
        this.menu = menu;
        return super.onPrepareOptionsMenu(menu);

    }


    private void findViews() {

        appBar = findViewById(R.id.app_bar);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        floatingActionButton = findViewById(R.id.floatingActionButton);
        mainPager = findViewById(R.id.main_pager);
        tab_layout = findViewById(R.id.tab_layout);
        rootView = findViewById(R.id.root);
        tab_layout.setupWithViewPager(mainPager);
        mAdView = findViewById(R.id.adView);
        header_name = findViewById(R.id.header_name);
        TextUtils.setFont(HomeActivity.this, header_name, Constants.FONT_CIRCULAR);
        loadAds();
        mainPager.setAdapter(new TabsFragmentPagerAdapter(this, getSupportFragmentManager()));
        mainPager.setCurrentItem(1);
        mainPager.setOnPageChangeListener(this);
//        mainPager.setPageTransformer(false,new WallpaperPageTransformer());
        floatingActionButton.setOnClickListener(this);

        header_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
            }
        });

////        getIntentData(getIntent());
//        tab_layout.getTabAt(0).setIcon(ContextCompat.getDrawable(getApplicationContext(), icons[0]));
//        tab_layout.getTabAt(1).setIcon(ContextCompat.getDrawable(getApplicationContext(), icons[1]));
//        tab_layout.getTabAt(2).setIcon(ContextCompat.getDrawable(getApplicationContext(), icons[2]));
//        tab_layout.getTabAt(3).setIcon(ContextCompat.getDrawable(getApplicationContext(), icons[3]));
//        tab_layout.getTabAt(4).setIcon(ContextCompat.getDrawable(getApplicationContext(), icons[4]));
//        setTabColors(tab_layout);
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        getIntentData(intent);
    }

    private void getIntentData(Intent intent) {
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            query = intent.getStringExtra(SearchManager.QUERY);
//            //use the query to search your data somehow
//            Toast_Snack_Dialog_Utils.show_ShortToast(HomeActivity.this, query);
//            buildDialog_and_search(query);
//        }
        adapterPos = intent.getIntExtra(Constants.HOME_SCREEN_NUMBER, 1);
        if (mainPager != null && mainPager.getAdapter() != null) {
            switch (adapterPos) {
                case 0:
                    floatingActionButton.performClick();
                    break;
                case 1:
                    mainPager.setCurrentItem(1);
                    break;
                case 2:
                    mainPager.setCurrentItem(1);
                    break;
                case 3:
                    mainPager.setCurrentItem(2);
                    break;
                case 4:
                    mainPager.setCurrentItem(3);
                    break;
                case 5:
                    mainPager.setCurrentItem(4);
                    break;
            }
        }
    }


    private void buildDialog_and_search() {

        final Dialog dialog = new Dialog(HomeActivity.this, R.style.EditTextDialog_non_floater_2);
        dialog.setContentView(R.layout.search_chooser_layout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog_non_floater_2;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(andro));
        dialog.setCancelable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setStatusBarColor(ContextCompat.getColor(HomeActivity.this, android.R.color.white));
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
        AdUtils.loadBannerAd(adView, HomeActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            result_rv.setClipToOutline(true);
        }
//        setImages(result_rv, query, loader, radioGroup, search_header, search_term);
//        setQuotes(result_rv, query, loader);
//        search_Query.setText(query);
        TextUtils.findText_and_applyTypeface(root, HomeActivity.this);
        TextUtils.findText_and_applyamim_slideup(root, HomeActivity.this);

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
//                            setQuotes(result_rv, search_Query.getText().toString(), loader, radioGroup, search_header, search_term);
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

    private void setImages(final RecyclerView rv, final String query, final ProgressBar loader, @NonNull final RadioGroup radioGroup, @NonNull final LinearLayout search_header, @NonNull final TextView search_term) {
        rv.setAdapter(null);
        loader.setVisibility(View.VISIBLE);
        final String request = Constants.UNSPLASH_SEARCH_IMAGES_API + "&query=" + query + "&per_page=30";
        new Restutility(HomeActivity.this).getUnsplashImagesForSearchQuery(HomeActivity.this, new SearchImagesResponseListener() {
            @Override
            public void onSuccess(@NonNull final UnsplashImages_Collection_Response response) {

                loader.setVisibility(GONE);
                rv.setAdapter(null);
                rv.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

                Log.i("VALUE FROM UNSPLASH", String.valueOf(response.getResults().size()));
                if (response.getResults().size() <= 0) {
//                    dataView.setVisibility(View.GONE);
                    Toast_Snack_Dialog_Utils.show_ShortToast(HomeActivity.this, getString(R.string.Currently_no_wallpaper));
                } else {
//                    dataView.setVisibility(View.VISIBLE);
                    rv.setAdapter(new WallpaperAdapter(HomeActivity.this, (Unsplash_Image[]) response.getResults().toArray(), new WallpaperAdapter.OnWallpaperClickListener() {
                        @Override
                        public void onWallpaperClicked(int position, ArrayList<Unsplash_Image> unsplash_images, ImageView itemView) {
//                            Toast_Snack_Dialog_Utils.show_ShortToast(HomeActivity.this, wallpaper.getUser().getFirst_name());
                            Intent intent = new Intent(HomeActivity.this, WallpapersPagerActivity.class);

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
                Toast_Snack_Dialog_Utils.show_ShortToast(HomeActivity.this, getString(R.string.Failed));
            }
        }, request);

    }

//    private void setQuotes(final RecyclerView result_rv, final String query, final ProgressBar loader, @NonNull RadioGroup radioGroup, @NonNull LinearLayout search_header, @NonNull TextView searchTerm) {
//        loader.setVisibility(View.VISIBLE);
//        result_rv.setAdapter(null);
//        result_rv.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false));
//
//        final ArrayList<Quote>[] quoteslisttemp = new ArrayList[]{new ArrayList<>(), new ArrayList<>()};
//        final ArrayList<Quote> finalArrayList;
//        finalArrayList = new ArrayList<>();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
////                quoteslisttemp[0] = (ArrayList<Quote>) new QuotesDbHelper(HomeActivity.this).getQuotesBystring(query);
////                quoteslisttemp[1] = (ArrayList<Quote>) new QuotesDbHelper(HomeActivity.this).getQuotesByCategory(query);
////                quoteslisttemp[1].remove(quoteslisttemp[0]);
////                finalArrayList.addAll(quoteslisttemp[0]);
////                finalArrayList.addAll(quoteslisttemp[1]);
//
//            }
//        }).run();
//        if (finalArrayList.size() <= 0) {
//            loader.setVisibility(GONE);
//            Toast_Snack_Dialog_Utils.show_ShortToast(HomeActivity.this, getString(R.string.no_quotes_available));
//        } else {
//            loader.setVisibility(GONE);
//            adapter = (new LocalAdapter(HomeActivity.this, null, new LocalAdapter.OnQuoteClickListener() {
//                @Override
//                public void onQuoteClicked(int position, @NonNull GradientDrawable color, Quote quote, View view) {
//                    Intent intent = new Intent(HomeActivity.this, QuoteDetailsActivity.class);
//                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(HomeActivity.this, new Pair<View, String>(view, getString(R.string.root_quote))).toBundle();
//                    intent.putExtra(Constants.INTENT_QUOTE_OBJECT_KEY, quote);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        intent.putExtra(IntentConstants.GRADIENT_COLOR1, color.getColors());
//
//                    } else {
//
//                    }
//                    startActivity(intent, bundle);
//                }
//            }));
//            adapter.addData(finalArrayList);
//            adapter.notifyDataSetChanged();
//            result_rv.setAdapter(adapter);
//            radioGroup.setVisibility(GONE);
//            search_header.setVisibility(GONE);
//            search_header.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup));
//            radioGroup.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup));
//            result_rv.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup));
//
//            searchTerm.setText(query);
//            searchTerm.setVisibility(View.VISIBLE);
//            searchTerm.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup));
//
//        }
//
//    }

    private void loadAds() {
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
        AdUtils.loadBannerAd(mAdView, HomeActivity.this);

    }

    private void setAnimation(View view) {
        view.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        final EditText editText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
//
//        editText.setHint("Search");
//
//        editText.setHintTextColor(getResources().getColor(android.R.color.white));
//        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));
//
//        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    buildDialog_and_search(editText.getText().toString());
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });
        if (this != null) {
            TextUtils.setMenu_Font(menu, HomeActivity.this);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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
                Intent intent = new Intent(HomeActivity.this, ProfeaturesActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slideup, R.anim.slidedown);
            }
            break;

            case R.id.action_Pro_about: {
                Intent intent = new Intent(HomeActivity.this, ProfeaturesActivity.class);
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

    public void taketoRate() {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity  object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void launch_credits_dialog() {
        final Dialog dialog = new Dialog(HomeActivity.this, R.style.EditTextDialog_non_floater);
        dialog.setContentView(R.layout.credits_dialog_layout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog_non_floater;
//        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(andro));
        dialog.setCancelable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setStatusBarColor(ContextCompat.getColor(HomeActivity.this, R.color.colorPrimary));
        }
        final RelativeLayout root = dialog.findViewById(R.id.root_Rl);
        final Button bt_cose = dialog.findViewById(R.id.bt_close);
        TextUtils.findText_and_applyTypeface(root, HomeActivity.this);
        dialog.show();
        TextUtils.findText_and_applyamim_slideup(root, HomeActivity.this);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(@NonNull DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
                    TextUtils.findText_and_applyamim_slidedown(root, HomeActivity.this);
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
                TextUtils.findText_and_applyamim_slidedown(root, HomeActivity.this);
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onClick(final View v) {
////        DIALOG FOR EDITORS
//        if (v == floatingActionButton) {
//            Button quoteId, meme, close;
//            TextView quote_tv, meme_tv, close_tv, select_tv;
//
//
//            // Handle clicks for floatingActionButton
//            final Dialog dialog = new Dialog(this, R.style.EditTextDialog_scaled);
//            final View view = View.inflate(this, R.layout.edit_chooser, null);
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog.setContentView(view);
//
//            quoteId = (Button) dialog.findViewById(R.id.Quotes);
//            meme = (Button) dialog.findViewById(R.id.meme);
//            close = (Button) dialog.findViewById(R.id.close);
//
//            quote_tv = (TextView) dialog.findViewById(R.id.Quotes_tv);
//            meme_tv = (TextView) dialog.findViewById(R.id.meme_tv);
//            close_tv = (TextView) dialog.findViewById(R.id.close_tv);
//            select_tv = (TextView) dialog.findViewById(R.id.select_tv);
//
//            TextUtils.setFont(HomeActivity.this, quote_tv, Shared_prefs_constants.FONT_CIRCULAR);
//            TextUtils.setFont(HomeActivity.this, meme_tv, Shared_prefs_constants.FONT_CIRCULAR);
//            TextUtils.setFont(HomeActivity.this, close_tv, Shared_prefs_constants.FONT_CIRCULAR);
//            TextUtils.setFont(HomeActivity.this, select_tv, Shared_prefs_constants.FONT_CIRCULAR);
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
//            quoteId.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // TODO: 02-12-2017 implement to go quotes editor
//                    dialog.dismiss();
//                    Intent intent = new Intent(HomeActivity.this, EditorActivity.class);
//                    intent.putExtra(Shared_prefs_constants.INTENT_IS_FROM_EDIT_KEY, 1);
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
        overridePendingTransition(R.anim.slideup, R.anim.slidedown);

    }


    /*VIEW PAGER METHODS*/
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        appBar.setBackgroundColor(ColorUtils.getHEaderColor(colors, position, positionOffset, HomeActivity.this));
//        mainPager.setBackgroundColor(ColorUtils.getHEaderColor(colors, position, positionOffset, HomeActivity.this));
//        tab_layout.setBackgroundColor(ColorUtils.getHEaderColor(colors, position, positionOffset, HomeActivity.this));
////        floatingActionButton.setBackgroundColor((getHEaderColor(position, positionOffset)));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.setStatusBarColor(ColorUtils.getHEaderColor(colors, position, positionOffset, HomeActivity.this));
//        }
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));

        header_name.setTextColor(ColorUtils.getHEaderColor(colors, position, positionOffset, HomeActivity.this));

        tab_layout.setTabTextColors(ContextCompat.getColor(getApplication(), R.color.textColor), ColorUtils.getHEaderColor(colors, position, positionOffset, HomeActivity.this));
        tab_layout.setSelectedTabIndicatorColor(ColorUtils.getHEaderColor(colors, position, positionOffset, HomeActivity.this));
        editTabLayout();
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
    }
}
