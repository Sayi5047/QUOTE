package com.hustler.quote.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hustler.quote.R;

/**
 * Created by Sayi on 27-01-2018.
 */

public class PaperFragment extends Fragment {

    ImageView imageView;
    String textView;

    public static PaperFragment newInstance(String textView) {
        PaperFragment wallpaperFragmente = new PaperFragment();
        Bundle args = new Bundle();
        args.putString("IMAGEURL", textView);
        wallpaperFragmente.setArguments(args);
        return wallpaperFragmente;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.textView = getArguments().getString("IMAGEURL");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wallpaer_fragment_layout, container, false);
        imageView = (ImageView) view.findViewById(R.id.wallpaper_image);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        Log.i("CREATEDDDDD","CREATEDDDD");
        Log.i("CREATEDDDDD",textView);
        Glide.with(getActivity()).load(textView).crossFade().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("ATTACH","ATTACHEDDD");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("DESTROY","DESTROYED");

    }
}
