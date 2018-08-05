package com.hustler.quotzy.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hustler.quotzy.R;
import com.hustler.quotzy.ui.Widgets.CarouselAdapter;
import com.hustler.quotzy.ui.Widgets.PagerTransformer;
import com.hustler.quotzy.ui.activities.MainActivity;
import com.hustler.quotzy.ui.activities.QuotesFragment;
import com.hustler.quotzy.ui.apiRequestLauncher.Constants;
import com.hustler.quotzy.ui.pojo.OffLineQuotes;
import com.hustler.quotzy.ui.utils.TextUtils;
import com.hustler.quotzy.ui.utils.Toast_Snack_Dialog_Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RelativeLayout relativeLayout;
    private String mParam1;
    private String mParam2;

    private RelativeLayout root;
    private WebView instagram;
    private Button create;
    private LinearLayout qwL;
    private Button quotes;
    private Button wallpapers;
    private LinearLayout wfL;
    private Button Works;
    private Button fav;
    private Button Search;
    private TextView followUs;
    private LinearLayout shareLayoutImage;
    private ImageView facebook;
    private ImageView whatsapp;
    private ImageView instagramAcnt;
    private ImageView twitter;
    private ImageView youtube;
    private OnFragmentInteractionListener mListener;
    private ViewPager viewPager;
    private CarouselAdapter pagerAdapter;

    int current_page = 0;
    int totalNumberOfpagesOriginal = 25;
    int MULTIPLIER = 25;
    int numberInOneSlides = totalNumberOfpagesOriginal * MULTIPLIER;
    Timer pagerTimer;


    public MainFragment() {
        // Required empty public constructor
    }


    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        relativeLayout = view.findViewById(R.id.root);
        findViews(view);
        assert null != getActivity();
        TextUtils.findText_and_applyTypeface(relativeLayout, getActivity());
        instagram.getSettings().setJavaScriptEnabled(true);
//        instagram.loadUrl("https://www.instagram.com/quotzy_/");
        setUpCarousel(new Gson().fromJson(Constants.offlineQuote, OfflineQuotesData.class).getData());
        return view;
    }

    class OfflineQuotesData {
        public ArrayList<OffLineQuotes> getData() {
            return data;
        }

        public void setData(ArrayList<OffLineQuotes> data) {
            this.data = data;
        }

        ArrayList<OffLineQuotes> data = new ArrayList<>();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void findViews(View view) {
        root = view.findViewById(R.id.root);
        instagram = view.findViewById(R.id.instagram);
        create = view.findViewById(R.id.create);
        qwL = view.findViewById(R.id.qw_l);
        quotes = view.findViewById(R.id.quotes);
        wallpapers = view.findViewById(R.id.wallpapers);
        wfL = view.findViewById(R.id.wf_l);
        Works = view.findViewById(R.id.Works);
        fav = view.findViewById(R.id.fav);
        Search = view.findViewById(R.id.Search);
        followUs = view.findViewById(R.id.follow_us);
        shareLayoutImage = view.findViewById(R.id.share_layout_image);
        facebook = view.findViewById(R.id.facebook);
        whatsapp = view.findViewById(R.id.whatsapp);
        instagramAcnt = view.findViewById(R.id.instagram_acnt);
        twitter = view.findViewById(R.id.twitter);
        youtube = view.findViewById(R.id.youtube);
        viewPager = view.findViewById(R.id.carousel_pager);

        create.setOnClickListener(this);
        quotes.setOnClickListener(this);
        wallpapers.setOnClickListener(this);
        Works.setOnClickListener(this);
        fav.setOnClickListener(this);
        Search.setOnClickListener(this);

        facebook.setOnClickListener(this);
        whatsapp.setOnClickListener(this);
        instagramAcnt.setOnClickListener(this);
        twitter.setOnClickListener(this);
        youtube.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        assert null != getActivity();

        MainActivity mainActivity = (MainActivity) getActivity();
        if (v == create) {
            mainActivity.fab.performClick();
        } else if (v == quotes) {
            mainActivity.launchFragment(new QuotesFragment());
        } else if (v == wallpapers) {
            mainActivity.launchFragment(new WallpaperFragment());
        } else if (v == Works) {
            mainActivity.launchFragment(new UserWorkFragment());
        } else if (v == fav) {
            mainActivity.launchFragment(new UserFavuritesFragment());
        } else if (v == Search) {
            mainActivity.buildDialog_and_search();
        } else if (v == facebook) {
            startActivity(newFacebookIntent(getActivity().getPackageManager(), "https://www.facebook.com/quotzy/"));
        } else if (v == whatsapp) {
            launchWhatsapp();
        } else if (v == instagramAcnt) {
            launchInstagram();
        } else if (v == twitter) {
            launchTwitter();
        } else if (v == youtube) {
            launchYoutube();
        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public static Intent newFacebookIntent(PackageManager pm, String url) {
        Uri uri = Uri.parse(url);
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                // http://stackoverflow.com/a/24547437/1048340
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            } else {
                uri = Uri.parse(url);
            }

        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    private void launchWhatsapp() {
        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
        String url = null;

        if (isWhatsappInstalled) {
            try {
                url = "https://api.whatsapp.com/send?phone=" + "+917780187505" + "&text=" + URLEncoder.encode("FROM QUOTZY USER", "UTF-8");
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setPackage("com.whatsapp");
                sendIntent.setData(Uri.parse(url));
                if (sendIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    getActivity().startActivity(sendIntent);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), "Whatsapp not installed");
            Uri uri = Uri.parse("market://details?id=com.whatsapp");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(goToMarket);

        }
    }

    private void launchInstagram() {
        Uri uri = Uri.parse("https://instagram.com/_u/quotzy_");
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
        sendIntent.setPackage("com.instagram.android");
        if (sendIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            getActivity().startActivity(sendIntent);
        } else {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/quotzy_/")));
        }
    }

    private void launchTwitter() {
        Uri uri = Uri.parse("twitter://user/Quotzy1");
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
        sendIntent.setPackage("com.twitter.android");
        if (sendIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            getActivity().startActivity(sendIntent);
        } else {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/Quotzy1")));
        }
    }

    private void launchYoutube() {
        Uri uri = Uri.parse("vnd.youtube://user/channel/\" + UCSUCVzCBf6rPDWMKea3Sv_A");
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
        sendIntent.setPackage("com.youtube");
        if (sendIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            getActivity().startActivity(sendIntent);
        } else {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCSUCVzCBf6rPDWMKea3Sv_A")));
        }
    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getActivity().getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    private void setUpCarousel(ArrayList<OffLineQuotes> banners) {
        DisplayMetrics metrics = new DisplayMetrics();
        if (getActivity() != null) {
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            viewPager.setPageMargin(-(int) (metrics.widthPixels / 2.15));
            pagerAdapter = new CarouselAdapter(getChildFragmentManager(), MainFragment.this, banners);
            PagerTransformer transformer = new PagerTransformer(R.id.root_container);
            viewPager.setPageTransformer(false, transformer);
            viewPager.setCurrentItem(1);
            viewPager.setOffscreenPageLimit(2);
            viewPager.setAdapter(pagerAdapter);
            MULTIPLIER = totalNumberOfpagesOriginal = banners.size();
            if (banners.size() <= 0) {
                viewPager.setVisibility(View.GONE);
            } else {
                viewPager.setVisibility(View.VISIBLE);
//                setPagerTimer(25, banners.size());
            }
        }

    }

    private void setPagerTimer(int i, final int totlpages) {
        if (pagerTimer != null) pagerTimer.cancel();
        pagerTimer = new Timer();

        pagerTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (viewPager == null) {
                            } else {
                                try {
                                    current_page = (current_page + 1) % numberInOneSlides;
                                    if (current_page == totlpages - 1) {
                                        viewPager.setCurrentItem(0, true);
                                        current_page = 0;
                                    } else {
                                        viewPager.setCurrentItem(current_page, true);

                                    }
                                } catch (Exception ignored) {

                                }
                            }
                        }
                    });
                }

            }
        }, 0, i * 1000);
    }

}
