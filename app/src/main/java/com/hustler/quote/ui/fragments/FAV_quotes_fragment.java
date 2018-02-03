package com.hustler.quote.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hustler.quote.R;
import com.hustler.quote.ui.activities.QuoteDetailsActivity;
import com.hustler.quote.ui.adapters.LocalAdapter;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.database.QuotesDbHelper;
import com.hustler.quote.ui.pojo.Quote;

import java.util.ArrayList;

/**
 * Created by anvaya5 on 02/02/2018.
 */

public class FAV_quotes_fragment extends android.support.v4.app.Fragment {
    ImageView iv_no_fav;
    RecyclerView rv_imag_no_fv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fav_quotes_fragment_layout, container, false);

        iv_no_fav = (ImageView) view.findViewById(R.id.iv);
        rv_imag_no_fv = (RecyclerView) view.findViewById(R.id.rv);
        rv_imag_no_fv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        setAdapter(rv_imag_no_fv);
        return view;
    }

    private void setAdapter(RecyclerView recyclerView) {
        final ArrayList<Quote>[] arrayLists = new ArrayList[]{new ArrayList<>()};
        new Thread(new Runnable() {
            @Override
            public void run() {
                arrayLists[0] = (ArrayList<Quote>) new QuotesDbHelper(getActivity()).getAllFav_Quotes();
            }
        }).run();
        recyclerView.setAdapter(new LocalAdapter(getActivity(), arrayLists[0], new LocalAdapter.OnQuoteClickListener() {
            @Override
            public void onQuoteClicked(int position, int color, Quote quote, View view) {
                Intent intent = new Intent(getActivity(), QuoteDetailsActivity.class);
                intent.putExtra(Constants.INTENT_QUOTE_OBJECT_KEY, quote);
                startActivity(intent);
//                Intent intent = new Intent(
//                        WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
//                intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
//                        new ComponentName(getActivity(), MyWallpaperService.class));
//                startActivity(intent);
            }
        }));

    }

    @Override
    public void onResume() {
        super.onResume();
        setAdapter(rv_imag_no_fv);

    }
}
