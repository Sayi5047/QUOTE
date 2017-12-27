package com.hustler.quote.ui.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hustler.quote.R;

/**
 * Created by anvaya5 on 20/12/2017.
 */

public class UserFavuritesFragment extends Fragment {
    ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fav_layout, container, false);
        imageView = (ImageView) view.findViewById(R.id.iv);
        Glide.with(getActivity()).load(Uri.parse("http://drive.google.com/uc?export=view&id=1-xt_qFGV_IsKJ7ZH2iJ7OL9a3WQB0ERs")).
        centerCrop().crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        return view;
    }
}
