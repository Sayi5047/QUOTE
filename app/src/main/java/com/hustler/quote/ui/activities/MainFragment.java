package com.hustler.quote.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.transition.Explode;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hustler.quote.R;
import com.hustler.quote.ui.adapters.LocalAdapter;
import com.hustler.quote.ui.adapters.MainAdapter;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.apiRequestLauncher.QuotesApiResponceListener;
import com.hustler.quote.ui.apiRequestLauncher.Restutility;
import com.hustler.quote.ui.database.QuotesDbHelper;
import com.hustler.quote.ui.pojo.Quote;
import com.hustler.quote.ui.pojo.QuotesFromFC;
import com.hustler.quote.ui.superclasses.App;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Sayi on 07-10-2017.
 */

public class MainFragment extends Fragment {

    RecyclerView rv;
    ProgressBar loader;
    MainAdapter adapter;
    ImageView quote_of_day;
    TextView quote_author;
    int selectedQuote;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mainfragmentlayout, container, false);
        rv = (RecyclerView) view.findViewById(R.id.main_rv);
        rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        loader = (ProgressBar) view.findViewById(R.id.loader);
        quote_of_day = (ImageView) view.findViewById((R.id.quote_of_day));
        quote_author = (TextView) view.findViewById(R.id.quote_of_day_author);
//        quote_of_day.setTypeface(App.getZingCursive(getActivity(), Constants.FONT_ZINGCURSIVE));
        quote_author.setTypeface(App.getZingCursive(getActivity(), Constants.FONT_ZINGCURSIVE));
        quote_author.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slideup));
        quote_of_day.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slideup));

        quote_of_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rv.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                    rv.setLayoutManager(new GridLayoutManager(getActivity(), 2));

                } else if (rv.getLayoutManager() instanceof GridLayoutManager) {
                    rv.setLayoutManager(new LinearLayoutManager(getActivity()));

                } else if (rv.getLayoutManager() instanceof LinearLayoutManager) {
                    rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                }


            }

        });
//        getRandomQuotes();
        setAdapter(rv);

        return view;
    }

    private void setAdapter(RecyclerView rv) {
        rv.setAdapter(new LocalAdapter(
                getActivity(),
                (ArrayList<Quote>) new QuotesDbHelper(getActivity()).getAllQuotes(),
                new LocalAdapter.OnQuoteClickListener() {
                    @Override
                    public void onQuoteClicked(int position, int color, Quote quote, View view1) {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            getActivity().getWindow().setEnterTransition(new Slide());
//                            Intent intent = new Intent(getActivity(), QuoteDetailsActivity.class);
//                            intent.putExtra(Constants.INTENT_QUOTE_OBJECT_KEY, quote);
//                            ActivityOptionsCompat options = ActivityOptionsCompat.
//                                    makeSceneTransitionAnimation(getActivity(),
//                                            view1,
//                                            getString(R.string.quotes_author_transistion));
//                            startActivity(intent, options.toBundle());
//                        } else {
                        Intent intent = new Intent(getActivity(), QuoteDetailsActivity.class);
                        intent.putExtra(Constants.INTENT_QUOTE_OBJECT_KEY, quote);
                        startActivity(intent);
//                        }
                    }
                }));
        loader.setVisibility(View.GONE);

    }

    private void getRandomQuotes() {

        new Restutility(getActivity()).getRandomQuotes(getActivity(), new QuotesApiResponceListener() {
            @Override
            @Nullable
            public void onSuccess(List<QuotesFromFC> quotes) {
                rv.setAdapter(new MainAdapter(getActivity(), quotes, new MainAdapter.OnItemClicListener() {
                    @Override
                    public void onItemClickHappened(QuotesFromFC quotesFromFC, View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getActivity().getWindow().setEnterTransition(new Slide());
                            Intent intent = new Intent(getActivity(), QuoteDetailsActivity.class);
                            intent.putExtra(Constants.INTENT_QUOTE_OBJECT_KEY, quotesFromFC);
                            ActivityOptionsCompat options = ActivityOptionsCompat.
                                    makeSceneTransitionAnimation(getActivity(),
                                            view,
                                            getString(R.string.quotes_author_transistion));
                            startActivity(intent, options.toBundle());
                        } else {
                            Intent intent = new Intent(getActivity(), QuoteDetailsActivity.class);
                            intent.putExtra(Constants.INTENT_QUOTE_OBJECT_KEY, quotesFromFC);
                            startActivity(intent);
                        }

                    }
                }));
                rv.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slideup));

                selectedQuote = new Random().nextInt(quotes.size() - 1) + 1;
//                quote_of_day.setText(quotes.get(selectedQuote).getBody());
//                adapter.updateQuotes(quotes);

                quote_author.setText(quotes.get(selectedQuote).getAuthor());


            }

            @Override
            public void onError(String message) {
                loader.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("MainAdapter","ON RESUME");
        rv.setAdapter(null);
        setAdapter(rv);

    }

    //    public void setLayout(int i) {
//        if(rv!=null){
//            switch (i){
//                case 1:
//                    rv.setLayoutManager(new LinearLayoutManager(getActivity()));
//                    getRandomQuotes();
//
//                    break;
//                case 2:
//                    rv.setLayoutManager(new GridLayoutManager(getActivity(),2));
//                    getRandomQuotes();
//
//                    break;
//                case 3: rv.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
//                    getRandomQuotes();
//
//                    break;
//            }
//        }
//    }
}
