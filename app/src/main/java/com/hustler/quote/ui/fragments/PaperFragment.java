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
import android.transition.Explode;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
public class PaperFragment extends Fragment {

    ImageView imageView;
    Unsplash_Image textView;
    RadioGroup radioGroup;
    RadioButton rd1, rd2, rd3;
    SharedPreferences sharedPreferences;
    LinearLayout loading_layout;

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

    public void setExplodeAnimations() {
        Explode explode = new Explode();
        explode.setDuration(300);
        getActivity().getWindow().setEnterTransition(explode);
        getActivity().getWindow().setExitTransition(explode);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wallpaer_fragment_layout, container, false);
        imageView = view.findViewById(R.id.wallpaper_image);
        loading_layout = view.findViewById(R.id.loading_layout);
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
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            setExplodeAnimations();
//        }
        return view;
    }

    private void updateSharedPreferences(int i) {
        loading_layout.setVisibility(View.VISIBLE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Constants.Shared_prefs_image_resol_key, i);
        editor.apply();

        switch (i) {
            case 0: {
                Glide.with(getActivity()).load(textView.getUrls().getRegular()).crossFade().centerCrop().listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        loading_layout.setVisibility(View.GONE);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        loading_layout.setVisibility(View.GONE);
                        return false;
                    }
                }).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
            }
            break;
            case 1: {
                Glide.with(getActivity()).load(textView.getUrls().getFull()).crossFade().centerCrop().listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        loading_layout.setVisibility(View.GONE);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        loading_layout.setVisibility(View.GONE);
                        return false;
                    }
                }).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
            }
            break;
            case 2: {
                Glide.with(getActivity()).load(textView.getUrls().getRaw()).crossFade().centerCrop().listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        loading_layout.setVisibility(View.GONE);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        loading_layout.setVisibility(View.GONE);
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
        Glide.with(getActivity()).load(textView.getUrls().getRegular()).crossFade().centerCrop().
                listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        loading_layout.setVisibility(View.GONE);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        loading_layout.setVisibility(View.GONE);
                        return false;
                    }
                }).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);


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
