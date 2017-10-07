package com.hustler.quote.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.hustler.quote.R;
import com.hustler.quote.ui.adapters.MainAdapter;
import com.hustler.quote.ui.apiRequestLauncher.QuotesApiResponceListener;
import com.hustler.quote.ui.apiRequestLauncher.Restutility;
import com.hustler.quote.ui.pojo.QuotesFromFC;
import com.hustler.quote.ui.superclasses.App;

import java.util.List;

/**
 * Created by Sayi on 07-10-2017.
 */

public class MainFragment extends Fragment {

    RecyclerView rv;
    ProgressBar loader;
    MainAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.mainfragmentlayout,container,false);
        rv=(RecyclerView) view.findViewById(R.id.main_rv);
        rv.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        loader=(ProgressBar) view.findViewById(R.id.loader);

        getRandomQuotes();
        return view;
    }

    private void getRandomQuotes() {

        new Restutility(getActivity()).getRandomQuotes(getActivity(), new QuotesApiResponceListener() {
            @Override
            public void onSuccess(List<QuotesFromFC> quotes) {
                loader.setVisibility(View.GONE);
                rv.setAdapter(new MainAdapter(getActivity(),quotes));
//                adapter.updateQuotes(quotes);
            }

            @Override
            public void onError(String message) {
                loader.setVisibility(View.GONE);
                App.showToast(getActivity(),getString(R.string.no_quotes_available));
            }
        });
    }
}
