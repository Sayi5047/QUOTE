package com.hustler.quote.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hustler.quote.R;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.pojo.unspalsh.Unsplash_Image;

/**
 * Created by Sayi on 27-01-2018.
 */

public class PaperFragment extends Fragment {

    ImageView imageView;
    Unsplash_Image textView;
    RadioGroup radioGroup;
    RadioButton rd1, rd2, rd3;
    SharedPreferences sharedPreferences;

    public static PaperFragment newInstance(Unsplash_Image textView) {
        PaperFragment wallpaperFragmente = new PaperFragment();
        Bundle args = new Bundle();
        args.putSerializable("IMAGEURL", textView);
        wallpaperFragmente.setArguments(args);
        return wallpaperFragmente;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.textView = (Unsplash_Image) getArguments().getSerializable("IMAGEURL");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wallpaer_fragment_layout, container, false);
        imageView = view.findViewById(R.id.wallpaper_image);
        radioGroup = view.findViewById(R.id.rd_group);
        rd1 = view.findViewById(R.id.rb_regular);
        rd2 = view.findViewById(R.id.rb_hd);
        rd3 = view.findViewById(R.id.rb_uhd);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            radioGroup.setClipToOutline(true);
            rd1.setClipToOutline(true);
            rd2.setClipToOutline(true);
            rd3.setClipToOutline(true);
        }
        radioGroup.setClipToPadding(true);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        return view;
    }

    private void updateSharedPreferences(int i) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Constants.Shared_prefs_image_resol_key, i);
        editor.commit();
        final ProgressBar progressBar = new ProgressBar(getActivity());

        switch (i) {
            case 0: {
                progressBar.setVisibility(View.VISIBLE);
                Glide.with(getActivity()).load(textView.getUrls().getRegular()).crossFade().centerCrop().listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;

                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);

            }
            break;
            case 1: {
                progressBar.setVisibility(View.VISIBLE);

                Glide.with(getActivity()).load(textView.getUrls().getFull()).crossFade().centerCrop().listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);

                        return false;
                    }
                }).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);

            }
            break;
            case 2: {
                progressBar.setVisibility(View.VISIBLE);

                Glide.with(getActivity()).load(textView.getUrls().getRaw()).crossFade().centerCrop().listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);

                        return false;
                    }
                }).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);

            }
            break;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        Log.i("CREATEDDDDD","CREATEDDDD");
//        Log.i("CREATEDDDDD",textView);
        Glide.with(getActivity()).load(textView.getUrls().getRegular()).crossFade().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.rb_regular) {
                    updateSharedPreferences(0);
                } else if (checkedId == R.id.rb_hd) {
                    updateSharedPreferences(1);

                } else if (checkedId == R.id.rb_uhd) {
                    updateSharedPreferences(2);

                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("ATTACH", "ATTACHEDDD");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("DESTROY", "DESTROYED");

    }
}
