package com.hustler.quote.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hustler.quote.R;

/**
 * Created by anvaya5 on 15/12/2017.
 */

public class CustomFontsFragment extends DialogFragment {
    private RecyclerView fontsRv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.font_rv_layout,container,false);
        return view;
    }}
